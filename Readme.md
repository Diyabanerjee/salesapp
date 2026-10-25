# SalesFlow — Enterprise CRM & Sales Management Platform

**A backend-focused CRM application built with Java 17, Spring Boot 3, Spring Data JPA, Hibernate, and Oracle Database.**

SalesFlow is a RESTful backend application designed to manage customers, track sales leads, manage lead lifecycles, and support lead-to-customer conversion. The project focuses on practical backend engineering concepts, including layered architecture, REST API design, data validation, database persistence, transaction management, pagination, and centralized exception handling.

The long-term goal is to evolve SalesFlow into a production-style CRM backend with secure authentication, role-based authorization, event-driven processing with Apache Kafka, automated testing, and containerized deployment.


## Overview

SalesFlow provides backend APIs for managing the core entities and workflows of a sales organization.

The application is organized into modules and follows the Controller–Service–Repository architecture. Spring Boot handles the application layer, Spring Data JPA and Hibernate manage persistence, and Oracle Database stores the business data.

### Problems Addressed

- Managing customer information through structured REST APIs.
- Tracking potential customers through a defined lead lifecycle.
- Searching and retrieving records efficiently using pagination and filtering.
- Converting qualified leads into customers while maintaining database consistency.
- Handling invalid requests, missing resources, and database conflicts through centralized exception handling.

## Key Features

Features are grouped by their implementation status. The implemented list should reflect the version currently available in the repository; planned features are not represented as completed functionality.

### 1. Customer Management

- Create, retrieve, update, and delete customer records.
- Validate incoming request payloads.
- Enforce unique customer email addresses.
- Search and filter customers.
- Support pagination for collection endpoints.
- Track creation and update timestamps.
- Return structured customer response DTOs.

### 2. Lead Management

- Create, retrieve, update, and delete sales leads.
- Maintain lead information, including name, email, company, phone, source, and status.
- Validate required fields and request data.
- Support paginated lead retrieval.
- Represent lead sources and statuses using Java enums.
- Track creation and update timestamps.

### 3. Lead Lifecycle Management

SalesFlow models a lead lifecycle using controlled status transitions.

| Status | Description |
|---|---|
| `NEW` | Lead has been created. |
| `CONTACTED` | Initial contact has been made. |
| `QUALIFIED` | Lead meets the qualification criteria. |
| `CONVERTED` | Lead has been converted into a customer. |
| `LOST` | Lead is no longer an active opportunity. |

The intended transitions are:

- `NEW` → `CONTACTED` or `LOST`
- `CONTACTED` → `QUALIFIED` or `LOST`
- `QUALIFIED` → `CONVERTED` or `LOST`
- `CONVERTED` and `LOST` are terminal states.

The conversion workflow should be the authoritative way to transition a qualified lead to `CONVERTED`, ensuring that conversion is accompanied by customer creation.

### 4. Lead-to-Customer Conversion

- Convert qualified leads into customer records.
- Validate lead eligibility before conversion.
- Prevent conversion when the customer's email already exists.
- Map lead details to the new customer.
- Update the lead's status after successful customer creation.
- Use transaction management to keep the related database changes consistent.

### 5. Validation and Exception Handling

- Jakarta Bean Validation for request validation.
- Centralized exception handling using `@ControllerAdvice`.
- Consistent HTTP status codes for common API failures.
- Dedicated exceptions for missing resources, duplicate resources, and invalid status transitions.
- Database integrity violation handling.

### 6. API Documentation

- OpenAPI documentation through Springdoc.
- Swagger UI for exploring and executing REST endpoints.
- Request and response schemas for API operations.
- Pagination parameters exposed for supported collection endpoints.

---

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming language |
| Spring Boot 3 | Application framework |
| Spring Web | REST API development |
| Spring Data JPA | Repository and persistence abstraction |
| Hibernate | Object-relational mapping |
| Oracle Database | Relational data persistence |
| Jakarta Bean Validation | Request validation |
| Maven | Dependency management and build automation |
| Springdoc OpenAPI | API specification and documentation |
| Swagger UI | Interactive API exploration |
| Git and GitHub | Version control and collaboration |
| IntelliJ IDEA | Development environment |
| DBeaver | Database development and inspection |

### Planned Technology Additions

The following technologies are intended for future implementation:

| Technology | Planned purpose |
|---|---|
| Spring Security | Authentication and endpoint security |
| BCrypt | Password hashing |
| JWT | Token-based authentication |
| JUnit 5 | Automated unit testing |
| Mockito | Mocking dependencies in unit tests |
| Spring Boot Test / MockMvc | Application and controller testing |
| Apache Kafka | Asynchronous event-driven processing |
| Docker | Reproducible application environments |
| Docker Compose | Local orchestration of application dependencies |
| CI/CD | Automated build, test, and deployment workflows |

These technologies are part of the development roadmap and should not be interpreted as already integrated.

---

## Architecture

SalesFlow follows a layered backend architecture that separates HTTP handling, business logic, data access, and persistence.

```text
                  Client / Swagger UI
                          |
                          v
                 REST Controller Layer
                          |
                          v
                    Service Layer
                   Business Logic
                          |
                          v
                Spring Data JPA Layer
                          |
                          v
                  Hibernate / JDBC
                          |
                          v
                   Oracle Database
```



## Author

**Diya Banerjee**
