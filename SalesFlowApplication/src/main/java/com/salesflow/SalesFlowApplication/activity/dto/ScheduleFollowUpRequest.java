package com.salesflow.SalesFlowApplication.activity.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleFollowUpRequest(
        @NotNull
        @Future
        LocalDateTime followUpAt
) {
}
