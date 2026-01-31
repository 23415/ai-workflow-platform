package com.platform.workflowservice.dto;

import com.platform.workflowservice.enums.SlaStatus;

import java.time.Instant;

public class SlaResponseDto {

    private SlaStatus status;
    private Instant deadline;
    private Boolean breached;

    public SlaResponseDto(SlaStatus name, Instant deadline, boolean breached) {
        this.status = name;
        this.deadline = deadline;
        this.breached = breached;
    }

    public SlaStatus getStatus() {
        return status;
    }

    public void setStatus(SlaStatus status) {
        this.status = status;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public Boolean getBreached() {
        return breached;
    }

    public void setBreached(Boolean breached) {
        this.breached = breached;
    }
}
