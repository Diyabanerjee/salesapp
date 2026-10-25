package com.salesflow.SalesFlowApplication.lead.dto;

import com.salesflow.SalesFlowApplication.lead.LeadSource;
import com.salesflow.SalesFlowApplication.lead.LeadStatus;

import java.time.LocalDateTime;

public record LeadResponse(

        Long id,
        String firstName,
        String lastName,
        String email,
        String company,
        String phone,
        LeadSource source,
        LeadStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long assignedUserId,
        String assignedUserName
) {}
