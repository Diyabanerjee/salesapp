package com.salesflow.SalesFlowApplication.common.exception;

//These exceptions represent two business outcomes:
//        - A customer requested by ID doesn't exist.
//        - A customer email is already registered.


public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
