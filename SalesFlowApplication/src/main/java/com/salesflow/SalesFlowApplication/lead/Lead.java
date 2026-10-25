package com.salesflow.SalesFlowApplication.lead;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;

import com.salesflow.SalesFlowApplication.user.SalesUser;

@Entity
@Table(name="leads")
public class Lead {

    public SalesUser getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(SalesUser assignedTo) {
        this.assignedTo = assignedTo;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="assigned_to")
    private SalesUser assignedTo;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name",nullable=false,length=100)
    private String firstName;

    @Column(name="last_name",nullable=false,length=100)
    private String lastName;

    @Email
    @Column(nullable=false,length = 255)
    private String email;

    @Column(length=150)
    private String company;

    @Column(length=30)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=30)
    private LeadSource source;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=30)
    private LeadStatus status=LeadStatus.NEW;

    @Column(name="created_at",nullable=false,updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public Lead(){
    }

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public String getPhone() {
        return phone;
    }

    public LeadSource getSource() {
        return source;
    }

    public LeadStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSource(LeadSource source) {
        this.source = source;
    }

    public void setStatus(LeadStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
