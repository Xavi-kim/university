@echo off
echo ========================================
echo Opening University Management System
echo ========================================
echo.
echo Opening browser at http://localhost:8080/
echo.

timeout /t 2 /nobreak >nul
start http://localhost:8080/

echo.
echo Browser opened!
echo.
pause

