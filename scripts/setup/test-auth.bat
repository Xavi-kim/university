@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 🔐 ТЕСТИРОВАНИЕ АУТЕНТИФИКАЦИИ
echo ========================================
echo.

echo [1] Проверка данных в базе university_db
echo.

REM Ищем PostgreSQL
set PSQL_PATH=
if exist "C:\Program Files\PostgreSQL\16\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\16\bin
) else if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\15\bin
) else if exist "C:\Program Files\PostgreSQL\14\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\14\bin
) else (
    echo ❌ PostgreSQL не найден в стандартных путях
    echo.
    echo Попробуйте вручную:
    echo psql -U postgres -d university_db
    echo SELECT * FROM users WHERE email = 'admin@university.kz';
    echo.
    pause
    exit /b 1
)

echo ✅ PostgreSQL найден: %PSQL_PATH%
echo.
echo ----------------------------------------
echo 📊 Пользователи в базе данных:
echo ----------------------------------------
echo.

"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, name, email, role, enabled FROM users;"

echo.
echo ----------------------------------------
echo 🔍 Детали администратора:
echo ----------------------------------------
echo.

"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, name, email, role, enabled, registration_date FROM users WHERE email = 'admin@university.kz';"

echo.
echo ----------------------------------------
echo 🔐 Проверка хеша пароля:
echo ----------------------------------------
echo.

"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT email, LEFT(password, 30) || '...' as password_hash FROM users WHERE email = 'admin@university.kz';"

echo.
echo ----------------------------------------
echo ✅ Если вы видите пользователя admin@university.kz
echo    с хешем пароля, начинающимся с $2a$10$
echo    значит данные в порядке!
echo ----------------------------------------
echo.
echo 🔑 Попробуйте войти:
echo    URL: http://localhost:8080/auth/login
echo    Email: admin@university.kz
echo    Пароль: admin123
echo.
pause

