@echo off
echo ========================================
echo ПРОВЕРКА ПОДКЛЮЧЕНИЯ К POSTGRESQL
echo ========================================
echo.

cd C:\jakarta\university

echo Тестируем подключение к базе данных...
echo.

REM Проверяем PostgreSQL
psql -U postgres -d university_db -c "SELECT version();" 2>nul
if %errorlevel% neq 0 (
    echo ОШИБКА: Не удается подключиться к PostgreSQL
    echo.
    echo Проверьте:
    echo 1. Запущен ли PostgreSQL
    echo 2. Существует ли база данных university_db
    echo 3. Правильный ли пароль в application.yml
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo ПРОВЕРКА ПОЛЬЗОВАТЕЛЕЙ
echo ========================================
echo.

echo Проверяем созданных пользователей...
psql -U postgres -d university_db -c "SELECT id, name, email, role, enabled FROM users;"

echo.
echo ========================================
echo ПРОВЕРКА ХЕША ПАРОЛЯ АДМИНА
echo ========================================
echo.

psql -U postgres -d university_db -c "SELECT email, LEFT(password, 20) || '...' as password_hash FROM users WHERE email = 'admin@university.kz';"

echo.
pause

