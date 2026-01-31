package com.platform.workflowservice.dto;

import com.platform.workflowservice.enums.TicketStatus;

public class TicketResponseDto {

    private Long id;
    private String title;
    private TicketStatus status;
    private SlaResponseDto sla;

    public TicketResponseDto(Long id, String title, TicketStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public TicketResponseDto() {

    }

    public SlaResponseDto getSla() {
        return sla;
    }

    public void setSla(SlaResponseDto sla) {
        this.sla = sla;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
