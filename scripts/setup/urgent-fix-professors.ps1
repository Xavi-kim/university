# ════════════════════════════════════════════════════════════════
# СРОЧНОЕ ИСПРАВЛЕНИЕ ВСЕХ ПРОФЕССОРОВ
# ════════════════════════════════════════════════════════════════

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
Write-Host "  🚨 СРОЧНОЕ ИСПРАВЛЕНИЕ НЕПОЛНЫХ ХЕШЕЙ ПАРОЛЕЙ" -ForegroundColor Yellow
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
Write-Host ""

# Настройки подключения к PostgreSQL
$env:PGHOST = "localhost"
$env:PGPORT = "5432"
$env:PGDATABASE = "university_db"
$env:PGUSER = "postgres"
$env:PGPASSWORD = "1234"

Write-Host "❌ ПРОБЛЕМА:" -ForegroundColor Red
Write-Host "   Пользователи найдены, но пароли не работают" -ForegroundColor White
Write-Host "   Причина: BCrypt хеш обрезан (длина < 60 символов)" -ForegroundColor White
Write-Host ""
Write-Host "✅ РЕШЕНИЕ:" -ForegroundColor Green
Write-Host "   Заменить все неполные хеши на правильный" -ForegroundColor White
Write-Host "   Пароль для всех: professor123" -ForegroundColor White
Write-Host ""

# Путь к SQL файлу
$sqlFile = Join-Path $PSScriptRoot "..\..\sql\URGENT_FIX_ALL_PROFESSORS.sql"

if (Test-Path $sqlFile) {
    Write-Host "📄 Выполняем SQL скрипт исправления..." -ForegroundColor Cyan
    Write-Host ""

    # Выполняем SQL скрипт
    & psql -h $env:PGHOST -p $env:PGPORT -U $env:PGUSER -d $env:PGDATABASE -f $sqlFile

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
        Write-Host "✅ УСПЕШНО! ВСЕ ХЕШИ ИСПРАВЛЕНЫ" -ForegroundColor Green
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
        Write-Host ""
        Write-Host "📝 Данные для входа (для ВСЕХ профессоров):" -ForegroundColor Yellow
        Write-Host "   Email:  email профессора из базы данных" -ForegroundColor White
        Write-Host "   Пароль: professor123" -ForegroundColor White
        Write-Host ""
        Write-Host "🌐 Примеры:" -ForegroundColor Yellow
        Write-Host "   aigul.nurbekova@kaznu.kz / professor123" -ForegroundColor Cyan
        Write-Host "   marat.tokayev@kaznu.kz / professor123" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "🚀 Попробуйте войти:" -ForegroundColor Yellow
        Write-Host "   http://localhost:8080/login" -ForegroundColor Cyan
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
        Write-Host "❌ ОШИБКА при выполнении скрипта" -ForegroundColor Red
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
        Write-Host ""
        Write-Host "Попробуйте вручную через pgAdmin:" -ForegroundColor Yellow
        Write-Host "  1. Откройте pgAdmin" -ForegroundColor White
        Write-Host "  2. Подключитесь к базе university_db" -ForegroundColor White
        Write-Host "  3. Query Tool → Выполните:" -ForegroundColor White
        Write-Host ""
        Write-Host "UPDATE users" -ForegroundColor Cyan
        Write-Host "SET password = '\$2a\$10\$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS'" -ForegroundColor Cyan
        Write-Host "WHERE role = 'PROFESSOR' AND LENGTH(password) != 60;" -ForegroundColor Cyan
        Write-Host ""
    }
} else {
    Write-Host "❌ SQL файл не найден: $sqlFile" -ForegroundColor Red
    Write-Host ""
    Write-Host "Выполните команду вручную в pgAdmin:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "UPDATE users" -ForegroundColor Cyan
    Write-Host "SET password = '\$2a\$10\$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS'" -ForegroundColor Cyan
    Write-Host "WHERE role = 'PROFESSOR' AND LENGTH(password) != 60;" -ForegroundColor Cyan
    Write-Host ""
}

Write-Host ""
Write-Host "Нажмите любую клавишу для выхода..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

