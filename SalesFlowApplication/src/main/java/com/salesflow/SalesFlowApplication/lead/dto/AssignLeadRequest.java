package com.salesflow.SalesFlowApplication.lead.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AssignLeadRequest (

        @NotNull
        @Positive
        Long userId

){}
