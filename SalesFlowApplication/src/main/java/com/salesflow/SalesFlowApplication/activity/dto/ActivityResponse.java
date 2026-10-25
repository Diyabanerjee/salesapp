package com.salesflow.SalesFlowApplication.activity.dto;

import com.salesflow.SalesFlowApplication.activity.ActivityType;

import java.time.LocalDateTime;

public record ActivityResponse(
        Long id,
        Long leadId,
        ActivityType activityType,
        String subject,
        String description,
        LocalDateTime activityDate,
        LocalDateTime createdAt,
        boolean followUpCompleted
) {
}
