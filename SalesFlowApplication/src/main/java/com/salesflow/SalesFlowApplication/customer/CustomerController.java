package com.salesflow.SalesFlowApplication.customer;

import com.salesflow.SalesFlowApplication.customer.dto.CreateCustomerRequest;
import com.salesflow.SalesFlowApplication.customer.dto.CustomerResponse;
import com.salesflow.SalesFlowApplication.customer.dto.UpdateCustomerRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private CustomerService customerService;

    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CreateCustomerRequest request){
        return customerService.create(request);
    }

    @GetMapping("/{id}")
    public CustomerResponse getById(@PathVariable Long id){
        return customerService.getById(id);
    }

    @GetMapping
    public Page<CustomerResponse> getAll(
            @RequestParam(required=false) String search,
            @RequestParam(required=false) String company,
            @ParameterObject @PageableDefault(size=10, sort="createdAt", direction= Sort.Direction.DESC)Pageable pageable){
        //return customerService.getAll(pageable);
        return customerService.searchCustomers(
                search,company,pageable
                );
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest request){
        return customerService.update(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        customerService.delete(id);
    }

   @GetMapping("/health")
    public String health(){
        return "Customer API is running";
    }
}
