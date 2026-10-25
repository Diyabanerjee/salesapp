package com.salesflow.SalesFlowApplication.activity;

import com.salesflow.SalesFlowApplication.lead.Lead;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="sales_activities")
public class SalesActivity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="lead_id",nullable=false)
    private Lead lead;

    @Enumerated(EnumType.STRING)
    @Column(name="activity_type",nullable=false,length=20)
    private ActivityType activityType;

    @Column(nullable=false,length=200)
    private String subject;

    @Column(length=1000)
    private String description;


    @Column(name="activity_date",nullable=false)
    private LocalDateTime activityDate;

    @Column(name="created_at",nullable=false,updatable=false)
    private LocalDateTime createdAt;

    @Column(name="follow_up_completed",nullable=false)
    private boolean followUpCompleted = false;

//    public LocalDateTime getFollowUpAt() {
//        return followUpAt;
//    }

//    public void setFollowUpAt(LocalDateTime followUpAt) {
//        this.followUpAt = followUpAt;
//    }

    public boolean isFollowUpCompleted() {
        return followUpCompleted;
    }

    public void setFollowUpCompleted(boolean followUpCompleted) {
        this.followUpCompleted = followUpCompleted;
    }

//    @Column(name="follow_up_at")
//    private LocalDateTime followUpAt;


    protected SalesActivity(){

    }

    @PrePersist
    protected void onCreate(){
        if(activityDate == null){
            activityDate = LocalDateTime.now();
        }
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDateTime activityDate) {
        this.activityDate = activityDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
