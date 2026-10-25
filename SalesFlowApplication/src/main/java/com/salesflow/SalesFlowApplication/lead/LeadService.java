package com.salesflow.SalesFlowApplication.lead;

import com.salesflow.SalesFlowApplication.common.exception.InvalidStatusTransitionException;
import com.salesflow.SalesFlowApplication.common.exception.ResourceNotFoundException;
import com.salesflow.SalesFlowApplication.customer.CustomerRepository;
import com.salesflow.SalesFlowApplication.customer.dto.CustomerResponse;
import com.salesflow.SalesFlowApplication.lead.dto.AssignLeadRequest;
import com.salesflow.SalesFlowApplication.lead.dto.CreateLeadRequest;
import com.salesflow.SalesFlowApplication.lead.dto.LeadResponse;
import com.salesflow.SalesFlowApplication.lead.dto.UpdateLeadRequest;
import com.salesflow.SalesFlowApplication.user.SalesUser;
import com.salesflow.SalesFlowApplication.user.SalesUserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import com.salesflow.SalesFlowApplication.customer.Customer;
import com.salesflow.SalesFlowApplication.customer.CustomerRepository;
import com.salesflow.SalesFlowApplication.customer.dto.CustomerResponse;
import com.salesflow.SalesFlowApplication.common.exception.DuplicateResourceException;
import com.salesflow.SalesFlowApplication.common.exception.InvalidStatusTransitionException;
import com.salesflow.SalesFlowApplication.user.SalesUserRepository;


@Service
public class LeadService {

    private final LeadRepository leadRepository; //Spring injects both repository beans into the service.
    private final CustomerRepository customerRepository;
    private final SalesUserRepository salesUserRepository;


    public LeadService(LeadRepository leadRepository, CustomerRepository customerRepository, SalesUserRepository salesUserRepository){
        this.leadRepository=leadRepository;
        this.customerRepository = customerRepository;
        this.salesUserRepository = salesUserRepository;
    }

    @Transactional
    public LeadResponse createLead(CreateLeadRequest request) {

        Lead lead = new Lead();

        lead.setFirstName(request.firstName());
        lead.setLastName((request.lastName()));
        lead.setEmail(request.email());
        lead.setCompany(request.company());
        lead.setPhone(request.phone());
        lead.setSource(request.source());

        Lead savedLead = leadRepository.save(lead);

        return toResponse(savedLead);
    }

    @Transactional
    public Page<LeadResponse> getAllLeads(Pageable pageable) {
        return leadRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Transactional
    public LeadResponse getLeadById(Long id) {
        Lead lead = findLead(id);
        return toResponse(lead);
    }

    @Transactional
    public LeadResponse updateLead(Long id, UpdateLeadRequest request) {
        Lead lead = findLead(id);

        lead.setFirstName(request.firstName());
        lead.setLastName(request.lastName());
        lead.setEmail(request.email());
        lead.setCompany(request.company());
        lead.setPhone(request.phone());
        lead.setSource(request.source());

        Lead updatedLead = leadRepository.save(lead);

        return toResponse(updatedLead);
    }

    @Transactional
    public void deleteLead(Long id) {
        leadRepository.deleteById(id);
    }

    private Lead findLead(Long id){
        return leadRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lead not found with id: " + id
                        ));
    }

    public LeadResponse toResponse(Lead lead){
        return new LeadResponse(lead.getId(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getEmail(),
                lead.getCompany(),
                lead.getPhone(),
                lead.getSource(),
                lead.getStatus(),
                lead.getCreatedAt(),
                lead.getUpdatedAt(),
                lead.getAssignedTo() != null
                ? lead.getAssignedTo().getId():null,
                lead.getAssignedTo() != null
                ? lead.getAssignedTo().getName():null

        );
    }


    @Transactional
    public LeadResponse updateLeadStatus(Long id, LeadStatus newStatus){
        Lead lead = findLead(id);

        LeadStatus currentStatus  = lead.getStatus();

        if(!isValidTransition(currentStatus,newStatus)){
            throw new InvalidStatusTransitionException(
                    "Cannot change lead status from" + currentStatus + "to" + newStatus

            );
        }

        lead.setStatus(newStatus);

        Lead updatedLead = leadRepository.save(lead);

        return toResponse(updatedLead);

    }

    private boolean isValidTransition(LeadStatus currentStatus, LeadStatus newStatus){
        return switch(currentStatus){
            case NEW -> newStatus == LeadStatus.CONTACTED || newStatus == LeadStatus.LOST;

            case CONTACTED -> newStatus == LeadStatus.QUALIFIED || newStatus == LeadStatus.LOST;

            case QUALIFIED -> newStatus == LeadStatus.SUCCESS || newStatus == LeadStatus.LOST;

            case SUCCESS , LOST -> false;
        };
    }

    //Lead to Customer
    @Transactional
    public CustomerResponse changeLeadToCustomer(Long leadId){

        // 1. Find the lead.
        Lead lead = findLead(leadId);

        // 2. Only qualified leads can be changed
        if(lead.getStatus()!= LeadStatus.QUALIFIED ){
            throw new InvalidStatusTransitionException("Only qualified leads can be changed. Current Status" + lead.getStatus());
        }

        // 3. Reject conversion if this customer already exists.
        if(customerRepository.existsByEmail(lead.getEmail())){
            throw new DuplicateResourceException("A customer already exists");
        }

        // 4. Create a customer from the lead's details.
        Customer customer = new Customer();
        customer.setFirstName(lead.getFirstName());
        customer.setLastName(lead.getLastName());
        customer.setEmail(lead.getEmail());
        customer.setCompany(lead.getCompany());
        customer.setPhone(lead.getPhone());

        // 5. Save the new customer.
        Customer saveCustomer = customerRepository.save(customer);

        //6. Mark the lead as changed
        lead.setStatus(LeadStatus.SUCCESS);
        leadRepository.save(lead);

        //7.Return the created customer
        return new CustomerResponse(
                saveCustomer.getId(),
                saveCustomer.getFirstName(),
                saveCustomer.getLastName(),
                saveCustomer.getEmail(),
                saveCustomer.getCompany(),
                saveCustomer.getPhone(),
                saveCustomer.getCreatedAt(),
                saveCustomer.getUpdatedAt()
        );

    }

    public Page<LeadResponse> searchLeads(
            String search,
            LeadStatus status,
            LeadSource source,
            Pageable pageable
    ) {
        if (search != null && search.isBlank()) {
            search = null;
        }

        return leadRepository
                .searchLeads(search, status, source, pageable)
                .map(this::toResponse);
    }


    public LeadResponse assignLead(Long leadId, AssignLeadRequest request) {
        Lead lead = findLead(leadId);

        SalesUser salesUser = salesUserRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sales user not found with id: " + request.userId()
                ));

        lead.setAssignedTo(salesUser);

        Lead savedLead = leadRepository.save(lead);

        return toResponse(savedLead);
    }

    public Page<LeadResponse> getLeadsAssignedToUser(Long userId, Pageable pageable){
        if(!salesUserRepository.existsById(userId)){
            throw new ResourceNotFoundException(
                    "Sales user not found with id: " + userId
                    );
        }

        return leadRepository
                .findByAssignedTo_Id(userId,pageable)
                .map(this::toResponse);
    }

}
