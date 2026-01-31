package com.platform.workflowservice.enums;

public enum TicketStatus {
    OPEN,
    ASSIGNED,
    IN_PROGRESS,
    RESOLVED,
    CLOSED;


    public boolean canTransitionTo(TicketStatus next){
        return switch (this){
            case OPEN -> next == ASSIGNED;
            case ASSIGNED -> next == IN_PROGRESS;
            case IN_PROGRESS -> next == RESOLVED;
            case RESOLVED -> next == CLOSED;
            case CLOSED -> false;
        };
    }
}
