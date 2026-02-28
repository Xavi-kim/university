# 🎓 University Management System

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen?logo=springboot)
![Java](https://img.shields.io/badge/Java-24-orange?logo=openjdk)
![Spring Security](https://img.shields.io/badge/Spring%20Security-✓-brightgreen?logo=springsecurity)
![H2 Database](https://img.shields.io/badge/Database-H2-blue)
![License](https://img.shields.io/badge/License-MIT-blue)
![Status](https://img.shields.io/badge/Status-🟢%20Active-success)
![Version](https://img.shields.io/badge/Version-1.0--SNAPSHOT-yellow)

**Полнофункциональная LMS-платформа для управления университетом**  
*Вдохновлена Canvas LMS · Moodle · Coursera · Google Classroom*

[🚀 Быстрый старт](#-быстрый-старт) · [✨ Возможности](#-возможности) · [🔌 API](#-rest-api) · [🗺 Roadmap](#-roadmap) · [📚 Документация](#-документация)

</div>

---

## 📋 О проекте

**University Management System (UMS)** — веб-платформа для автоматизации учебного процесса. Администраторы управляют курсами и преподавателями, студенты записываются на курсы и отслеживают оценки — всё в одном интерфейсе с полным REST API.

```
┌─────────────────────────────────────────────────────────┐
│                  University Management System            │
│                                                         │
│   👑 Администратор          🎓 Студент                  │
│   ├── CRUD курсов           ├── Запись на курсы         │
│   ├── Управление данными    ├── Просмотр своих курсов   │
│   └── Статистика системы   └── Оценки и статусы        │
│                                                         │
│   🔌 REST API  ·  🔐 Spring Security  ·  🗄 H2/JPA    │
└─────────────────────────────────────────────────────────┘
```

---

## ✨ Возможности

### 🔐 Авторизация и безопасность
| Функция | Описание |
|---------|----------|
| Регистрация | По email и паролю, роль `STUDENT` по умолчанию |
| Вход / Выход | Сессионная аутентификация |
| BCrypt | Шифрование паролей (strength 10) |
| RBAC | Ролевая модель: `ADMIN` / `STUDENT` |
| Защита роутов | Проверка прав в контроллерах |

### 👑 Для администратора (`/admin/dashboard`)
- ✅ Просмотр и управление всеми курсами
- ✅ Создание / редактирование / удаление курсов
- ✅ Статистика: количество курсов, преподавателей, университетов
- ✅ Выставление оценок через REST API

### 🎓 Для студента (`/student/dashboard`)
- ✅ Запись на курс одним кликом
- ✅ Отписка от курса
- ✅ Просмотр своих курсов с оценками и статусами
- ✅ Фильтрация: показываются только незаписанные курсы

### 🔌 REST API (полный CRUD)
- ✅ `/api/courses` — курсы
- ✅ `/api/professors` — преподаватели
- ✅ `/api/universities` — университеты
- ✅ `/api/users` — пользователи
- ✅ `/api/enrollments` — записи (+ выставление оценок)

---

## 🚀 Быстрый старт

### Требования
- **Java 17+** (рекомендуется Java 24)
- Maven (поставляется через `mvnw`)

### Запуск

```powershell
# 1. Установить JAVA_HOME (Windows)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-24"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# 2. Перейти в папку проекта
cd C:\jakarta\university

# 3. Собрать проект
.\mvnw.cmd clean package -DskipTests

# 4. Запустить
.\mvnw.cmd spring-boot:run
```

```bash
# Linux / macOS
./mvnw clean package -DskipTests
./mvnw spring-boot:run
```

**Открыть в браузере:** `http://localhost:8080/`

---

## 🔑 Тестовые аккаунты

| Роль | Email | Пароль | Панель |
|------|-------|--------|--------|
| 👑 Администратор | `admin@university.kz` | `admin123` | `/admin/dashboard` |
| 🎓 Студент 1 | `asel@student.kz` | `123456` | `/student/dashboard` |
| 🎓 Студент 2 | `erlan@student.kz` | `123456` | `/student/dashboard` |

---

## 🌐 Страницы системы

| URL | Описание | Доступ |
|-----|----------|--------|
| `/` | Главная страница | 🌍 Публичный |
| `/about` | О системе | 🌍 Публичный |
| `/auth/login` | Вход | 🌍 Публичный |
| `/auth/register` | Регистрация | 🌍 Публичный |
| `/browse` | API Browser | 🌍 Публичный |
| `/admin/dashboard` | Панель администратора | 👑 ADMIN |
| `/student/dashboard` | Панель студента | 🎓 STUDENT |
| `/h2-console` | База данных (H2) | 🔧 Dev |

---

## 🔌 REST API

### Примеры запросов

```bash
# Получить все курсы
curl http://localhost:8080/api/courses

# Создать курс
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{"title":"Java","description":"ООП основы","department":"ИТ","semester":"2024-Fall"}'

# Записать студента (ID=2) на курс (ID=1)
curl -X POST "http://localhost:8080/api/enrollments?studentId=2&courseId=1"

# Выставить оценку 95.5 для записи (ID=1)
curl -X PATCH "http://localhost:8080/api/enrollments/1/grade?grade=95.5"

# Получить курсы студента
curl http://localhost:8080/api/enrollments/student/2
```

> 📖 Полный справочник API: [`docs/api/rest-api-reference.md`](docs/api/rest-api-reference.md)

---

## 🗄 База данных

**H2 Console:** `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:testdb` · User: `sa` · Password: *(пусто)*

```sql
-- Полезные запросы
SELECT * FROM USERS;
SELECT u.name, c.title, e.status, e.grade
FROM ENROLLMENTS e
JOIN USERS u ON e.user_id = u.id
JOIN COURSES c ON e.course_id = c.id;
```

---

## 🏗 Архитектура

```
Presentation (Controllers)
      ↓
Business Logic (Services)
      ↓
Data Access (Repositories / Spring Data JPA)
      ↓
Database (H2 In-Memory → PostgreSQL в prod)
```

**Стек:** Java 24 · Spring Boot 3.4 · Spring Security · Spring Data JPA · Hibernate · Thymeleaf · H2 · BCrypt · Jackson · Maven

> 📐 Детальная архитектура: [`docs/architecture/overview.md`](docs/architecture/overview.md)

---

## 🗺 Roadmap

| Фаза | Версия | Срок | Статус | Ключевые функции |
|------|--------|------|--------|-----------------|
| **Фаза 1 — MVP** | v1.0 | Q1 2026 | ✅ Готово | Auth, CRUD, Enrollments, REST API |
| **Фаза 2 — Core** | v1.1 | Q2 2026 | 🔄 Планируется | PostgreSQL, Профили, GPA, Swagger |
| **Фаза 3 — Smart** | v2.0 | Q3 2026 | 📋 Запланировано | Задания, Материалы, Уведомления, Форум |
| **Фаза 4 — Enterprise** | v3.0+ | 2027 | 🔮 Видение | AI, Мобильное приложение, OAuth2, Геймификация |

> 🗺 Полный Roadmap: [`docs/roadmap/ROADMAP.md`](docs/roadmap/ROADMAP.md)

---

## 📚 Документация

Полная документация хранится в папке [`docs/`](docs/) (локально, в `.gitignore`):

| Документ | Описание |
|---------|----------|
| [`docs/architecture/overview.md`](docs/architecture/overview.md) | Архитектурный обзор |
| [`docs/architecture/database-schema.md`](docs/architecture/database-schema.md) | ERD и схема БД |
| [`docs/architecture/tech-stack.md`](docs/architecture/tech-stack.md) | Технологический стек |
| [`docs/api/rest-api-reference.md`](docs/api/rest-api-reference.md) | REST API справочник |
| [`docs/guides/getting-started.md`](docs/guides/getting-started.md) | Быстрый старт |
| [`docs/guides/admin-guide.md`](docs/guides/admin-guide.md) | Руководство администратора |
| [`docs/guides/student-guide.md`](docs/guides/student-guide.md) | Руководство студента |
| [`docs/guides/development-guide.md`](docs/guides/development-guide.md) | Руководство разработчика |
| [`docs/features/current-features.md`](docs/features/current-features.md) | Текущие возможности |
| [`docs/features/planned-features.md`](docs/features/planned-features.md) | Планируемые функции |
| [`docs/roadmap/ROADMAP.md`](docs/roadmap/ROADMAP.md) | Полный Roadmap |

---

## 🛠 Разработка

```powershell
# Запуск тестов
.\mvnw.cmd test

# Запуск с логами SQL
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--logging.level.org.hibernate.SQL=DEBUG"

# Сборка JAR
.\mvnw.cmd clean package -DskipTests
java -jar target/university-1.0-SNAPSHOT.jar
```

> 🔧 Подробное руководство разработчика: [`docs/guides/development-guide.md`](docs/guides/development-guide.md)

---

## 📊 Структура проекта

```
src/main/java/org/example/university/
├── Application.java              ← Точка входа
├── config/
│   ├── SecurityConfig.java       ← Spring Security
│   └── DataInitializer.java      ← Seed данные
├── controller/                   ← HTTP контроллеры
├── dto/                          ← Data Transfer Objects
├── model/                        ← JPA Entity
├── repository/                   ← Spring Data JPA
└── service/                      ← Бизнес-логика
```

---

## 📄 Лицензия

MIT License · © 2026 University Management System

---

<div align="center">

**⭐ Понравился проект? Поставьте звезду!**

*Сделано с ❤️ на Spring Boot 3.4 · Java 24*

</div>
