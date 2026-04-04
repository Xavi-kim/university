@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 🔍 ДИАГНОСТИКА ПРОБЛЕМЫ ВХОДА
echo ========================================
echo.
echo Email:  admin@university.kz
echo Пароль: admin123
echo Ошибка: "Неверный email или пароль"
echo.
echo ========================================
echo.

set PSQL_PATH=C:\Program Files\PostgreSQL\16\bin

echo [ШАГ 1] Проверка установки PostgreSQL
echo ----------------------------------------

if exist "%PSQL_PATH%\psql.exe" (
    echo ✅ PostgreSQL 16 установлен
    echo    Путь: %PSQL_PATH%
) else if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" (
    echo ✅ PostgreSQL 15 установлен
    set PSQL_PATH=C:\Program Files\PostgreSQL\15\bin
) else if exist "C:\Program Files\PostgreSQL\14\bin\psql.exe" (
    echo ✅ PostgreSQL 14 установлен
    set PSQL_PATH=C:\Program Files\PostgreSQL\14\bin
) else (
    echo.
    echo ❌ ПРОБЛЕМА НАЙДЕНА: PostgreSQL НЕ УСТАНОВЛЕН
    echo.
    echo 📖 РЕШЕНИЕ:
    echo    1. Скачайте PostgreSQL 16:
    echo       https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
    echo.
    echo    2. Установите с паролем: 123261181
    echo.
    echo    3. Запустите: setup-postgresql.bat
    echo.
    echo    4. Затем: start.bat
    echo.
    echo 📄 Подробная инструкция: РЕШЕНИЕ_ПРОБЛЕМЫ_ВХОДА.md
    echo.
    pause
    exit /b 1
)

echo.
echo [ШАГ 2] Проверка службы PostgreSQL
echo ----------------------------------------

sc query postgresql-x64-16 >nul 2>&1
if %errorLevel% neq 0 (
    sc query postgresql-x64-15 >nul 2>&1
    if %errorLevel% neq 0 (
        echo ❌ Служба PostgreSQL не найдена
        goto PROBLEM_FOUND
    )
)

sc query postgresql-x64-16 | findstr "RUNNING" >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo ❌ ПРОБЛЕМА НАЙДЕНА: Служба PostgreSQL НЕ ЗАПУЩЕНА
    echo.
    echo 📖 РЕШЕНИЕ:
    echo    Запустите службу командой:
    echo    net start postgresql-x64-16
    echo.
    echo    Или через Services.msc:
    echo    Win+R → services.msc → postgresql-x64-16 → Start
    echo.

    set /p answer="   Запустить службу сейчас? (Y/N): "
    if /i "%answer%"=="Y" (
        echo.
        echo    Запуск службы...
        net start postgresql-x64-16
        timeout /t 3 /nobreak >nul
        echo    ✅ Служба запущена
        echo.
    ) else (
        pause
        exit /b 1
    )
) else (
    echo ✅ Служба PostgreSQL запущена
)

echo.
echo [ШАГ 3] Проверка базы данных university_db
echo ----------------------------------------

"%PSQL_PATH%\psql.exe" -U postgres -lqt 2>nul | findstr "university_db" >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo ❌ ПРОБЛЕМА НАЙДЕНА: База данных university_db НЕ СУЩЕСТВУЕТ
    echo.
    echo 📖 РЕШЕНИЕ:
    echo    Создайте базу данных командой:
    echo    psql -U postgres -c "CREATE DATABASE university_db;"
    echo.

    set /p answer="   Создать базу данных сейчас? (Y/N): "
    if /i "%answer%"=="Y" (
        echo.
        echo    Создание базы данных...
        echo    Введите пароль PostgreSQL (должен быть: 123261181):
        "%PSQL_PATH%\psql.exe" -U postgres -c "CREATE DATABASE university_db;"
        if %errorLevel% equ 0 (
            echo    ✅ База данных создана
            echo.
        ) else (
            echo    ❌ Ошибка при создании
            pause
            exit /b 1
        )
    ) else (
        pause
        exit /b 1
    )
) else (
    echo ✅ База данных university_db существует
)

echo.
echo [ШАГ 4] Проверка таблиц в базе данных
echo ----------------------------------------

"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "\dt" 2>nul | findstr "users" >nul 2>&1
if %errorLevel% neq 0 (
    echo ⚠️  Таблицы не созданы
    echo    Это нормально для первого запуска
    echo    Hibernate создаст их автоматически
    goto STEP5
)

echo ✅ Таблицы существуют

echo.
echo [ШАГ 5] Проверка пользователей в базе данных
echo ----------------------------------------

for /f "tokens=*" %%a in ('"%PSQL_PATH%\psql.exe" -U postgres -d university_db -t -c "SELECT COUNT(*) FROM users;" 2^>nul') do set USER_COUNT=%%a
set USER_COUNT=%USER_COUNT: =%

if "%USER_COUNT%"=="0" (
    echo ⚠️  Пользователи не созданы
    echo    DataInitializer создаст их при первом запуске
    goto STEP5
) else (
    echo ✅ Найдено пользователей: %USER_COUNT%
)

echo.
echo    Список пользователей:
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, name, email, role, enabled FROM users;"

echo.
echo [ШАГ 6] Проверка администратора
echo ----------------------------------------

