package com.platform.workflowservice.entities;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "comments",
indexes = {
        @Index(name = "idx_comment_ticket", columnList = "ticket_id"),
        @Index(name = "idx_comment_author", columnList = "author_id")
})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false, columnDefinition = "Text")
    private String content;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false,updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate(){
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
