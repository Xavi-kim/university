@echo off
chcp 65001 >nul
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔧 ОЧИСТКА БД И СОЗДАНИЕ ПРЕПОДАВАТЕЛЯ
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
) else if exist "C:\Program Files\PostgreSQL\17\bin\psql.exe" (
    set PSQL_PATH=C:\Program Files\PostgreSQL\17\bin
) else (
    echo ❌ PostgreSQL не найден в стандартных путях
    echo.
    echo Попробуйте вручную:
    echo 1. Откройте pgAdmin или psql
    echo 2. Выполните: DELETE FROM users;
    echo 3. Перезапустите приложение
    echo.
    pause
    exit /b 1
)

echo ✅ PostgreSQL найден: %PSQL_PATH%
echo.

echo [1/6] Остановка приложения...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul
echo ✅ Приложение остановлено
echo.

echo [2/6] Подключение к базе данных...
echo.

echo [3/6] Удаление старых данных (в правильном порядке)...
echo.

echo    Удаление enrollments...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "DELETE FROM enrollments;" 2>nul
if %errorLevel% equ 0 (
    echo    ✅ Enrollments удалены
) else (
    echo    ⚠️  Таблица enrollments пуста или не существует
)

echo    Удаление courses...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "DELETE FROM courses;" 2>nul
if %errorLevel% equ 0 (
    echo    ✅ Courses удалены
) else (
    echo    ⚠️  Таблица courses пуста или не существует
)

echo    Удаление professors...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "DELETE FROM professors;" 2>nul
if %errorLevel% equ 0 (
    echo    ✅ Professors удалены
) else (
    echo    ⚠️  Таблица professors пуста или не существует
)

echo    Удаление universities...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "DELETE FROM universities;" 2>nul
if %errorLevel% equ 0 (
    echo    ✅ Universities удалены
) else (
    echo    ⚠️  Таблица universities пуста или не существует
)

echo    Удаление users...
"%PSQL_PATH%\psql.exe" -U postgres -d university_db -c "DELETE FROM users;" 2>nul
if %errorLevel% equ 0 (
    echo    ✅ Users удалены
) else (
    echo    ⚠️  Таблица users пуста или не существует
)

echo.
echo ✅ База данных очищена!
echo.

echo [4/6] Проверка очистки...
for /f "tokens=*" %%a in ('"%PSQL_PATH%\psql.exe" -U postgres -d university_db -t -c "SELECT COUNT(*) FROM users;" 2^>nul') do set USER_COUNT=%%a
set USER_COUNT=%USER_COUNT: =%

if "%USER_COUNT%"=="0" (
    echo ✅ Пользователей в БД: 0 (готово к инициализации)
) else (
    echo ⚠️  Пользователей в БД: %USER_COUNT%
)
echo.

echo [5/6] Освобождение порта 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    taskkill /F /PID %%a >nul 2>&1
)
timeout /t 1 /nobreak >nul
echo ✅ Порт 8080 свободен
echo.

echo [6/6] Запуск приложения с инициализацией...
echo.
echo ════════════════════════════════════════════════════════════════
echo ⏳ Ожидайте создания пользователей...
echo ════════════════════════════════════════════════════════════════
echo.
echo Вы должны увидеть:
echo    ✅ База данных инициализирована тестовыми данными
echo    👤 Пользователей: 4
echo    🔑 Тестовые аккаунты:
echo       ПРЕПОДАВАТЕЛЬ: teacher@university.kz / teacher123
echo.
echo ════════════════════════════════════════════════════════════════
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

REM Открываем браузер через 30 секунд
start "" cmd /c "timeout /t 30 /nobreak >nul & start http://localhost:8080/auth/login"

REM Запускаем приложение
"%JAVA_HOME%\bin\java.exe" -jar target\university-1.0-SNAPSHOT.jar

echo.
echo Приложение остановлено.
pause

