@echo off
setlocal enabledelayedexpansion

cd C:\jakarta\university

echo ========================================
echo University Management System
echo ========================================
echo.

REM Установка JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo [1/6] Cleaning up old processes...
echo.

REM Останавливаем все Java процессы
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM javaw.exe >nul 2>&1

echo     - Java processes stopped
timeout /t 2 /nobreak >nul

REM Проверяем и освобождаем порт 8080
echo [2/6] Checking port 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    echo     - Killing process using port 8080: %%a
    taskkill /F /PID %%a >nul 2>&1
)

timeout /t 2 /nobreak >nul
echo     - Port 8080 is now free!
echo.

echo [3/6] Checking PostgreSQL connection...
echo.

REM Проверка подключения к PostgreSQL
psql -U postgres -d university_db -c "SELECT 1;" >nul 2>&1
if %errorlevel% neq 0 (
    echo     ❌ ERROR: Cannot connect to PostgreSQL!
    echo.
    echo     Please check:
    echo       1. PostgreSQL is running
    echo       2. Database 'university_db' exists
    echo       3. Password in application.yml is correct
    echo.
    echo     To create database run:
    echo       psql -U postgres
    echo       CREATE DATABASE university_db;
    echo.
    pause
    exit /b 1
)

echo     ✅ PostgreSQL connection OK
echo.

echo [4/6] Checking existing users...
echo.

REM Проверка существующих пользователей
for /f "tokens=*" %%a in ('psql -U postgres -d university_db -t -c "SELECT COUNT(*) FROM users;" 2^>nul') do set USER_COUNT=%%a
set USER_COUNT=%USER_COUNT: =%

if "%USER_COUNT%"=="0" (
    echo     ⚠️  WARNING: No users found in database
    echo     DataInitializer will create test accounts on startup
) else (
    echo     ✅ Found %USER_COUNT% user(s) in database
)

echo.
echo [5/6] Starting application...
echo ========================================
echo.
echo Application URL: http://localhost:8080/
echo Login page:      http://localhost:8080/auth/login
echo Profile:         http://localhost:8080/profile
echo Catalog:         http://localhost:8080/catalog
echo Admin panel:     http://localhost:8080/admin/dashboard
echo.
echo Credentials:
echo   👔 Admin:   admin@university.kz / admin123
echo   👨‍🎓 Student: asel@student.kz / 123456
echo.
echo [6/6] Launching...
echo.
echo ⏳ Please wait ~30 seconds for application to start...
echo 📋 Watch console for startup messages
echo 🔍 Look for: "Started Application"
echo.

REM Открываем браузер через 30 секунд
start "" cmd /c "timeout /t 30 /nobreak >nul & start http://localhost:8080/"

REM Запускаем приложение с логированием
call mvnw.cmd spring-boot:run

echo.
echo Application stopped.
pause

