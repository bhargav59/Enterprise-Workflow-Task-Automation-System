# Enterprise Workflow & Task Automation System

A production-grade Java full stack system for managing projects, tasks, and workflows in organizations.

## Features

- **Authentication & Authorization**: JWT-based login/signup with role-based access control (Admin, Manager, Employee)
- **Project Management**: Create, update, delete projects and assign team members
- **Task Management**: Create tasks with status lifecycle (TODO → IN_PROGRESS → DONE)
- **Kanban Board**: Drag-and-drop task management
- **Workflow Engine**: Define custom rules (e.g., IF status=DONE THEN notify)
- **Notifications**: In-app notifications
- **Dashboard**: Analytics with task statistics

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2, JPA/Hibernate
- **Database**: H2 (dev) / PostgreSQL (prod)
- **Frontend**: HTML, CSS, vanilla JavaScript
- **Security**: Spring Security with JWT

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on: `http://localhost:8080`

H2 Console: `http://localhost:8080/h2-console`

### Run Frontend

Open `frontend/index.html` in browser.

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/register` | POST | Register new user |
| `/api/auth/login` | POST | Login |
| `/api/projects` | GET, POST | List/Create projects |
| `/api/tasks` | GET, POST | List/Create tasks |
| `/api/tasks/{id}/status` | PATCH | Update task status |
| `/api/workflows` | GET, POST | List/Create workflows |
| `/api/dashboard` | GET | Get statistics |

## User Roles

- **ADMIN**: Manage users, system configs
- **MANAGER**: Create projects, assign tasks
- **Employee**: Work on tasks