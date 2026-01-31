package com.platform.workflowservice.controller;

import com.platform.workflowservice.dto.CommentRequestDto;
import com.platform.workflowservice.dto.CommentResponseDto;
import com.platform.workflowservice.entities.Comment;
import com.platform.workflowservice.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto req){
        System.out.println("req content"+req.getContent());
        Comment comment = commentService.createComment(req.getTicketId(), req.getContent(), req.getAuthorId());
        return convert(comment);
    }

    @GetMapping("/{id}")
    public List<CommentResponseDto> fetchCommentsById(@PathVariable Long id){
        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        List<Comment> commentsList = commentService.fetchComments(id);
        for(Comment comment : commentsList){
            responseDtoList.add(convert(comment));
        }
        return responseDtoList;
    }

    CommentResponseDto convert(Comment comment){
        CommentResponseDto res = new CommentResponseDto();
        res.setAuthorId(comment.getAuthorId());
        res.setContent(comment.getContent());
        res.setId(comment.getId());
        res.setCreatedAt(comment.getCreatedAt());
        res.setTicketId(comment.getTicketId());

        return res;
    }
}