"%PSQL_PATH%\psql.exe" -U postgres -d university_db -t -c "SELECT email FROM users WHERE email = 'admin@university.kz';" 2>nul | findstr "admin@university.kz" >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo ❌ ПРОБЛЕМА НАЙДЕНА: Пользователь admin@university.kz НЕ СУЩЕСТВУЕТ
    echo.
    echo 📖 РЕШЕНИЕ:
    echo    1. Остановите приложение (Ctrl+C если запущено)
    echo    2. Удалите данные: DROP TABLE users CASCADE;
    echo    3. Запустите: start.bat
    echo    4. DataInitializer создаст всех пользователей
    echo.
    goto PROBLEM_FOUND
)

echo ✅ Администратор найден в базе данных

echo.
echo    Детали пользователя:
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "SELECT id, name, email, role, enabled FROM users WHERE email = 'admin@university.kz';"

echo.
echo [ШАГ 7] Проверка хеша пароля
echo ----------------------------------------

for /f "tokens=*" %%a in ('"%PSQL_PATH%\psql.exe" -U postgres -d university_db -t -c "SELECT LEFT(password, 7) FROM users WHERE email = 'admin@university.kz';" 2^>nul') do set HASH_PREFIX=%%a
set HASH_PREFIX=%HASH_PREFIX: =%

if "%HASH_PREFIX%"=="$2a$10$" (
    echo ✅ Пароль зашифрован BCrypt
    echo    Начало хеша: %HASH_PREFIX%...
) else if "%HASH_PREFIX%"=="" (
    echo ❌ Не удалось получить хеш пароля
) else (
    echo ⚠️  Неожиданный формат хеша: %HASH_PREFIX%
)

:STEP5
echo.
echo [ШАГ 8] Проверка application.yml
echo ----------------------------------------

if exist "src\main\resources\application.yml" (
    echo ✅ Файл application.yml существует
    echo.
    echo    Настройки подключения:
    findstr /C:"url:" src\main\resources\application.yml | findstr "jdbc"
    findstr /C:"username:" src\main\resources\application.yml
    findstr /C:"password:" src\main\resources\application.yml

    findstr /C:"password: 123261181" src\main\resources\application.yml >nul 2>&1
    if %errorLevel% equ 0 (
        echo.
        echo    ✅ Пароль: 123261181 (совпадает с рекомендуемым)
    ) else (
        echo.
        echo    ⚠️  ВНИМАНИЕ: Пароль отличается от 123261181
        echo       Убедитесь что он совпадает с паролем PostgreSQL
    )
) else (
    echo ❌ Файл application.yml не найден
)

echo.
echo [ШАГ 9] Проверка Spring Security конфигурации
echo ----------------------------------------

if exist "src\main\java\org\example\university\config\SecurityConfig.java" (
    echo ✅ SecurityConfig.java существует

    findstr /C:"usernameParameter" src\main\java\org\example\university\config\SecurityConfig.java >nul 2>&1
    if %errorLevel% equ 0 (
        echo    ✅ Параметр usernameParameter настроен
    ) else (
        echo    ⚠️  Параметр usernameParameter не найден
    )
) else (
    echo ❌ SecurityConfig.java не найден
)

echo.
echo [ШАГ 10] Проверка CustomUserDetailsService
echo ----------------------------------------

if exist "src\main\java\org\example\university\service\CustomUserDetailsService.java" (
    echo ✅ CustomUserDetailsService.java существует
) else (
    echo ❌ CustomUserDetailsService.java не найден
)

echo.
echo ========================================
echo 📊 РЕЗУЛЬТАТЫ ДИАГНОСТИКИ
echo ========================================
echo.

if "%USER_COUNT%"=="" (
    echo 🔴 ОСНОВНАЯ ПРОБЛЕМА:
    echo    Приложение еще не запускалось или таблицы не созданы
    echo.
    echo 📖 РЕШЕНИЕ:
    echo    1. Запустите приложение: start.bat
    echo    2. Дождитесь сообщения "Started Application"
    echo    3. Проверьте что DataInitializer создал пользователей
    echo    4. Попробуйте войти: http://localhost:8080/auth/login
    echo.
) else if "%USER_COUNT%"=="0" (
    echo 🔴 ОСНОВНАЯ ПРОБЛЕМА:
    echo    Таблицы созданы, но пользователи не добавлены
    echo.
    echo 📖 РЕШЕНИЕ:
    echo    1. Остановите приложение
    echo    2. Удалите таблицы или всю БД
    echo    3. Перезапустите: start.bat
    echo    4. DataInitializer создаст тестовых пользователей
    echo.
) else (
    echo ✅ ВСЕ ПРОВЕРКИ ПРОЙДЕНЫ!
    echo.
    echo 🎯 СЛЕДУЮЩИЙ ШАГ:
    echo    1. Запустите приложение: start.bat
    echo    2. Откройте: http://localhost:8080/auth/login
    echo    3. Войдите с учетными данными:
    echo.
    echo       Email:    admin@university.kz
    echo       Пароль:   admin123
    echo.
    echo    Если все еще не работает, проверьте логи приложения
    echo    на наличие ошибок аутентификации.
    echo.
)

echo ========================================
echo.
pause

