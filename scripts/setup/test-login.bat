@echo off
chcp 65001 >nul
echo.
echo ════════════════════════════════════════════════════════════════
echo 🔐 ТЕСТ ВХОДА В СИСТЕМУ
echo ════════════════════════════════════════════════════════════════
echo.

REM Проверяем что приложение запущено
echo [1/5] Проверка что приложение запущено...
netstat -ano | findstr ":8080" | findstr "LISTENING" >nul 2>&1
if %errorLevel% neq 0 (
    echo ❌ Приложение НЕ ЗАПУЩЕНО на порту 8080
    echo.
    echo Запустите приложение командой: start.bat
    echo Затем запустите этот скрипт снова
    echo.
    pause
    exit /b 1
)
echo ✅ Приложение запущено на порту 8080
echo.

echo [2/5] Тестирование доступа к странице входа...
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:8080/auth/login
echo.

echo [3/5] Попытка входа с учетными данными...
echo    Email: admin@university.kz
echo    Пароль: admin123
echo.

REM Отправляем POST запрос на вход
curl -v -X POST http://localhost:8080/auth/login ^
     -H "Content-Type: application/x-www-form-urlencoded" ^
     -d "email=admin@university.kz&password=admin123" ^
     --cookie-jar cookies.txt ^
     --location ^
     2>&1 | findstr /C:"Location:" /C:"HTTP/"

echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo [4/5] ВАЖНО: Смотрите логи в консоли приложения!
echo.
echo Вы должны увидеть одно из:
echo.
echo ✅ УСПЕХ:
echo    🔍 [AUTH] Попытка входа с email: admin@university.kz
echo    ✅ [AUTH] Пользователь найден: Администратор
echo    ✅ [AUTH SUCCESS] Успешная аутентификация!
echo    → Location: /admin/dashboard
echo.
echo ❌ ОШИБКА:
echo    🔍 [AUTH] Попытка входа с email: admin@university.kz
echo    ❌ [AUTH FAILURE] Ошибка аутентификации
echo    → Причина: [смотрите в логах]
echo.
echo ════════════════════════════════════════════════════════════════
echo.

if exist cookies.txt (
    echo [5/5] Проверка созданной сессии...
    type cookies.txt | findstr "JSESSIONID"
    if %errorLevel% equ 0 (
        echo.
        echo ✅ Сессия создана! Cookie JSESSIONID получен
        echo    Это означает что вход УСПЕШЕН!
    ) else (
        echo.
        echo ❌ Сессия НЕ создана
        echo    Вход НЕ УДАЛСЯ
    )
    del cookies.txt 2>nul
)

echo.
echo ════════════════════════════════════════════════════════════════
echo.
echo 📖 ЧТО ДЕЛАТЬ ДАЛЬШЕ:
echo.
echo 1. Если видите HTTP 302 и Location: /admin/dashboard
echo    → ВХОД РАБОТАЕТ! ✅
echo    → Откройте http://localhost:8080/auth/login в браузере
echo.
echo 2. Если видите HTTP 302 и Location: /auth/login?error=true
echo    → Ошибка входа ❌
echo    → Смотрите логи приложения для причины
echo.
echo 3. Если видите другие ошибки
echo    → Запустите: diagnose-login.bat
echo.
echo ════════════════════════════════════════════════════════════════
echo.
pause

