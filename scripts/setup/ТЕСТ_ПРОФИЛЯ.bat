@echo off
chcp 65001 >nul
cls
color 0A
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                                                                ║
echo ║     ✅ ПРОФИЛЬ СТУДЕНТА - КНОПКА И РЕДАКТИРОВАНИЕ ГОТОВЫ      ║
echo ║                                                                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo 🎉 ВЫПОЛНЕНО:
echo    ✅ Добавлена кнопка "👤 Профиль" в панель студента
echo    ✅ Подключена возможность редактирования профиля
echo    ✅ Улучшена навигация между страницами
echo    ✅ Проект скомпилирован
echo.
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
echo 🎯 КАК ПРОВЕРИТЬ:
echo ════════════════════════════════════════════════════════════════
echo.
echo 1. Дождитесь сообщения "Started Application"
echo.
echo 2. Откройте: http://localhost:8080/auth/login
echo.
echo 3. Войдите как студент:
echo    Email:    asel@student.kz
echo    Пароль:   123456
echo.
echo 4. На панели студента вы увидите новую кнопку:
echo    [👤 Профиль]
echo.
echo 5. Нажмите на неё и перейдите к профилю
echo.
echo 6. Нажмите "Редактировать профиль" и измените:
echo    - Имя
echo    - Телефон: +7 (777) 123-45-67
echo    - Биографию
echo    - Аватар: https://i.pravatar.cc/300?img=5
echo.
echo 7. Сохраните изменения ✅
echo.
echo 8. Используйте кнопку "← Вернуться к панели студента"
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

