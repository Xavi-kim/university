@echo off
chcp 65001 >nul
cls
color 0A
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                                                                ║
echo ║        🚀 ЗАПУСК С ДИАГНОСТИКОЙ ПРОБЛЕМЫ ВХОДА                 ║
echo ║                                                                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo 🔧 ЧТО ДОБАВЛЕНО:
echo    ✅ Детальное логирование аутентификации
echo    ✅ Логи ошибок и успехов входа
echo    ✅ Автоматическая диагностика
echo.
echo ════════════════════════════════════════════════════════════════
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo [1/4] Остановка предыдущих процессов...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul
echo ✅ Готово
echo.

echo [2/4] Освобождение порта 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 1 /nobreak >nul
echo ✅ Порт 8080 свободен
echo.

echo [3/4] Компиляция проекта...
call mvnw.cmd clean package -DskipTests -q
if %errorLevel% neq 0 (
    echo ❌ Ошибка компиляции
    pause
    exit /b 1
)
echo ✅ Проект скомпилирован
echo.

echo [4/4] Запуск приложения...
echo.
echo ════════════════════════════════════════════════════════════════
echo ⚠️  ВАЖНО: СМОТРИТЕ НА ЛОГИ НИЖЕ!
echo ════════════════════════════════════════════════════════════════
echo.
echo При попытке входа вы увидите:
echo.
echo 🔍 [AUTH] Попытка входа с email: ...
echo    ↓
echo ✅ [AUTH] Пользователь найден: ...
echo    ИЛИ
echo ❌ [AUTH] Пользователь не найден: ...
echo    ↓
echo ✅ [AUTH SUCCESS] Успешная аутентификация!
echo    Redirect: /admin/dashboard
echo    ИЛИ
echo ❌ [AUTH FAILURE] Ошибка аутентификации:
echo    Причина: ...
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 🌐 Откройте в браузере:
echo    http://localhost:8080/auth/login
echo.
echo 🔑 Учетные данные:
echo    Email:    admin@university.kz
echo    Пароль:   admin123
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo Ожидание запуска приложения...
echo.

REM Открываем браузер через 30 секунд
start "" cmd /c "timeout /t 30 /nobreak >nul & start http://localhost:8080/auth/login"

REM Запускаем приложение
"%JAVA_HOME%\bin\java.exe" -jar target\university-1.0-SNAPSHOT.jar

echo.
echo Приложение остановлено.
pause

