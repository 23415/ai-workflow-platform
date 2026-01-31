package com.platform.workflowservice.service;

import com.platform.workflowservice.entities.Comment;
import com.platform.workflowservice.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Long ticketId,String content,Long authorId){
        Comment newComment = new Comment();
        newComment.setTicketId(ticketId);
        newComment.setContent(content);
        newComment.setAuthorId(authorId);
        System.out.println("comment request: "+content);
        return commentRepository.save(newComment);
    }

    public List<Comment> fetchComments(Long ticketId){
        return commentRepository.findCommentByTicketId(ticketId);
    }
}
