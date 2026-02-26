@echo off
echo ================================================
echo   University Management System - Restart Script
echo ================================================
echo.

echo [1/3] Stopping processes on port 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
    echo Stopping process %%a...
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 2 >nul

echo [2/3] Setting up environment...
set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo [3/3] Starting application...
echo.
echo ================================================
echo   Starting Spring Boot Application...
echo   URL: http://localhost:8080/
echo ================================================
echo.

cd /d "%~dp0"
call mvnw.cmd spring-boot:run

pause

