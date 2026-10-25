package com.salesflow.SalesFlowApplication.activity;


import com.salesflow.SalesFlowApplication.activity.dto.ActivityResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follow-ups")
public class FollowUpController {

    private final SalesActivityService activityService;

    public FollowUpController(SalesActivityService activityService) {
        this.activityService = activityService;
    }

    @PatchMapping("/{activityId}/complete")
    public ActivityResponse completeFollowUp(
            @PathVariable Long activityId
    ) {
        return activityService.completeFollowUp(activityId);
    }

    @GetMapping("/overdue")
    public Page<ActivityResponse> getOverdueFollowUps(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "activityDate",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return activityService.getOverdueFollowUps(pageable);
    }

    @GetMapping("/pending")
    public Page<ActivityResponse> getPendingFollowUps(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "activityDate",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return activityService.getPendingFollowUps(pageable);
    }
}

