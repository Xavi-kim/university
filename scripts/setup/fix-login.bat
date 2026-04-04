@echo off
chcp 65001 >nul
cls
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                                                                ║
echo ║        🔐 РЕШЕНИЕ ПРОБЛЕМЫ: "Неверный email или пароль"       ║
echo ║                                                                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo 📧 Email:  admin@university.kz
echo 🔑 Пароль: admin123
echo ❌ Ошибка: "Неверный email или пароль"
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 🎯 ПРИЧИНА ПРОБЛЕМЫ:
echo    PostgreSQL не установлен или база данных не настроена
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 📋 ВЫБЕРИТЕ ДЕЙСТВИЕ:
echo.
echo    [1] 🔍 Диагностика - проверить что не работает
echo    [2] 🔧 Настроить PostgreSQL (если уже установлен)
echo    [3] 📖 Инструкция по установке PostgreSQL
echo    [4] 🚀 Запустить приложение (если всё готово)
echo    [5] ❌ Выход
echo.
echo ════════════════════════════════════════════════════════════════
echo.

set /p choice="Введите номер (1-5): "

if "%choice%"=="1" goto DIAGNOSE
if "%choice%"=="2" goto SETUP
if "%choice%"=="3" goto INSTRUCTIONS
if "%choice%"=="4" goto START_APP
if "%choice%"=="5" goto END

echo.
echo ❌ Неверный выбор
timeout /t 2 /nobreak >nul
goto START

:DIAGNOSE
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔍 ЗАПУСК ДИАГНОСТИКИ...
echo ════════════════════════════════════════════════════════════════
echo.
call diagnose-login.bat
goto END

:SETUP
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔧 НАСТРОЙКА POSTGRESQL...
echo ════════════════════════════════════════════════════════════════
echo.
echo ⚠️  Требуются права администратора!
echo.
pause
powershell -Command "Start-Process -Verb RunAs -FilePath 'setup-postgresql.bat'"
goto END

:INSTRUCTIONS
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 📖 ИНСТРУКЦИЯ ПО УСТАНОВКЕ POSTGRESQL
echo ════════════════════════════════════════════════════════════════
echo.
echo ШАГ 1: СКАЧАЙТЕ POSTGRESQL 16
echo ────────────────────────────────────────────────────────────────
echo.
echo    🔗 Ссылка для загрузки:
echo       https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
echo.
echo    Выберите:
echo    • Windows x86-64
echo    • Version: PostgreSQL 16.x
echo    • Нажмите Download
echo.
pause
echo.
echo ШАГ 2: УСТАНОВИТЕ POSTGRESQL
echo ────────────────────────────────────────────────────────────────
echo.
echo    1. Запустите установщик postgresql-16.x-windows-x64.exe
echo.
echo    2. Параметры установки:
echo       • Installation Directory: (по умолчанию)
echo         C:\Program Files\PostgreSQL\16
echo.
echo       • Components: (выберите все)
echo         ✅ PostgreSQL Server
echo         ✅ pgAdmin 4
echo         ✅ Stack Builder
echo         ✅ Command Line Tools
echo.
echo       • Data Directory: (по умолчанию)
echo         C:\Program Files\PostgreSQL\16\data
echo.
echo       • ⚠️  Password: (ВАЖНО!)
echo         123261181
echo         ^(Этот пароль уже прописан в application.yml^)
echo.
echo       • Port: 5432
echo.
echo       • Locale: Russian, Russia (или Default locale)
echo.
echo    3. Нажмите Next → Next → Install
echo.
echo    4. Дождитесь завершения (~5 минут)
echo.
echo    5. Снимите галочку "Launch Stack Builder" → Finish
echo.
pause
echo.
echo ШАГ 3: ЗАПУСТИТЕ СЛУЖБУ POSTGRESQL
echo ────────────────────────────────────────────────────────────────
echo.
echo    Способ 1: Через Services
echo    • Win+R → services.msc → Enter
echo    • Найдите: postgresql-x64-16
echo    • Статус должен быть: Running
echo    • Если Stopped: Правый клик → Start
echo.
echo    Способ 2: Через PowerShell (от Администратора)
echo    • Start-Service postgresql-x64-16
echo.
pause
echo.
echo ШАГ 4: СОЗДАЙТЕ БАЗУ ДАННЫХ
echo ────────────────────────────────────────────────────────────────
echo.
echo    Откройте командную строку (cmd) и выполните:
echo.
echo    cd C:\Program Files\PostgreSQL\16\bin
echo    psql -U postgres
echo    ^(введите пароль: 123261181^)
echo.
echo    CREATE DATABASE university_db;
echo    \l
echo    ^(должны увидеть university_db в списке^)
echo    \q
echo.
pause
echo.
echo ШАГ 5: ЗАПУСТИТЕ ПРИЛОЖЕНИЕ
echo ────────────────────────────────────────────────────────────────
echo.
echo    cd C:\jakarta\university
echo    start.bat
echo.
echo    Приложение автоматически:
echo    ✅ Подключится к PostgreSQL
echo    ✅ Создаст таблицы
echo    ✅ Добавит тестовых пользователей
echo.
pause
echo.
echo ШАГ 6: ВОЙДИТЕ В СИСТЕМУ
echo ────────────────────────────────────────────────────────────────
echo.
echo    Откройте браузер:
echo    http://localhost:8080/auth/login
echo.
echo    Введите учетные данные:
echo    Email:    admin@university.kz
echo    Пароль:   admin123
echo.
echo    Нажмите "Войти"
echo.
echo    Результат: ✅ Успешный вход → Панель администратора
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 📄 Подробная инструкция в файле:
echo    РЕШЕНИЕ_ПРОБЛЕМЫ_ВХОДА.md
echo.
pause
goto END

:START_APP
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo 🚀 ЗАПУСК ПРИЛОЖЕНИЯ...
echo ════════════════════════════════════════════════════════════════
echo.
echo Проверка готовности...
echo.

REM Проверяем PostgreSQL
if exist "C:\Program Files\PostgreSQL\16\bin\psql.exe" (
    echo ✅ PostgreSQL установлен
) else if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" (
    echo ✅ PostgreSQL установлен
) else (
    echo ❌ PostgreSQL не установлен
    echo.
    echo Сначала установите PostgreSQL (выберите пункт 3)
    echo.
    pause
    goto END
)

REM Проверяем службу
sc query postgresql-x64-16 | findstr "RUNNING" >nul 2>&1
if %errorLevel% neq 0 (
    echo ❌ Служба PostgreSQL не запущена
    echo.
    echo Запустите службу через Services.msc
    echo или выполните: net start postgresql-x64-16
    echo.
    pause
    goto END
)

echo ✅ Служба PostgreSQL запущена
echo.
echo Запускаем приложение...
echo.
call start.bat
goto END

:END
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 💡 ПОЛЕЗНЫЕ ССЫЛКИ:
echo.
echo    📄 Полная инструкция:   РЕШЕНИЕ_ПРОБЛЕМЫ_ВХОДА.md
echo    🔍 Диагностика:         diagnose-login.bat
echo    🔧 Настройка:           setup-postgresql.bat
echo    🚀 Запуск:              start.bat
echo.
echo ════════════════════════════════════════════════════════════════
echo.
pause

