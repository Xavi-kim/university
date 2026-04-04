# 🎓 University Management System

Полнофункциональная веб‑платформа для управления учебным процессом в университете.

- Backend: **Spring Boot 3.4** (Java **24**)
- UI: **Thymeleaf**
- DB: **PostgreSQL** (JPA/Hibernate)
- Security: **Spring Security + BCrypt**
- Email: **Spring Mail (SMTP / Gmail App Password)**

---

## ✨ Возможности

### 👑 Администратор
- Управление пользователями (включая назначение роли **PROFESSOR**)
- Мониторинг профессоров и статистика
- Управление курсами

### 👨‍🏫 Профессор
- Создание и управление курсами
- Домашние задания: создание, сбор работ
- Проверка работ и выставление оценок
- Просмотр студентов на курсах
- Сообщения/чат со студентами

### 👨‍🎓 Студент
- Просмотр профессоров и их курсов
- Запись на курсы
- Просмотр деталей курса («Подробнее»)
- Сдача домашних заданий
- Просмотр оценок
- Сообщения/чат с профессором

---

## 🚀 Быстрый старт (Windows)

### 1) Требования
- Java **24**
- PostgreSQL (локально)
- Maven используется через **Maven Wrapper** (`mvnw.cmd`)

### 2) Сборка и запуск

```powershell
cd C:\jakarta\university
.\mvnw.cmd clean package -DskipTests
java --enable-native-access=ALL-UNNAMED -jar target\university-1.0-SNAPSHOT.jar
```

Открыть в браузере:
- http://localhost:8080

---

## ⚙️ Настройки

### PostgreSQL
Файл: `src/main/resources/application.yml`

Проверь значения:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

### Email (SMTP)
Используется `SmtpEmailService` (реальная отправка).

Рекомендуемый способ — задавать параметры через переменные окружения:
- `SPRING_MAIL_USERNAME` — Gmail адрес
- `SPRING_MAIL_PASSWORD` — пароль приложения (App Password) без пробелов

---

## 🔐 Тестовые аккаунты

Актуальный список тестовых аккаунтов хранится в документации:
- [`docs/README.md`](./docs/README.md#тестовые-аккаунты)

---

## 📚 Документация

- Главная страница документации: [`docs/README.md`](./docs/README.md)
- Архитектура: [`docs/architecture/overview.md`](./docs/architecture/overview.md)
- Структура кода: [`docs/architecture/code-structure.md`](./docs/architecture/code-structure.md)
- Схема БД: [`docs/architecture/database-schema.md`](./docs/architecture/database-schema.md)
- REST API: [`docs/api/rest-api-reference.md`](./docs/api/rest-api-reference.md)
- Админ‑панель: [`docs/guides/ADMIN_PANEL_GUIDE.md`](./docs/guides/ADMIN_PANEL_GUIDE.md)
- Email/SMTP: [`docs/guides/EMAIL_AND_ADMIN_GUIDE.md`](./docs/guides/EMAIL_AND_ADMIN_GUIDE.md)

---

## 🧩 Структура репозитория (кратко)

- `src/main/java` — контроллеры, сервисы, модели, репозитории
- `src/main/resources/templates` — HTML (Thymeleaf)
- `sql/` — вспомогательные SQL‑скрипты
- `scripts/` — скрипты обслуживания/настройки
- `docs/` — документация
