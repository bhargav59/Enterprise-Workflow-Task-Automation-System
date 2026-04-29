1. PRODUCT REQUIREMENT DOCUMENT (PRD)
   🧩 1.1 Product Overview

A web-based system that allows organizations to:

Manage projects & tasks
Define workflows
Automate business processes
Track productivity

👉 In real enterprises, such systems manage structured workflows and tasks across teams and departments

🎯 1.2 Objectives
Build a production-grade Java full stack system
Demonstrate:
Backend architecture (Spring Boot)
Secure APIs
Workflow logic engine
Deliver a system usable by real teams
👥 1.3 User Roles
Role Description
Admin Manage users, system configs
Manager Create projects, assign tasks
Employee Work on tasks
⚙️ 1.4 Core Features
🔐 Authentication & Authorization
JWT-based login/signup
Role-based access (RBAC)
📁 Project Management
Create / update / delete projects
Assign team members
✅ Task Management
Create tasks
Assign users
Status lifecycle:
TODO → IN_PROGRESS → DONE
📊 Kanban Board
Drag & drop tasks
Real-time updates (optional WebSocket)
🔄 Workflow Engine (⭐ CORE FEATURE)

Users define rules:

Example:

IF task.status = DONE → send notification
IF deadline passed → mark as OVERDUE
🔔 Notification System
In-app notifications
Email (optional)
📈 Dashboard & Analytics
Tasks completed
Productivity stats
Overdue tasks
🗄️ 1.5 Non-Functional Requirements
REST API design
Secure endpoints
Scalable architecture
Clean layered structure
Logging & error handling
🧱 1.6 System Architecture
Frontend (React / JSP)
↓
Spring Boot Backend
↓
Service Layer
↓
Repository Layer (JPA)
↓
Database (PostgreSQL)

Optional:

Redis (caching)
WebSocket (real-time)
🗃️ 1.7 Database Design (Core Tables)
USERS
id, name, email, password, role
PROJECTS
id, name, description, created_by
TASKS
id, title, description, status, deadline, project_id, assigned_to
WORKFLOWS
id, condition, action
NOTIFICATIONS
id, message, user_id, read_status
