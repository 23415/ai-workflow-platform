package com.platform.workflowservice.dto;

import com.platform.workflowservice.enums.TicketStatus;

public class TicketRequestDto {

    private String title;
    private String description;
    private TicketStatus status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
