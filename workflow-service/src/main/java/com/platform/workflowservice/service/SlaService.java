package com.platform.workflowservice.service;

import com.platform.workflowservice.entities.SLA;
import com.platform.workflowservice.enums.SlaStatus;
import com.platform.workflowservice.exception.ResourceNotFoundException;
import com.platform.workflowservice.repository.SlaRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class SlaService {

    private final SlaRepository slaRepository;

    SlaService(SlaRepository slaRepository){
        this.slaRepository = slaRepository;
    }

    public void createTicketSla(Long ticketId){
        Instant now = Instant.now();

        SLA sla = new SLA();
        sla.setTicketId(ticketId);
        sla.setDeadline(now.plus(5, ChronoUnit.MINUTES));
        sla.setStatus(SlaStatus.ACTIVE);
        sla.setStartTime(now);

        slaRepository.save(sla);
    }

    public void markSlaResolved(Long ticketId){
        SLA sla = slaRepository.findByTicketId(ticketId).orElseThrow(()-> new ResourceNotFoundException("No SLA found"));

        sla.setResolvedAt(Instant.now());
        sla.setStatus(SlaStatus.MET);

        slaRepository.save(sla);
    }
}
