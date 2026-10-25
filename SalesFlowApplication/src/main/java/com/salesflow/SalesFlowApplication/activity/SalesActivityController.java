package com.salesflow.SalesFlowApplication.activity;

import com.salesflow.SalesFlowApplication.activity.dto.ActivityResponse;
import com.salesflow.SalesFlowApplication.activity.dto.CreateActivityRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leads/{leadId}/activities")
public class SalesActivityController {

    private final SalesActivityService activityService;

    public SalesActivityController(SalesActivityService activityService){
        this.activityService=activityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityResponse createActivity(
            @PathVariable Long leadId,
            @Valid @RequestBody CreateActivityRequest request
            ){
        return activityService.createActivity(leadId,request);
    }

    @GetMapping
    public Page<ActivityResponse> getActivities(
            @PathVariable Long leadId,
            @ParameterObject
            @PageableDefault(
                    size=10,
                    sort="activityDate",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ){
        return activityService.getActivitiesForLead(leadId,pageable);
    }

    @DeleteMapping("/{activityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivity(@PathVariable Long leadId,@PathVariable Long activityId
                               ){
        activityService.deleteActivity(activityId);
    }






}
