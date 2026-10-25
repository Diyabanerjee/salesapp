package com.salesflow.SalesFlowApplication.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SalesActivityRepository extends JpaRepository<SalesActivity, Long> {

    Page<SalesActivity> findBylead_Id(
            Long leadid,
            Pageable pageable
    );


    Page<SalesActivity>
    findByActivityTypeAndActivityDateBeforeAndFollowUpCompletedFalse(
            ActivityType activityType,
            LocalDateTime time,
            Pageable pageable
    );

    Page<SalesActivity>
    findByActivityTypeAndFollowUpCompletedFalse(
            ActivityType activityType,
            Pageable pageable
    );


}
