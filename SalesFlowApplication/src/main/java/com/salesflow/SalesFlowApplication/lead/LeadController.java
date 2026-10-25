package com.salesflow.SalesFlowApplication.lead;

import com.salesflow.SalesFlowApplication.customer.dto.CustomerResponse;
import com.salesflow.SalesFlowApplication.lead.dto.*;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/v1/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeadResponse createLead(@Valid @RequestBody CreateLeadRequest request) {
        return leadService.createLead(request);
    }

    @GetMapping("/{id}")
    public LeadResponse getLeadById(@PathVariable Long id) {
        return leadService.getLeadById(id);
    }

    @GetMapping
    public Page<LeadResponse> getAllLeads(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        return leadService.getAllLeads(pageable);

    }

    @PutMapping("/{id}")
    public LeadResponse updateLead(@PathVariable Long id,
                                   @Valid @RequestBody UpdateLeadRequest request
    ) {
        return leadService.updateLead(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
    }

    @PatchMapping("/{id}/status")
    public LeadResponse updateLeadStatus(@PathVariable Long id,
                                         @Valid @RequestBody UpdateLeadStatusRequest request
    ) {
        return leadService.updateLeadStatus(id, request.status());
    }


    @PostMapping("/{id}/change")
    public CustomerResponse changeLeadToCustomer(@PathVariable Long id){
        return leadService.changeLeadToCustomer(id);
    }


    @GetMapping("/search")
    public Page<LeadResponse> searchLeads(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(required = false) LeadSource source,
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return leadService.searchLeads(search, status, source, pageable);
    }

    @PatchMapping("/{id}/assign")
    public LeadResponse assignLead(@PathVariable Long id, @Valid @RequestBody AssignLeadRequest request){
        return leadService.assignLead(id,request);
    }

    @GetMapping("assigned-to/{userId}")
    public Page<LeadResponse> getLeadsAssignedToUser(@PathVariable Long userId,
                                                 @ParameterObject
                                                 @PageableDefault(
                                                         size=10,
                                                         sort="createdAt",
                                                         direction = Sort.Direction.DESC
                                                 )Pageable pageable
                                                 ){
        return leadService.getLeadsAssignedToUser(userId,pageable);
    }

}