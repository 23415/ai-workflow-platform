package com.platform.workflowservice.utils;

import com.platform.workflowservice.entities.SLA;
import com.platform.workflowservice.entities.Ticket;
import com.platform.workflowservice.enums.SlaStatus;
import com.platform.workflowservice.event.SlaBreachedEvent;
import com.platform.workflowservice.exception.ResourceNotFoundException;
import com.platform.workflowservice.repository.SlaRepository;
import com.platform.workflowservice.repository.TicketRepository;
import com.platform.workflowservice.service.TicketEventProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class SlaBreachScheduler {

    private final SlaRepository slaRepository;
    private final TicketRepository ticketRepository;
    private final TicketEventProducer ticketEventProducer;

    public SlaBreachScheduler(SlaRepository slaRepository, TicketRepository ticketRepository, TicketEventProducer ticketEventProducer) {
        this.slaRepository = slaRepository;
        this.ticketRepository = ticketRepository;
        this.ticketEventProducer = ticketEventProducer;
    }


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkBreachedStatus(){
        List<SLA> breachedSlaList = slaRepository.findBreachedSlas(Instant.now());

        for(SLA breachedSLA : breachedSlaList){
            breachedSLA.setStatus(SlaStatus.BREACHED);

            Ticket ticket = ticketRepository.findById(breachedSLA.getTicketId())
                    .orElseThrow(()-> new ResourceNotFoundException("Ticket not found"));

            SlaBreachedEvent event = new SlaBreachedEvent();

            event.setEventId(UUID.randomUUID().toString());
            event.setEventType("sla-breached");
            event.setBreachedAt(Date.from(Instant.now()));
            event.setTicketId(ticket.getId());
            event.setTicketStatus(SlaStatus.BREACHED.toString());
            event.setAssignedToUserId(ticket.getAssignedToUserId());
            event.setTicketTitle(ticket.getTitle());
            event.setOccurredAt(Date.from(Instant.now()));
            event.setCreatedByUserId(ticket.getCreatedByUserId());

            ticketEventProducer.slaBreachedEvent(event);
        }

        slaRepository.saveAll(breachedSlaList);
    }
}
