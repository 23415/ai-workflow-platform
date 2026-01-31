package com.platform.workflowservice.service;

import com.platform.workflowservice.dto.TicketRequestDto;
import com.platform.workflowservice.entities.Ticket;
import com.platform.workflowservice.enums.TicketStatus;
import com.platform.workflowservice.exception.ResourceNotFoundException;
import com.platform.workflowservice.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketService.class);


    private final TicketRepository ticketRepository;
    private final SlaService slaService;

    public TicketService(TicketRepository ticketRepository, SlaService slaService){
        log.info("Ticket Service Contructor invoked");
        this.ticketRepository = ticketRepository;
        this.slaService = slaService;
    }

    @Transactional
    public Ticket createTicket(TicketRequestDto req,Long userId){
        Ticket newTicket = new Ticket();

        newTicket.setTitle(req.getTitle());
        newTicket.setDescription(req.getDescription());
        newTicket.setCreatedByUserId(userId);

        Ticket savedTicket = ticketRepository.save(newTicket);

        slaService.createTicketSla(savedTicket.getId());

        return savedTicket;
    }

    public Ticket assignTicket(Long ticketId, Long userId) throws ResourceNotFoundException {
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(()-> new ResourceNotFoundException("Ticket does not exists"));

        ticket.setAssignedToUserId(userId);
        ticket.setStatus(TicketStatus.ASSIGNED);
        return ticketRepository.save(ticket);
    }

    public Ticket ticketStatusChange(Long ticketId, String status){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new ResourceNotFoundException("Ticket does not exists"));

        TicketStatus oldStatus = ticket.getStatus();
        TicketStatus newStatus = TicketStatus.valueOf(status);
        if(!oldStatus.canTransitionTo(newStatus)) {
            throw new IllegalStateException("cannot do transition from : "+oldStatus+"->"+newStatus);
        }

        ticket.setStatus(newStatus);
        Ticket savedTicket = ticketRepository.save(ticket);

        if(newStatus.equals(TicketStatus.CLOSED)){
            slaService.markSlaResolved(savedTicket.getId());
        }

        return savedTicket;
    }

    public Page<Ticket> getTicketByRole(
            String email,
            String role,
            Pageable pageable
    ){
        return switch (role){
            case "ROLE_ADMIN" -> ticketRepository.findAll(pageable);
            case "ROLE_AGENT" -> ticketRepository.findAllByAssignedToUserId(email, pageable);
            case "ROLE_USER" -> ticketRepository.findAllByCreatedByUserId(email, pageable);
            default -> throw new IllegalStateException("Invalid Role: " + role);
        };
    }

    public Ticket getTicketById(Long ticketId){
        log.info("Ticket Fetched using Id : {}",ticketId);
        return ticketRepository.findById(ticketId).orElseThrow(()-> new ResourceNotFoundException("No ticket found"));
    }
}
