@echo off
chcp 65001 >nul
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo ✅ ФИНАЛЬНОЕ РЕШЕНИЕ - ИСПОЛЬЗУЕМ ХЕШ ОТ ADMIN
echo ════════════════════════════════════════════════════════════════
echo.
echo 🎯 РЕШЕНИЕ:
echo    Скопируем хеш пароля от admin к teacher
echo    Пароль admin точно работает!
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 📋 ВЫПОЛНИТЕ В pgAdmin:
echo.
echo UPDATE users
echo SET password = (SELECT password FROM users WHERE email = 'admin@university.kz')
echo WHERE email = 'teacher@university.kz';
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo После выполнения SQL нажмите любую клавишу для запуска приложения...
pause >nul
echo.
echo ════════════════════════════════════════════════════════════════
echo 🚀 ЗАПУСК ПРИЛОЖЕНИЯ
echo ════════════════════════════════════════════════════════════════
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo [1/3] Остановка старых процессов...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul
echo ✅ Готово
echo.

echo [2/3] Освобождение порта 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 1 /nobreak >nul
echo ✅ Порт свободен
echo.

echo [3/3] Запуск приложения...
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔐 УЧЕТНЫЕ ДАННЫЕ ДЛЯ ВХОДА:
echo ════════════════════════════════════════════════════════════════
echo.
echo Email:    teacher@university.kz
echo Пароль:   admin123
echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 🌐 Через 30 секунд откроется браузер:
echo    http://localhost:8080/auth/login
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

