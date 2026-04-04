@echo off
chcp 65001 >nul
echo ════════════════════════════════════════════════════════════════
echo   СОЗДАНИЕ ПОЛЬЗОВАТЕЛЕЙ ДЛЯ ПРОФЕССОРОВ
echo ════════════════════════════════════════════════════════════════
echo.

REM Настройки подключения к PostgreSQL
set PGHOST=localhost
set PGPORT=5432
set PGDATABASE=university_db
set PGUSER=postgres
set PGPASSWORD=1234

echo 📊 Проверяем профессоров в базе данных...
echo.

REM Выполняем SQL скрипт
psql -h %PGHOST% -p %PGPORT% -U %PGUSER% -d %PGDATABASE% -f "%~dp0..\sql\CREATE_USERS_FOR_PROFESSORS.sql"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ════════════════════════════════════════════════════════════════
    echo ✅ УСПЕШНО! Пользователи для профессоров созданы
    echo ════════════════════════════════════════════════════════════════
    echo.
    echo 📝 Данные для входа:
    echo    Email:  email профессора из базы данных
    echo    Пароль: professor123
    echo.
) else (
    echo.
    echo ════════════════════════════════════════════════════════════════
    echo ❌ ОШИБКА при создании пользователей
    echo ════════════════════════════════════════════════════════════════
    echo.
    echo Проверьте:
    echo   1. Запущен ли PostgreSQL
    echo   2. Правильные ли настройки подключения в этом файле
    echo   3. Существует ли база данных university_db
    echo.
)

pause

