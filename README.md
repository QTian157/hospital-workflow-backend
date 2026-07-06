# Hospital Equipment Tracking System

> Enterprise-style backend application built with Spring Boot for managing hospital equipment lifecycle, movement, maintenance, and audit history.

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-orange)
![JWT](https://img.shields.io/badge/Security-JWT-red)
![JUnit5](https://img.shields.io/badge/Test-JUnit5-success)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## Project Overview

Hospital equipment constantly moves between departments, rooms, and staff throughout its lifecycle. Tracking equipment manually often leads to misplaced assets, incomplete maintenance records, and missing sterilization history.

This project provides a secure backend system for managing hospital equipment through its entire lifecycle, including movement tracking, staff assignment, maintenance management, and complete audit history.

This project was designed to simulate a real-world hospital equipment management system rather than a simple CRUD application, with a focus on business workflows, auditability, and maintainable backend architecture.
## Highlights

✔ Equipment Lifecycle State Machine

✔ Complete Audit Trail

✔ Business Rule Validation

✔ Equipment Movement Tracking

✔ Staff Assignment Workflow

✔ Maintenance Workflow

✔ JWT Authentication

✔ RESTful APIs

## Technology Stack

Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- JWT

Database

- MySQL
- H2

Testing

- JUnit5
- Mockito

Tools

- Maven
- Swagger
- Postman

## System Architecture
<p align="center">
  <img src="docs/Architecture.png" width="450">
</p>

## Hospital Workflow & Architecture
![Hospital Workflow & Architecture](docs/LifeSafety.png)

## Core Domain Model
![Hospital Workflow & Architecture](docs/Core_Domain_model.png)


## Project Structure
```text
src
├── config                            
│   ├── SecurityConfig                  
├── controller
│   ├── AuthController
│   ├── EquipmentController
│   └── MaintenanceRecordController
├── dto                                 
│   ├── request
│   └── response
├── exception                            
├── model                               
├── repository                           
├── security                           
├── service                             
├── validation                         
└── spec                                 
```
| Package    | Description                                  |
| ---------- |----------------------------------------------|
| config     | Spring Security & JWT configuration          |
| controller | REST API endpoints                           |
| dto        | Request/Response objects                     |
| exception  | Global exception handling                    |
| model      | JPA entities                                 |
| repository | Spring Data JPA repositories                 |
| security   | Authentication, JWT filter and authorization |
| service    | Business logic                               |
| validation | Business rule validation                     |
| spec       | Dynamic query specifications                 |

## API
| Method | Endpoint                        | Description           |
| ------ | ------------------------------- | --------------------- |
| POST   | `/api/auth/login`               | User login            |
| POST   | `/api/auth/register`            | Register user         |
| GET    | `/api/equipment`                | Get all equipment     |
| POST   | `/api/equipment`                | Create equipment      |
| POST   | `/api/equipment/{id}/move`      | Move equipment        |
| POST   | `/api/equipment/{id}/assign`    | Assign equipment      |
| POST   | `/api/equipment/{id}/start-use` | Start equipment usage |

Additional endpoints are available for:

• Status History
• Movement History
• Assignment History
• Maintenance Records


![swagger-API-overview](docs/swagger-API-overview.PNG)
![swagger-jwt-login](docs/swagger-jwt-login.png)

## Running the Project

Clone

```bash
git clone ...
```

Run

```bash
mvn spring-boot:run
```

Swagger

```
http://localhost:8080/swagger-ui/index.html
```

## Design Decisions

***Why separate history tables?***

Historical records are stored independently from the Equipment entity to avoid loading unnecessary historical data while preserving a complete audit trail.

***Why use a State Machine?***

Equipment status transitions follow strict business rules. Centralizing transition logic prevents invalid state changes and simplifies future maintenance.

***Why separate Maintenance Records?***

Maintenance is modeled as an independent business process rather than an equipment status, allowing detailed tracking of maintenance lifecycle and outcomes.

***Why DTOs?***

DTOs separate API contracts from persistence models, reducing coupling and improving security.

***Why use DTOs?***

DTOs isolate API contracts from persistence models, preventing entity exposure and allowing the API to evolve independently of the database schema.

***State Machine***

The state machine centralizes all equipment status transitions, ensuring only valid lifecycle changes are allowed while keeping business rules out of controllers.


## Future Improvements

Potential future enhancements:

- Barcode / QR code scanning for faster equipment identification.
- Email notifications for maintenance reminders and status updates.
- Docker support for simplified deployment.
- CI/CD pipeline with GitHub Actions.
- Redis caching to improve system performance.