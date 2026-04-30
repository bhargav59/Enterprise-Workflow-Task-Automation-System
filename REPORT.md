# Project Report: Enterprise Workflow & Task Automation System

## 1. Abstract
The Enterprise Workflow & Task Automation System is a comprehensive full-stack application designed to streamline project management and task allocation within an organization. It provides a robust platform for team collaboration, featuring role-based access control, real-time notifications, and a rule-based workflow engine.

## 2. Introduction
In modern enterprise environments, managing complex projects and ensuring task completion is critical. This project addresses these needs by providing a centralized system to track project lifecycles, assign tasks to specific employees, and automate notifications based on task status changes.

## 3. Technology Stack
### 3.1 Backend
- **Language:** Java 17
- **Framework:** Spring Boot 3.2
- **Security:** Spring Security with JSON Web Tokens (JWT)
- **Data Access:** Spring Data JPA / Hibernate
- **Build Tool:** Maven

### 3.2 Frontend
- **Languages:** HTML5, CSS3, JavaScript (ES6+)
- **Architecture:** Single Page Application (SPA) principles with Vanilla JS
- **API Communication:** Fetch API with centralized `api.js` handler

### 3.3 Database
- **Development:** H2 In-memory Database
- **Production:** PostgreSQL

## 4. System Architecture
The system follows a standard Three-Tier Architecture:
1.  **Presentation Layer:** Web-based interface built with HTML/CSS/JS.
2.  **Logic Layer:** Spring Boot REST controllers and services.
3.  **Data Layer:** Relational database managed through JPA/Hibernate.

### 4.1 Security Implementation
Authentication is handled via JWT. Upon login, the server generates a token which the client stores in `localStorage` and includes in the `Authorization: Bearer <token>` header for all subsequent requests.

## 5. Core Modules
- **Authentication Module:** Manages user registration and login with roles (ADMIN, MANAGER, EMPLOYEE).
- **Project Module:** Allows managers to create projects and define team structures.
- **Task Module:** Supports the complete task lifecycle (TODO, IN_PROGRESS, DONE).
- **Workflow Engine:** A rule-based service that triggers actions (like notifications) based on state changes.
- **Notification Module:** Real-time feedback for users when tasks are assigned or updated.
- **Dashboard:** Provides analytical insights and statistics for project health.

## 6. Database Schema (Entities)
- **User:** Stores credentials, roles, and profile information.
- **Project:** Contains project metadata and associations with team members.
- **Task:** Tracks individual units of work, status, deadlines, and assignees.
- **Workflow:** Stores user-defined rules for automation.
- **Notification:** Records messages sent to specific users.

## 7. Implementation Highlights
- **RESTful API:** Clean separation between frontend and backend.
- **Role-Based Access Control (RBAC):** Ensures data security by limiting actions based on user roles.
- **Validation:** Server-side input validation using Spring Boot Starter Validation.

## 8. Conclusion
The Enterprise Workflow & Task Automation System provides a scalable and secure solution for organizational task management. By leveraging modern Java technologies and a responsive frontend, it offers a production-ready template for enterprise applications.

---
*Generated for: College Project Submission*
*Date: April 30, 2026*
