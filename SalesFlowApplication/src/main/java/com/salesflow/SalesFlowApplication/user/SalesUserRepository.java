package com.salesflow.SalesFlowApplication.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesUserRepository extends JpaRepository<SalesUser,Long> {
}
