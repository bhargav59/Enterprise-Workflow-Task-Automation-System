@echo off
echo Starting Enterprise Workflow & Task Automation System...

:: Kill existing processes
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (taskkill /F /PID %%a >nul 2>&1)
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000 ^| findstr LISTENING') do (taskkill /F /PID %%a >nul 2>&1)
timeout /t 2 /nobreak >nul

:: Start backend
echo Starting backend on port 8080...
cd /d "%~dp0backend"
start /b mvn spring-boot:run > backend.log 2>&1

:: Wait for backend to start
timeout /t 15 /nobreak >nul

:: Start frontend
echo Starting frontend on port 3000...
cd /d "%~dp0frontend"
start /b python -m http.server 3000 > frontend.log 2>&1

echo.
echo =========================================
echo System is running!
echo Frontend: http://localhost:3000
echo Backend:  http://localhost:8080
echo H2 Console: http://localhost:8080/h2-console
echo =========================================
echo.
echo Press Ctrl+C to stop
pause