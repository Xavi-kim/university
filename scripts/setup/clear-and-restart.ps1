# ════════════════════════════════════════════════════════════════
# ОЧИСТКА БАЗЫ ДАННЫХ И ПЕРЕЗАПУСК
# ════════════════════════════════════════════════════════════════

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  🗑️  ОЧИСТКА БАЗЫ ДАННЫХ" -ForegroundColor Yellow
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

# Настройки подключения к PostgreSQL
$env:PGHOST = "localhost"
$env:PGPORT = "5432"
$env:PGDATABASE = "university_db"
$env:PGUSER = "postgres"
$env:PGPASSWORD = "1234"

Write-Host "⚠️  ВНИМАНИЕ: Это удалит ВСЕ данные из базы!" -ForegroundColor Red
Write-Host ""
Write-Host "Нажмите любую клавишу чтобы продолжить, или Ctrl+C чтобы отменить..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
Write-Host ""

# Путь к SQL файлу
$sqlFile = Join-Path $PSScriptRoot "..\..\sql\CLEAR_DATABASE_ONCE.sql"

Write-Host "📄 Выполняем SQL скрипт очистки..." -ForegroundColor Cyan
Write-Host ""

# Выполняем SQL скрипт
& psql -h $env:PGHOST -p $env:PGPORT -U $env:PGUSER -d $env:PGDATABASE -f $sqlFile

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
    Write-Host "✅ БАЗА ДАННЫХ ОЧИЩЕНА!" -ForegroundColor Green
    Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
    Write-Host ""
    Write-Host "Теперь запустите приложение:" -ForegroundColor Yellow
    Write-Host "   mvnw spring-boot:run" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Приложение автоматически создаст все таблицы и тестовые данные" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
    Write-Host "❌ ОШИБКА при очистке базы данных" -ForegroundColor Red
    Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
    Write-Host ""
}

Write-Host ""
Write-Host "Нажмите любую клавишу для выхода..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

