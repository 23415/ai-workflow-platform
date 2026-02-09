package com.platform.workflowservice.event;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class TicketUpdatedEvent {

    // ===== Event metadata =====
    private String eventId;              // UUID
    private String eventType;             // TICKET_UPDATED
    private Date occurredAt;

    // ===== Aggregate info =====
    private Long ticketId;

    // Who triggered the update
    private Long updatedByUserId;

    // ===== What changed =====
    private Map<String, String> oldValues;
    private Map<String, String> newValues;

    public TicketUpdatedEvent() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Date occurredAt) {
        this.occurredAt = occurredAt;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(Long updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    public Map<String, String> getOldValues() {
        return oldValues;
    }

    public void setOldValues(Map<String, String> oldValues) {
        this.oldValues = oldValues;
    }

    public Map<String, String> getNewValues() {
        return newValues;
    }

    public void setNewValues(Map<String, String> newValues) {
        this.newValues = newValues;
    }
}
