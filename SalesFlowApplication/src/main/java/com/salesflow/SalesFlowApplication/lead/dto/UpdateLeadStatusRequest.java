package com.salesflow.SalesFlowApplication.lead.dto;

import com.salesflow.SalesFlowApplication.lead.LeadStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateLeadStatusRequest(
        @NotNull
        LeadStatus status
) {}
