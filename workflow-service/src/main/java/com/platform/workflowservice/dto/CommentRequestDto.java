package com.platform.workflowservice.dto;

public class CommentRequestDto {

    public Long ticketId;
    public String Content;
    public Long authorId;

    public Long getTicketId() {
        return ticketId;
    }
    public String getContent() {
        return Content;
    }

    public Long getAuthorId() {
        return authorId;
    }
}
