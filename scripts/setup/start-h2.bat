@echo off
cd C:\jakarta\university

echo ========================================
echo University Management System
echo Режим: H2 Database (встроенная)
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo [1/3] Очистка процессов...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul

echo [2/3] Освобождение порта 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 2 /nobreak >nul
echo     ✅ Порт 8080 свободен
echo.

echo [3/3] Запуск приложения с H2...
echo.
echo ========================================
echo 📊 База данных: H2 (встроенная)
echo 📁 Файл БД: .\data\universitydb.mv.db
echo 🌐 H2 Console: http://localhost:8080/h2-console
echo.
echo 🔗 URL приложения:
echo    Главная:  http://localhost:8080/
echo    Вход:     http://localhost:8080/auth/login
echo    Профиль:  http://localhost:8080/profile
echo    Каталог:  http://localhost:8080/catalog
echo.
echo 🔑 Учётные данные:
echo    Админ:    admin@university.kz / admin123
echo    Студент:  asel@student.kz / 123456
echo.
echo 🎯 H2 Console (для просмотра БД):
echo    URL:      http://localhost:8080/h2-console
echo    JDBC URL: jdbc:h2:file:./data/universitydb
echo    User:     sa
echo    Password: (пусто)
echo.
echo ========================================
echo.
echo ⏳ Ожидайте запуска (~30 секунд)...
echo 📋 Смотрите логи для "Started Application"
echo.

REM Открываем браузер через 30 секунд
start "" cmd /c "timeout /t 30 /nobreak >nul & start http://localhost:8080/"

REM Запускаем с профилем h2
call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=h2

echo.
echo Приложение остановлено.
pause

