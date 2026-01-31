package com.platform.workflowservice.repository;

import com.platform.workflowservice.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    Optional<Ticket> findById(Long id);

    Page<Ticket> findAllByCreatedByUserId (String email, Pageable pageable);

    Page<Ticket> findAllByAssignedToUserId(String email, Pageable pageable);
}
