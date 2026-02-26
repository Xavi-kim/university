# Быстрый старт

## Запуск приложения

1. Откройте PowerShell в директории проекта
2. Выполните команды:

```powershell
# Установка Java
$env:JAVA_HOME="C:\Program Files\Java\jdk-24"
$env:Path="$env:JAVA_HOME\bin;$env:Path"

# Компиляция
.\mvnw.cmd clean package

# Запуск
.\mvnw.cmd spring-boot:run
```

3. Откройте браузер: http://localhost:8080

## Тестирование API

### Получить все университеты
```
GET http://localhost:8080/api/universities
```

### Получить всех преподавателей
```
GET http://localhost:8080/api/professors
```

### Получить все курсы
```
GET http://localhost:8080/api/courses
```

### Информация об API
```
GET http://localhost:8080/api/main/info
```

## Важно!

- Если порт 8080 занят, измените в `application.yml`:
```yaml
server:
  port: 8081  # Измените на свободный порт
```

- Для просмотра базы данных: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: _(пусто)_

