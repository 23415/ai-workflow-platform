package com.platform.workflowservice.repository;

import com.platform.workflowservice.entities.SLA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SlaRepository extends JpaRepository<SLA,Long> {

    Optional<SLA> findByTicketId(Long ticketId);

    @Query("SELECT s FROM SLA s WHERE s.Status = com.platform.workflowservice.enums.SlaStatus.ACTIVE AND s.deadline < :now")
    List<SLA> findBreachedSlas(@Param("now") Instant now);
}
