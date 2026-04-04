@echo off
chcp 65001 >nul
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔧 ИСПРАВЛЕНИЕ СТРУКТУРЫ ТАБЛИЦЫ USERS
echo ════════════════════════════════════════════════════════════════
echo.
echo ПРОБЛЕМА:
echo    Столбец email_verified не существует в таблице users
echo.
echo РЕШЕНИЕ:
echo    Добавим недостающие столбцы в таблицу
echo.
echo ════════════════════════════════════════════════════════════════
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
    echo ❌ PostgreSQL не найден
    pause
    exit /b 1
)

echo ✅ PostgreSQL найден: %PSQL_PATH%
echo.

echo [1/5] Проверка текущей структуры таблицы users...
echo.
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "\d users"

echo.
echo [2/5] Добавление недостающих столбцов...
echo.

REM Добавляем email_verified
echo Добавление email_verified...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "ALTER TABLE users ADD COLUMN IF NOT EXISTS email_verified BOOLEAN NOT NULL DEFAULT false;"

REM Добавляем verification_token
echo Добавление verification_token...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_token VARCHAR(255);"

REM Добавляем avatar_url (на случай если его тоже нет)
echo Добавление avatar_url...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "ALTER TABLE users ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(255);"

REM Добавляем phone_number
echo Добавление phone_number...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "ALTER TABLE users ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);"

REM Добавляем bio
echo Добавление bio...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "ALTER TABLE users ADD COLUMN IF NOT EXISTS bio VARCHAR(500);"

REM Добавляем registration_date
echo Добавление registration_date...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "ALTER TABLE users ADD COLUMN IF NOT EXISTS registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;"

echo.
echo ✅ Столбцы добавлены!
echo.

echo [3/5] Проверка новой структуры таблицы...
echo.
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "\d users"

echo.
echo [4/5] Проверка данных пользователей...
echo.
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, name, email, role, enabled, email_verified FROM users;"

echo.
echo [5/5] Обновление значений по умолчанию...
echo.

REM Устанавливаем email_verified = false для всех существующих пользователей
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "UPDATE users SET email_verified = false WHERE email_verified IS NULL;"

REM Устанавливаем registration_date = текущая дата для пользователей без даты
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "UPDATE users SET registration_date = CURRENT_TIMESTAMP WHERE registration_date IS NULL;"

echo.
echo ════════════════════════════════════════════════════════════════
echo ✅ ГОТОВО! Структура таблицы исправлена
echo ════════════════════════════════════════════════════════════════
echo.
echo 🎯 СЛЕДУЮЩИЙ ШАГ:
echo.
echo 1. Запустите приложение:
echo    start.bat
echo.
echo 2. Попробуйте войти:
echo    Email:    admin@university.kz
echo    Пароль:   admin123
echo.
echo 3. Смотрите логи - теперь должно работать! ✅
echo.
echo ════════════════════════════════════════════════════════════════
echo.
pause

