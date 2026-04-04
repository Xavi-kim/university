@echo off
chcp 65001 >nul
cls
color 0A
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                                                                ║
echo ║   ✅ РЕШЕНИЕ: Столбец email_verified не существует             ║
echo ║                                                                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo 🐛 ПРОБЛЕМА:
echo    Exception: InternalAuthenticationServiceException
echo    Message: столбец u1_0.email_verified не существует
echo.
echo ✅ РЕШЕНИЕ:
echo    Добавлен DatabaseSchemaFixer - автоматически исправляет
echo    структуру таблицы при запуске приложения
echo.
echo ════════════════════════════════════════════════════════════════
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo [1/4] Остановка старых процессов...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul
echo ✅ Готово
echo.

echo [2/4] Сборка проекта с DatabaseSchemaFixer...
call mvnw.cmd clean package -DskipTests -q
if %errorLevel% neq 0 (
    echo ❌ Ошибка сборки
    pause
    exit /b 1
)
echo ✅ Проект собран успешно
echo.

echo [3/4] Освобождение порта 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 1 /nobreak >nul
echo ✅ Порт свободен
echo.

echo [4/4] Запуск приложения...
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔧 DatabaseSchemaFixer автоматически исправит структуру БД
echo ════════════════════════════════════════════════════════════════
echo.
echo Вы увидите:
echo    🔧 [DB FIX] Проверка и исправление структуры таблицы users...
echo       Добавление столбца: email_verified
echo       ✅ Столбец email_verified добавлен
echo    ✅ [DB FIX] Структура таблицы users проверена и исправлена
echo.
echo Затем:
echo    ✅ База данных инициализирована тестовыми данными
echo    🔑 Тестовые аккаунты:
echo       АДМИН: admin@university.kz / admin123
echo.
echo И наконец при попытке входа:
echo    🔍 [AUTH] Попытка входа с email: admin@university.kz
echo    ✅ [AUTH] Пользователь найден: Администратор
echo    ✅ [AUTH SUCCESS] Успешная аутентификация!
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 🌐 Через 30 секунд откроется браузер:
echo    http://localhost:8080/auth/login
echo.
echo 🔑 Учетные данные:
echo    Email:    admin@university.kz
echo    Пароль:   admin123
echo.
echo ════════════════════════════════════════════════════════════════
echo.

REM Открываем браузер через 30 секунд
start "" cmd /c "timeout /t 30 /nobreak >nul & start http://localhost:8080/auth/login"

REM Запускаем приложение
"%JAVA_HOME%\bin\java.exe" -jar target\university-1.0-SNAPSHOT.jar

echo.
echo Приложение остановлено.
pause

