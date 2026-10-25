package com.salesflow.SalesFlowApplication.activity;

import com.salesflow.SalesFlowApplication.activity.dto.ActivityResponse;
import com.salesflow.SalesFlowApplication.activity.dto.CreateActivityRequest;
import com.salesflow.SalesFlowApplication.common.exception.ResourceNotFoundException;
import com.salesflow.SalesFlowApplication.lead.Lead;
import com.salesflow.SalesFlowApplication.lead.LeadRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class SalesActivityService {

    private final SalesActivityRepository activityRepository;
    private final LeadRepository leadRepository;

    public SalesActivityService(SalesActivityRepository activityRepository,LeadRepository leadRepository){
        this.activityRepository = activityRepository;
        this.leadRepository=leadRepository;
    }

    @Transactional
    public ActivityResponse createActivity(Long leadId, CreateActivityRequest request){
        if (request.activityType() == ActivityType.FOLLOW_UP) {
            if (request.activityDate() == null
                    || !request.activityDate().isAfter(LocalDateTime.now())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "A follow-up must have a future activityDate"
                );
            }
        }
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + leadId)

                );
        SalesActivity activity = new SalesActivity();
        activity.setLead(lead);
        activity.setActivityType(request.activityType());
        activity.setSubject(request.subject());
        activity.setDescription(request.description());
        activity.setActivityDate(request.activityDate());


         SalesActivity saved =  activityRepository.save(activity);

         return toResponse(saved);

    }

    public Page<ActivityResponse> getActivitiesForLead(
            Long leadId, Pageable pageable
    ){
        if(!leadRepository.existsById(leadId)){
            throw new ResourceNotFoundException((
                    "Lead not found with id:" + leadId
                    ));
        }
        return activityRepository.findBylead_Id(leadId,pageable)
                .map(this::toResponse);
    }

    public void deleteActivity(Long activityId){
        SalesActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Activity not found with id: " + activityId
                ));
        activityRepository.delete(activity);
    }


    public ActivityResponse toResponse(SalesActivity activity){
        return new ActivityResponse(
          activity.getId(),
          activity.getLead().getId(),
          activity.getActivityType(),
                activity.getSubject(),
                activity.getDescription(),
                activity.getActivityDate(),
                activity.getCreatedAt(),
                activity.isFollowUpCompleted()
        );
    }


    @Transactional
    public ActivityResponse completeFollowUp(Long activityId) {
        SalesActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Activity not found with id: " + activityId
                ));

        if (activity.getActivityType() != ActivityType.FOLLOW_UP) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only follow-up activities can be completed"
            );
        }

        activity.setFollowUpCompleted(true);

        return toResponse(activity);
    }


    @Transactional
    public Page<ActivityResponse> getOverdueFollowUps(Pageable pageable) {
        return activityRepository
                .findByActivityTypeAndActivityDateBeforeAndFollowUpCompletedFalse(
                        ActivityType.FOLLOW_UP,
                        LocalDateTime.now(),
                        pageable
                )
                .map(this::toResponse);
    }

    @Transactional
    public Page<ActivityResponse> getPendingFollowUps(Pageable pageable) {
        return activityRepository
                .findByActivityTypeAndFollowUpCompletedFalse(
                        ActivityType.FOLLOW_UP,
                        pageable
                )
                .map(this::toResponse);
    }




}
