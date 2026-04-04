# ════════════════════════════════════════════════════════════════
# СОЗДАНИЕ ПОЛЬЗОВАТЕЛЕЙ ДЛЯ ПРОФЕССОРОВ
# ════════════════════════════════════════════════════════════════

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "  СОЗДАНИЕ ПОЛЬЗОВАТЕЛЕЙ ДЛЯ ПРОФЕССОРОВ" -ForegroundColor Yellow
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

# Настройки подключения к PostgreSQL
$env:PGHOST = "localhost"
$env:PGPORT = "5432"
$env:PGDATABASE = "university_db"
$env:PGUSER = "postgres"
$env:PGPASSWORD = "1234"

Write-Host "📊 Проверяем профессоров в базе данных..." -ForegroundColor Green
Write-Host ""

# Путь к SQL файлу
$sqlFile = Join-Path $PSScriptRoot "..\..\sql\CREATE_USERS_FOR_PROFESSORS.sql"

if (Test-Path $sqlFile) {
    Write-Host "📄 Выполняем SQL скрипт: $sqlFile" -ForegroundColor Cyan
    Write-Host ""

    # Выполняем SQL скрипт
    & psql -h $env:PGHOST -p $env:PGPORT -U $env:PGUSER -d $env:PGDATABASE -f $sqlFile

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
        Write-Host "✅ УСПЕШНО! Пользователи для профессоров созданы" -ForegroundColor Green
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Green
        Write-Host ""
        Write-Host "📝 Данные для входа:" -ForegroundColor Yellow
        Write-Host "   Email:  email профессора из базы данных" -ForegroundColor White
        Write-Host "   Пароль: professor123" -ForegroundColor White
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
        Write-Host "❌ ОШИБКА при создании пользователей" -ForegroundColor Red
        Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Red
        Write-Host ""
        Write-Host "Проверьте:" -ForegroundColor Yellow
        Write-Host "  1. Запущен ли PostgreSQL" -ForegroundColor White
        Write-Host "  2. Правильные ли настройки подключения в этом файле" -ForegroundColor White
        Write-Host "  3. Существует ли база данных university_db" -ForegroundColor White
        Write-Host ""
    }
} else {
    Write-Host "❌ SQL файл не найден: $sqlFile" -ForegroundColor Red
}

Write-Host ""
Write-Host "Нажмите любую клавишу для выхода..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

