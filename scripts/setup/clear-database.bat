@echo off
chcp 65001 >nul
cls
echo ════════════════════════════════════════════════════════════════
echo   🗑️  ОЧИСТКА БАЗЫ ДАННЫХ
echo ════════════════════════════════════════════════════════════════
echo.
echo ⚠️  Это удалит ВСЕ данные из базы university_db
echo.
pause

PowerShell -NoProfile -ExecutionPolicy Bypass -File "%~dp0clear-and-restart.ps1"

pause

