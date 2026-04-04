@echo off
chcp 65001 >nul
echo.
echo ========================================
echo 🔧 НАСТРОЙКА POSTGRESQL
echo ========================================
echo.

REM Проверяем права администратора
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo ❌ ОШИБКА: Требуются права администратора
    echo.
    echo Правый клик на файл → "Запуск от имени администратора"
    echo.
    pause
    exit /b 1
)

echo [1/5] Проверка установки PostgreSQL...
echo.

REM Проверяем существование PostgreSQL
if exist "C:\Program Files\PostgreSQL\16\bin\psql.exe" (
    echo ✅ PostgreSQL 16 установлен
    echo    Путь: C:\Program Files\PostgreSQL\16
    goto CHECK_SERVICE
) else if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" (
    echo ✅ PostgreSQL 15 установлен
    echo    Путь: C:\Program Files\PostgreSQL\15
    set PSQL_PATH=C:\Program Files\PostgreSQL\15\bin
    goto CHECK_SERVICE
) else if exist "C:\Program Files\PostgreSQL\14\bin\psql.exe" (
    echo ✅ PostgreSQL 14 установлен
    echo    Путь: C:\Program Files\PostgreSQL\14
    set PSQL_PATH=C:\Program Files\PostgreSQL\14\bin
    goto CHECK_SERVICE
) else (
    echo ❌ PostgreSQL НЕ УСТАНОВЛЕН
    echo.
    echo 📥 Скачайте PostgreSQL 16:
    echo    https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
    echo.
    echo ⚙️  При установке используйте пароль: 123261181
    echo    (Этот пароль уже указан в application.yml)
    echo.
    echo 📖 Подробная инструкция в файле:
    echo    РЕШЕНИЕ_ПРОБЛЕМЫ_ВХОДА.md
    echo.
    pause
    exit /b 1
)

:CHECK_SERVICE
set PSQL_PATH=C:\Program Files\PostgreSQL\16\bin

echo.
echo [2/5] Проверка службы PostgreSQL...
echo.

REM Ищем службу PostgreSQL
sc query | findstr "postgresql" >nul 2>&1
if %errorLevel% neq 0 (
    echo ❌ Служба PostgreSQL не найдена
    echo    Переустановите PostgreSQL
    pause
    exit /b 1
)

REM Запускаем службу если не запущена
sc query postgresql-x64-16 | findstr "RUNNING" >nul 2>&1
if %errorLevel% neq 0 (
    echo ⏳ Запуск службы PostgreSQL...
    net start postgresql-x64-16 >nul 2>&1
    timeout /t 3 /nobreak >nul

    sc query postgresql-x64-16 | findstr "RUNNING" >nul 2>&1
    if %errorLevel% neq 0 (
        echo ❌ Не удалось запустить службу
        echo    Проверьте Services.msc → postgresql-x64-16
        pause
        exit /b 1
    )
)

echo ✅ Служба PostgreSQL запущена
echo.

echo [3/5] Проверка базы данных...
echo.

REM Проверяем существование базы данных
"%PSQL_PATH%\psql.exe" -U postgres -lqt 2>nul | findstr "university_db" >nul 2>&1
if %errorLevel% equ 0 (
    echo ✅ База данных university_db уже существует
    goto CHECK_TABLES
)

echo ⏳ Создание базы данных university_db...
"%PSQL_PATH%\psql.exe" -U postgres -c "CREATE DATABASE university_db;" 2>nul
if %errorLevel% equ 0 (
    echo ✅ База данных university_db создана
) else (
    echo ❌ Ошибка при создании базы данных
    echo    Возможно неверный пароль (должен быть: 123261181)
    echo.
    echo 💡 Создайте вручную:
    echo    psql -U postgres
    echo    CREATE DATABASE university_db;
    echo.
    pause
    exit /b 1
)

:CHECK_TABLES
echo.
echo [4/5] Проверка таблиц...
echo.

REM Проверяем таблицы
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "\dt" 2>nul | findstr "users" >nul 2>&1
if %errorLevel% equ 0 (
    echo ✅ Таблицы уже существуют

    REM Показываем количество пользователей
    for /f "tokens=*" %%a in ('"%PSQL_PATH%\psql.exe" -U postgres -d university_db -t -c "SELECT COUNT(*) FROM users;" 2^>nul') do set USER_COUNT=%%a
    set USER_COUNT=%USER_COUNT: =%
    echo    👤 Пользователей: %USER_COUNT%
) else (
    echo ⏳ Таблицы будут созданы при первом запуске приложения
    echo    (Hibernate создаст их автоматически)
)

echo.
echo [5/5] Проверка application.yml...
echo.

REM Проверяем пароль в конфигурации
findstr /C:"password: 123261181" "src\main\resources\application.yml" >nul 2>&1
if %errorLevel% equ 0 (
    echo ✅ Пароль в application.yml: 123261181
) else (
    echo ⚠️  ВНИМАНИЕ: Пароль в application.yml отличается от стандартного
    echo    Убедитесь что он совпадает с паролем PostgreSQL
)

echo.
echo ========================================
echo ✅ POSTGRESQL НАСТРОЕН И ГОТОВ!
echo ========================================
echo.
echo 🎯 Следующий шаг: Запустите приложение
echo.
echo    start.bat
echo.
echo 🔑 Учетные данные для входа:
echo    Email:    admin@university.kz
echo    Пароль:   admin123
echo.
echo 🌐 После запуска откройте:
echo    http://localhost:8080/auth/login
echo.
echo ========================================
echo.
pause

