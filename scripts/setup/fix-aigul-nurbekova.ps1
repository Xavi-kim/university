# ════════════════════════════════════════════════════════════════
# БЫСТРОЕ ИСПРАВЛЕНИЕ: Создать пользователя для aigul.nurbekova@kaznu.kz
# ════════════════════════════════════════════════════════════════

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  ИСПРАВЛЕНИЕ: Создание пользователя для Aigul Nurbekova" -ForegroundColor Yellow
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

# Настройки подключения к PostgreSQL
$env:PGHOST = "localhost"
$env:PGPORT = "5432"
$env:PGDATABASE = "university_db"
$env:PGUSER = "postgres"
$env:PGPASSWORD = "1234"

Write-Host "📧 Email: aigul.nurbekova@kaznu.kz" -ForegroundColor Green
Write-Host "🔑 Пароль: professor123" -ForegroundColor Green
Write-Host ""

# Путь к SQL файлу
$sqlFile = Join-Path $PSScriptRoot "..\..\sql\FIX_AIGUL_NURBEKOVA.sql"

if (Test-Path $sqlFile) {
    Write-Host "📄 Выполняем SQL скрипт..." -ForegroundColor Cyan
    Write-Host ""

    # Выполняем SQL скрипт
    & psql -h $env:PGHOST -p $env:PGPORT -U $env:PGUSER -d $env:PGDATABASE -f $sqlFile

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
        Write-Host "✅ УСПЕШНО! Пользователь создан" -ForegroundColor Green
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
        Write-Host ""
        Write-Host "📝 Данные для входа:" -ForegroundColor Yellow
        Write-Host "   Email:  aigul.nurbekova@kaznu.kz" -ForegroundColor White
        Write-Host "   Пароль: professor123" -ForegroundColor White
        Write-Host ""
        Write-Host "🌐 Войдите на сайт:" -ForegroundColor Yellow
        Write-Host "   http://localhost:8080/login" -ForegroundColor White
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
        Write-Host "❌ ОШИБКА при создании пользователя" -ForegroundColor Red
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
        Write-Host ""
        Write-Host "Попробуйте вручную через pgAdmin:" -ForegroundColor Yellow
        Write-Host "  1. Откройте pgAdmin" -ForegroundColor White
        Write-Host "  2. Query Tool" -ForegroundColor White
        Write-Host "  3. Откройте файл: sql\FIX_AIGUL_NURBEKOVA.sql" -ForegroundColor White
        Write-Host "  4. Нажмите F5" -ForegroundColor White
        Write-Host ""
    }
} else {
    Write-Host "❌ SQL файл не найден: $sqlFile" -ForegroundColor Red
}

Write-Host ""
Write-Host "Нажмите любую клавишу для выхода..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

