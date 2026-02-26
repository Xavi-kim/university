@echo off
cd C:\jakarta\university

echo ========================================
echo University Management System
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo Checking port 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    echo Killing process using port 8080: %%a
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 2 /nobreak >nul

echo Port 8080 is free!
echo.
echo ========================================
echo Starting application...
echo ========================================
echo.
echo Application URL: http://localhost:8080/
echo API Docs: http://localhost:8080/api-docs
echo Dashboard: http://localhost:8080/dashboard
echo.

call mvnw.cmd spring-boot:run

