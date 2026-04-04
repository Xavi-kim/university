@echo off
chcp 65001 >nul
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔧 ИСПРАВЛЕНИЕ ПАРОЛЯ ПРЕПОДАВАТЕЛЯ
echo ════════════════════════════════════════════════════════════════
echo.
echo Проблема: У пользователя teacher@university.kz неправильный хеш пароля
echo Решение:  Обновить на правильный BCrypt хеш
echo.
echo ════════════════════════════════════════════════════════════════
echo.

REM Ищем PostgreSQL
set PSQL_PATH=
if exist "C:\Program Files\PostgreSQL\17\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\17\bin
) else if exist "C:\Program Files\PostgreSQL\16\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\16\bin
) else if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\15\bin
) else if exist "C:\Program Files\PostgreSQL\14\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\14\bin
)

if "%PSQL_PATH%"=="" (
    echo ❌ PostgreSQL не найден автоматически
    echo.
    echo 📖 ИСПОЛЬЗУЙТЕ pgAdmin:
    echo.
    echo 1. Откройте pgAdmin 4
    echo 2. Query Tool на базе university_db
    echo 3. Выполните:
    echo.
    echo    UPDATE users
    echo    SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy'
    echo    WHERE email = 'teacher@university.kz';
    echo.
    echo 4. Нажмите F5
    echo.
    echo Или откройте файл: ИСПРАВИТЬ_ПАРОЛЬ_PROFESSOR.sql
    echo.
    pause
    exit /b 1
)

echo ✅ PostgreSQL найден: %PSQL_PATH%
echo.

echo [1/3] Текущее состояние пользователя...
echo.
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, email, LEFT(password, 20) as pass_preview, role, enabled FROM users WHERE email = 'teacher@university.kz';"

echo.
echo [2/3] Обновление пароля...
echo    Устанавливаем BCrypt хеш для пароля: teacher123
echo.

"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "UPDATE users SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy', role = 'PROFESSOR', enabled = true WHERE email = 'teacher@university.kz';"

if %errorLevel% equ 0 (
    echo    ✅ Пароль обновлен успешно!
) else (
    echo    ❌ Ошибка при обновлении
    pause
    exit /b 1
)

echo.
echo [3/3] Проверка результата...
echo.
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, email, LEFT(password, 30) as password_hash, role, enabled FROM users WHERE email = 'teacher@university.kz';"

echo.
echo ════════════════════════════════════════════════════════════════
echo ✅ ГОТОВО! Пароль исправлен
echo ════════════════════════════════════════════════════════════════
echo.
echo 🔐 Войдите в приложение:
echo    URL:      http://localhost:8080/auth/login
echo    Email:    teacher@university.kz
echo    Пароль:   teacher123
echo.
echo ✅ Вход должен работать сейчас!
echo.
echo ════════════════════════════════════════════════════════════════
echo.
pause

