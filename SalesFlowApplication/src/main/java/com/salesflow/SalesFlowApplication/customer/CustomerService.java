package com.salesflow.SalesFlowApplication.customer;

import com.salesflow.SalesFlowApplication.common.exception.DuplicateResourceException;
import com.salesflow.SalesFlowApplication.common.exception.ResourceNotFoundException;
import com.salesflow.SalesFlowApplication.customer.dto.CreateCustomerRequest;
import com.salesflow.SalesFlowApplication.customer.dto.CustomerResponse;
import com.salesflow.SalesFlowApplication.customer.dto.UpdateCustomerRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    @Transactional
    public CustomerResponse create(CreateCustomerRequest request){
        if(customerRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("A customer with thid email already exists");
        }
        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setCompany(request.company());
        customer.setPhone(request.phone());

        return toResponse(customerRepository.save(customer));
    }


    @Transactional
    public CustomerResponse getById(Long id) {
        return toResponse((findCustomer(id)));

    }

    @Transactional
    public Page<CustomerResponse> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Transactional
    public CustomerResponse update(
            Long id, UpdateCustomerRequest request) {

        Customer customer = findCustomer(id);

        customerRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "A customer with this email already exists"
                    );
                });

        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setCompany(request.company());
        customer.setPhone(request.phone());

        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = findCustomer(id);
        customerRepository.delete(customer);
    }

    public Customer findCustomer(Long id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id " + id
                        ));

    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getCompany(),
                customer.getPhone(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
    //@Transactional(readOnly = true)
    @Transactional
    public Page<CustomerResponse> searchCustomers(
            String search,
            String company,
            Pageable pageable) {

        String normalizedSearch =
                search == null || search.isBlank() ? null : search.trim();

        String normalizedCompany =
                company == null || company.isBlank() ? null : company.trim();

        return customerRepository
                .searchCustomers(normalizedSearch, normalizedCompany, pageable)
                .map(this::toResponse);
    }
}
