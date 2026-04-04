@echo off
chcp 65001 >nul
cls
color 0A
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                                                                ║
echo ║           🎓 UNIVERSITY MANAGEMENT SYSTEM - ЗАПУСК             ║
echo ║                                                                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 📋 ВЫБЕРИТЕ ДЕЙСТВИЕ:
echo.
echo    [1] 🚀 Запустить приложение (обычный запуск)
echo    [2] 👨‍🏫 Создать/пересоздать преподавателя (очистка БД)
echo    [3] 🔍 Проверить данные в базе
echo    [4] 📖 Показать учетные данные
echo    [5] ❌ Выход
echo.
echo ════════════════════════════════════════════════════════════════
echo.

set /p choice="Введите номер (1-5): "

if "%choice%"=="1" goto START
if "%choice%"=="2" goto RESET_DB
if "%choice%"=="3" goto CHECK_DB
if "%choice%"=="4" goto SHOW_CREDS
if "%choice%"=="5" goto END

echo.
echo ❌ Неверный выбор
timeout /t 2 /nobreak >nul
goto MENU

:START
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 🚀 ЗАПУСК ПРИЛОЖЕНИЯ
echo ════════════════════════════════════════════════════════════════
echo.
call start.bat
goto END

:RESET_DB
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 👨‍🏫 СОЗДАНИЕ ПРЕПОДАВАТЕЛЯ (ОЧИСТКА БД)
echo ════════════════════════════════════════════════════════════════
echo.
echo ⚠️  ВНИМАНИЕ: Это удалит ВСЕ данные из базы!
echo.
set /p confirm="Продолжить? (Y/N): "
if /i not "%confirm%"=="Y" goto MENU

call СОЗДАТЬ_ПРЕПОДАВАТЕЛЯ.bat
goto END

:CHECK_DB
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔍 ПРОВЕРКА ДАННЫХ В БАЗЕ
echo ════════════════════════════════════════════════════════════════
echo.
call test-auth.bat
goto END

:SHOW_CREDS
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔑 УЧЕТНЫЕ ДАННЫЕ ДЛЯ ВХОДА
echo ════════════════════════════════════════════════════════════════
echo.
echo 👔 АДМИНИСТРАТОР:
echo    Email:    admin@university.kz
echo    Пароль:   admin123
echo    Доступ:   /admin/dashboard
echo.
echo 👨‍🎓 СТУДЕНТ 1:
echo    Email:    asel@student.kz
echo    Пароль:   123456
echo    Доступ:   /student/dashboard
echo.
echo 👨‍🎓 СТУДЕНТ 2:
echo    Email:    erlan@student.kz
echo    Пароль:   123456
echo    Доступ:   /student/dashboard
echo.
echo 👨‍🏫 ПРЕПОДАВАТЕЛЬ:
echo    Email:    teacher@university.kz
echo    Пароль:   teacher123
echo    Доступ:   /professor/dashboard
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 🌐 URL входа: http://localhost:8080/auth/login
echo.
echo ════════════════════════════════════════════════════════════════
echo.
pause
goto MENU

:END
exit /b 0

:MENU
cls
goto START_MENU

:START_MENU
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║           🎓 UNIVERSITY MANAGEMENT SYSTEM - ЗАПУСК             ║
echo ╚════════════════════════════════════════════════════════════════╝
goto MENU_CHOICE

:MENU_CHOICE
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 📋 ВЫБЕРИТЕ ДЕЙСТВИЕ:
echo.
echo    [1] 🚀 Запустить приложение (обычный запуск)
echo    [2] 👨‍🏫 Создать/пересоздать преподавателя (очистка БД)
echo    [3] 🔍 Проверить данные в базе
echo    [4] 📖 Показать учетные данные
echo    [5] ❌ Выход
echo.
echo ════════════════════════════════════════════════════════════════
echo.
set /p choice="Введите номер (1-5): "
goto PROCESS_CHOICE

:PROCESS_CHOICE
if "%choice%"=="1" goto START
if "%choice%"=="2" goto RESET_DB
if "%choice%"=="3" goto CHECK_DB
if "%choice%"=="4" goto SHOW_CREDS
if "%choice%"=="5" exit /b 0
echo ❌ Неверный выбор
timeout /t 2 /nobreak >nul
goto MENU

