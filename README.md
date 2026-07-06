# Hospital Equipment Tracking System

> Enterprise-style backend application built with Spring Boot for managing hospital equipment lifecycle, movement, maintenance, and audit history.

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-orange)
![JWT](https://img.shields.io/badge/Security-JWT-red)
![JUnit5](https://img.shields.io/badge/Test-JUnit5-success)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## Highlights

✔ JWT Authentication & Role-Based Authorization

✔ Equipment Lifecycle State Machine

✔ Maintenance Workflow

✔ Equipment Movement Tracking

✔ Staff Assignment

✔ Complete Audit History

✔ RESTful API

✔ Spring Security

✔ JPA / Hibernate

✔ Unit Testing

✔ MySQL & H2 Profiles

## System Architecture
<p align="center">
  <img src="docs/Architecture.png" width="450">
</p>

## Hospital Workflow & Architecture
![Hospital Workflow & Architecture](docs/LifeSafety.png)

## ER Diagram

## Project Structure
```text
src
├── config                               # Spring Security & JWT configuration
│   ├── SecurityConfig                   # REST API endpoints
├── controller
│   ├── AuthController
│   ├── EquipmentController
│   └── MaintenanceRecordController
├── dto                                  # Request/Response objects
│   ├── request
│   └── response
├── exception                            # Global exception handling
├── model                                # JPA entities
├── repository                           # Spring Data JPA repositories
├── security                             # JWT filters & authentication
├── service                              # Business logic
├── validation                           # Business rule validation
└── spec                                 # Dynamic query specifications
```
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


![swagger-API-overview](docs/swagger-API-overview.PNG)
![swagger-jwt-login](docs/swagger-jwt-login.png)


