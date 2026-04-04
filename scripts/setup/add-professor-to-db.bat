@echo off
chcp 65001 >nul
echo.
echo ════════════════════════════════════════════════════════════════
echo 👨‍🏫 ДОБАВЛЕНИЕ ПРЕПОДАВАТЕЛЯ В POSTGRESQL
echo ════════════════════════════════════════════════════════════════
echo.

REM Ищем PostgreSQL
set PSQL_PATH=
if exist "C:\Program Files\PostgreSQL\16\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\16\bin
) else if exist "C:\Program Files\PostgreSQL\17\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\17\bin
) else if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\15\bin
) else if exist "C:\Program Files\PostgreSQL\14\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\14\bin
) else (
    echo ❌ PostgreSQL не найден в стандартных путях
    echo.
    echo Используйте pgAdmin для выполнения SQL:
    echo 1. Откройте pgAdmin 4
    echo 2. Подключитесь к university_db
    echo 3. Query Tool → Откройте файл add-professor.sql
    echo 4. Execute (F5)
    echo.
    pause
    exit /b 1
)

echo ✅ PostgreSQL найден: %PSQL_PATH%
echo.

echo [1/3] Проверка текущих пользователей...
echo.
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, name, email, role, enabled FROM users;"

echo.
echo [2/3] Добавление преподавателя...
echo.
echo    Email:    teacher@university.kz
echo    Пароль:   teacher123
echo    Роль:     PROFESSOR
echo.

REM Проверяем, существует ли уже преподаватель
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -t -c "SELECT COUNT(*) FROM users WHERE email = 'teacher@university.kz';" > temp_count.txt
set /p EXISTING_COUNT=<temp_count.txt
del temp_count.txt 2>nul
set EXISTING_COUNT=%EXISTING_COUNT: =%

if "%EXISTING_COUNT%"=="1" (
    echo ⚠️  Преподаватель уже существует!
    echo    Обновляю пароль...
    "%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "UPDATE users SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy', role = 'PROFESSOR', enabled = true WHERE email = 'teacher@university.kz';"
    if %errorLevel% equ 0 (
        echo    ✅ Пароль обновлен!
    )
) else (
    echo    Создание нового пользователя...
    "%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "INSERT INTO users (name, email, password, role, enabled, registration_date, email_verified) VALUES ('Айгуль Нурбекова', 'teacher@university.kz', '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy', 'PROFESSOR', true, CURRENT_TIMESTAMP, false);"
    if %errorLevel% equ 0 (
        echo    ✅ Преподаватель создан!
    ) else (
        echo    ❌ Ошибка при создании
        pause
        exit /b 1
    )
)

echo.
echo [3/3] Проверка результата...
echo.
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, name, email, role, enabled FROM users WHERE email = 'teacher@university.kz';"

echo.
echo ════════════════════════════════════════════════════════════════
echo ✅ ГОТОВО! Преподаватель добавлен в базу данных
echo ════════════════════════════════════════════════════════════════
echo.
echo 🔑 Учетные данные для входа:
echo    Email:    teacher@university.kz
echo    Пароль:   teacher123
echo.
echo 🌐 Войдите:
echo    http://localhost:8080/auth/login
echo.
echo 📊 Все пользователи в базе:
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, name, email, role FROM users ORDER BY id;"

echo.
echo ════════════════════════════════════════════════════════════════
echo.
pause

