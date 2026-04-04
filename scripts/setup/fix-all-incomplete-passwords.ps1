# ════════════════════════════════════════════════════════════════
# ИСПРАВЛЕНИЕ ВСЕХ НЕПОЛНЫХ ХЕШЕЙ ПАРОЛЕЙ
# ════════════════════════════════════════════════════════════════

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  ИСПРАВЛЕНИЕ НЕПОЛНЫХ ХЕШЕЙ ПАРОЛЕЙ ПРОФЕССОРОВ" -ForegroundColor Yellow
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

# Настройки подключения к PostgreSQL
$env:PGHOST = "localhost"
$env:PGPORT = "5432"
$env:PGDATABASE = "university_db"
$env:PGUSER = "postgres"
$env:PGPASSWORD = "1234"

Write-Host "❌ Проблема:" -ForegroundColor Red
Write-Host "   Некоторые пользователи имеют неполный BCrypt хеш" -ForegroundColor White
Write-Host "   (длина меньше 60 символов)" -ForegroundColor White
Write-Host ""
Write-Host "✅ Решение:" -ForegroundColor Green
Write-Host "   Заменить все неполные хеши на правильный" -ForegroundColor White
Write-Host "   Пароль для всех: professor123" -ForegroundColor White
Write-Host ""

# Путь к SQL файлу
$sqlFile = Join-Path $PSScriptRoot "..\..\sql\FIX_ALL_INCOMPLETE_PASSWORDS.sql"

if (Test-Path $sqlFile) {
    Write-Host "📄 Выполняем SQL скрипт..." -ForegroundColor Cyan
    Write-Host ""

    # Выполняем SQL скрипт
    & psql -h $env:PGHOST -p $env:PGPORT -U $env:PGUSER -d $env:PGDATABASE -f $sqlFile

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
        Write-Host "✅ УСПЕШНО! Все хеши исправлены" -ForegroundColor Green
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
        Write-Host ""
        Write-Host "📝 Данные для входа (для всех профессоров):" -ForegroundColor Yellow
        Write-Host "   Email:  email профессора из базы данных" -ForegroundColor White
        Write-Host "   Пароль: professor123" -ForegroundColor White
        Write-Host ""
        Write-Host "🌐 Примеры:" -ForegroundColor Yellow
        Write-Host "   marat.tokayev@kaznu.kz / professor123" -ForegroundColor White
        Write-Host "   aigul.nurbekova@kaznu.kz / professor123" -ForegroundColor White
        Write-Host ""
        Write-Host "🚀 Войдите на сайт:" -ForegroundColor Yellow
        Write-Host "   http://localhost:8080/login" -ForegroundColor Cyan
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
        Write-Host "❌ ОШИБКА при исправлении хешей" -ForegroundColor Red
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
        Write-Host ""
        Write-Host "Попробуйте вручную через pgAdmin:" -ForegroundColor Yellow
        Write-Host "  1. Откройте pgAdmin" -ForegroundColor White
        Write-Host "  2. Query Tool" -ForegroundColor White
        Write-Host "  3. Откройте файл: sql\FIX_ALL_INCOMPLETE_PASSWORDS.sql" -ForegroundColor White
        Write-Host "  4. Нажмите F5" -ForegroundColor White
        Write-Host ""
    }
} else {
    Write-Host "❌ SQL файл не найден: $sqlFile" -ForegroundColor Red
}

Write-Host ""
Write-Host "Нажмите любую клавишу для выхода..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

