# Cross-Platform Setup

## Prerequisites

### Windows
- Java 17+
- Maven 3.8+
- PostgreSQL (optional, for production)

### Linux/Mac
- Java 17+
- Maven 3.8+
- PostgreSQL (optional, for production)

## Quick Start

### Windows
Double-click `start.bat` or run from cmd:
```cmd
start.bat
```

### Linux/Mac
```bash
./start.sh
```

## Database Setup

### Using PostgreSQL (Production)
1. Install PostgreSQL
2. Create database:
   ```sql
   CREATE DATABASE enterprise_workflow;
   ```
3. Update credentials in environment or create `application-prod.properties`

### Using H2 (Development/Testing)
No database setup needed - runs in-memory.

```bash
# Linux/Mac
export SPRING_PROFILES_ACTIVE=dev
./start.sh

# Windows
set SPRING_PROFILES_ACTIVE=dev
start.bat
```

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| DB_URL | jdbc:postgresql://localhost:5432/enterprise_workflow | Database URL |
| DB_USER | postgres | Database username |
| DB_PASSWORD | | Database password |
| SERVER_PORT | 8080 | Server port |
| JWT_SECRET | ... | JWT secret key |
| H2_ENABLED | true | Enable H2 console |

## Access

- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

## Troubleshooting

### Port already in use
```bash
# Linux
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /F /PID <PID>
```