package com.platform.workflowservice.entities;

import com.platform.workflowservice.enums.SlaStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "sla",
    indexes = {
        @Index(name = "idx_sla_ticket", columnList = "ticket_id"),
        @Index(name = "idx_sla_deadline", columnList = "deadline")
    }
)
public class SLA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant deadline;

    private Instant resolvedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlaStatus Status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public SlaStatus getStatus() {
        return Status;
    }

    public void setStatus(SlaStatus status) {
        Status = status;
    }
}
