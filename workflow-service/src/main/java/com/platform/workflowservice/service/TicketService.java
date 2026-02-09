package com.platform.workflowservice.service;

import com.platform.workflowservice.dto.AiPredictResponse;
import com.platform.workflowservice.dto.PageResponse;
import com.platform.workflowservice.dto.TicketRequestDto;
import com.platform.workflowservice.dto.TicketResponseDto;
import com.platform.workflowservice.entities.Ticket;
import com.platform.workflowservice.enums.TicketStatus;
import com.platform.workflowservice.event.TicketCreatedEvent;
import com.platform.workflowservice.event.TicketUpdatedEvent;
import com.platform.workflowservice.exception.ResourceNotFoundException;
import com.platform.workflowservice.repository.TicketRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketService.class);


    private final TicketRepository ticketRepository;
    private final SlaService slaService;
    private final TicketEventProducer ticketEventProducer;

    public TicketService(TicketRepository ticketRepository, SlaService slaService, TicketEventProducer ticketEventProducer){
        this.ticketEventProducer = ticketEventProducer;
        log.info("Ticket Service Contructor invoked");
        this.ticketRepository = ticketRepository;
        this.slaService = slaService;
    }

    @Transactional
    @CacheEvict(value = "tickets-page",allEntries = true)
    public Ticket createTicket(TicketRequestDto req,Long userId){
        Ticket newTicket = new Ticket();

        newTicket.setTitle(req.getTitle());
        newTicket.setDescription(req.getDescription());
        newTicket.setCreatedByUserId(userId);

        Ticket savedTicket = ticketRepository.save(newTicket);

        slaService.createTicketSla(savedTicket.getId());

        Date now = Date.from(Instant.now());
        ticketEventProducer.createTicketEvent(new TicketCreatedEvent(
                UUID.randomUUID().toString(),
                "Ticket-created",
                now,
                savedTicket.getId(),
                savedTicket.getTitle(),
                savedTicket.getDescription(),
                savedTicket.getStatus(),
                savedTicket.getCreatedByUserId(),
                savedTicket.getAssignedToUserId(),
                now
        ));


        return savedTicket;
    }

    public Ticket assignTicket(Long ticketId, Long userId) throws ResourceNotFoundException {
        TicketUpdatedEvent event = new TicketUpdatedEvent();

        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(()-> new ResourceNotFoundException("Ticket does not exists"));

        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("ticket-update");
        event.setTicketId(ticket.getId());
        event.setOccurredAt(Date.from(Instant.now()));
        event.setUpdatedByUserId(userId);
        event.setOldValues(Map.of(
                "AssignedToUserId", ticket.getAssignedToUserId().toString(),"status",ticket.getStatus().toString()
        ));

        ticket.setAssignedToUserId(userId);
        ticket.setStatus(TicketStatus.ASSIGNED);


        event.setNewValues(Map.of(
                "AssignedToUserId", ticket.getAssignedToUserId().toString(),"status",ticket.getStatus().toString()
        ));
        Ticket savedTicket = ticketRepository.save(ticket);
        ticketEventProducer.updateTicketEvent(event);
        return savedTicket;
    }

    //TODO: kafka for status change event
    @CacheEvict(value = {"tickets","tickets-page"}, key = "#ticketId",allEntries = true)
    public Ticket ticketStatusChange(Long ticketId, String status){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new ResourceNotFoundException("Ticket does not exists"));

        TicketStatus oldStatus = ticket.getStatus();
        TicketStatus newStatus = TicketStatus.valueOf(status);
        if(!oldStatus.canTransitionTo(newStatus)) {
            throw new IllegalStateException("cannot do transition from : "+oldStatus+"->"+newStatus);
        }

        TicketUpdatedEvent event = new TicketUpdatedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setEventType("ticket-update");
        event.setOccurredAt(Date.from(Instant.now()));
        event.setTicketId(ticketId);
        event.setUpdatedByUserId(ticket.getCreatedByUserId());
        event.setOldValues(Map.of("status",oldStatus.toString()));
        event.setNewValues(Map.of("status",newStatus.toString()));
        ticketEventProducer.updateTicketEvent(event);

        ticket.setStatus(newStatus);
        Ticket savedTicket = ticketRepository.save(ticket);

        if(newStatus.equals(TicketStatus.CLOSED)){
            slaService.markSlaResolved(savedTicket.getId());
        }

        return savedTicket;
    }

    @Cacheable(value = "tickets-page",
        key = "#role +':' + #email +':' + #page +':' + #size +':createdAt:DESC'"
    )
    public PageResponse<TicketResponseDto> getTicketByRole(
            String email,
            String role,
            int page,
            int size
    ){
        Pageable pageable = PageRequest.of(
                page,size, Sort.by("createdAt").descending()
        );
        log.warn("🔥 CACHE MISS — DB HIT {} page", email);
        log.error("page CACHE PROXY HASH = {}", System.identityHashCode(this));

        Page<Ticket> ticket =  switch (role){
            case "ROLE_ADMIN" -> ticketRepository.findAll(pageable);
            case "ROLE_AGENT" -> ticketRepository.findAllByAssignedToUserId(email, pageable);
            case "ROLE_USER" -> ticketRepository.findAllByCreatedByUserId(email, pageable);
            default -> throw new IllegalStateException("Invalid Role: " + role);
        };
        return new PageResponse<>(ticket.map(this::toDto));
    }

    @Cacheable(value = "tickets", key = "#ticketId")
    public TicketResponseDto getTicketById(Long ticketId) {
        log.warn("🔥 CACHE MISS — DB HIT {}", ticketId);
        log.error("CACHE PROXY HASH = {}", System.identityHashCode(this));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("No ticket found"));

        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getStatus()
        );
    }

    private TicketResponseDto toDto(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getStatus()
        );
    }
}
