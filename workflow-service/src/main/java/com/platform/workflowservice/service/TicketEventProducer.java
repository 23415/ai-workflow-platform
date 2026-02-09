package com.platform.workflowservice.service;

import com.platform.workflowservice.event.SlaBreachedEvent;
import com.platform.workflowservice.event.TicketCreatedEvent;
import com.platform.workflowservice.event.TicketUpdatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketEventProducer {

    private final KafkaTemplate<String , Object> kafkaTemplate;

    public TicketEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void createTicketEvent(TicketCreatedEvent event){
        kafkaTemplate.send("ticket-created",event);
    }

    public void updateTicketEvent(TicketUpdatedEvent event){
        kafkaTemplate.send("ticket-updated",event);
    }

    public void slaBreachedEvent(SlaBreachedEvent event){
        kafkaTemplate.send("sla-breached",event);
    }
}
