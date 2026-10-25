package com.salesflow.SalesFlowApplication.lead;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface LeadRepository extends JpaRepository<Lead,Long> {

    @Query("""
        SELECT l FROM Lead l
        WHERE (
            :search IS NULL
            OR LOWER(l.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(l.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(l.email) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(l.company) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        AND (:status IS NULL OR l.status = :status)
        AND (:source IS NULL OR l.source = :source)
        """)
    Page<Lead> searchLeads(
            @Param("search") String search,
            @Param("status") LeadStatus status,
            @Param("source") LeadSource source,
            Pageable pageable
    );

    Page<Lead> findByAssignedTo_Id(Long userId, Pageable pageable);


}
