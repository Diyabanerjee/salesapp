package com.salesflow.SalesFlowApplication.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""

            SELECT c
    FROM Customer c
    WHERE (
        :search IS NULL
        OR LOWER(c.firstName) LIKE LOWER(CONCAT(CONCAT('%', :search), '%'))
        OR LOWER(c.lastName) LIKE LOWER(CONCAT(CONCAT('%', :search), '%'))
        OR LOWER(c.email) LIKE LOWER(CONCAT(CONCAT('%', :search), '%'))
    )
    AND (
        :company IS NULL
        OR LOWER(c.company) LIKE LOWER(CONCAT(CONCAT('%', :company), '%'))
    )
    """)
    Page<Customer> searchCustomers(
            @Param("search") String search,
            @Param("company") String company,
            Pageable pageable
    );
    }