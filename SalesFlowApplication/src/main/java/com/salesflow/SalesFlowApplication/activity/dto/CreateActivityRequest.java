package com.salesflow.SalesFlowApplication.activity.dto;

import com.salesflow.SalesFlowApplication.activity.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateActivityRequest(

        @NotNull
        ActivityType activityType,


        @NotBlank
        @Size(max=200)
        String subject,

        @Size(max=1000)
        String description,

        LocalDateTime activityDate,

        boolean followUpCompleted

) {
}
