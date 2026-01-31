package com.platform.workflowservice.repository;

import com.platform.workflowservice.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findCommentByTicketId (@Param("id") Long ticketId);
}
