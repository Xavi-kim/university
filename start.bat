@echo off
cd C:\jakarta\university

echo ========================================
echo University Management System
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo Cleaning up old processes...
echo.

REM Останавливаем все Java процессы
taskkill /F /IM java.exe >nul 2>&1
taskkill /F /IM javaw.exe >nul 2>&1

REM Ждём 2 секунды
timeout /t 2 /nobreak >nul

REM Проверяем и освобождаем порт 8080
echo Checking port 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    echo Killing process using port 8080: %%a
    taskkill /F /PID %%a >nul 2>&1
)

REM Ещё раз ждём
timeout /t 2 /nobreak >nul

echo.
echo Port 8080 is now free!
echo.
echo ========================================
echo Starting application...
echo ========================================
echo.
echo Application URL: http://localhost:8080/
echo Login: http://localhost:8080/auth/login
echo Profile: http://localhost:8080/profile
echo Catalog: http://localhost:8080/catalog
echo.
echo Credentials:
echo   Admin: admin@university.kz / admin123
echo   Student: asel@student.kz / 123456
echo.

echo Starting browser in 10 seconds...
start "" cmd /c "timeout /t 10 /nobreak >nul & start http://localhost:8080/"

call mvnw.cmd spring-boot:run

