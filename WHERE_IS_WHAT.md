# 🎓 ПОЛНОЕ РУКОВОДСТВО ПО ПРОЕКТУ

## 📂 ГДЕ ЧТО НАХОДИТСЯ

### 🔐 Система аутентификации

#### SecurityConfig.java
**Путь:** `src/main/java/org/example/university/config/SecurityConfig.java`

**Что делает:**
- Настраивает защиту URL по ролям
- Определяет, какие страницы открыты, какие требуют входа
- Настраивает форму входа и выхода
- Управляет сессиями пользователей

**Важные настройки:**
```java
// Открытые URL (без входа)
.requestMatchers("/", "/auth/**", "/api/**").permitAll()

// Только для администраторов
.requestMatchers("/admin/**").hasRole("ADMIN")

// Остальные - требуют входа
.anyRequest().authenticated()
```

#### CustomUserDetailsService.java
**Путь:** `src/main/java/org/example/university/service/CustomUserDetailsService.java`

**Что делает:**
- Загружает пользователя из базы данных PostgreSQL
- Преобразует нашу модель User в Spring Security UserDetails
- Используется Spring Security для проверки пароля при входе

---

### 👤 Работа с пользователями

#### User.java (Entity)
**Путь:** `src/main/java/org/example/university/model/User.java`

**Поля:**
- `id` - уникальный идентификатор
- `name` - имя пользователя
- `email` - email (используется для входа)
- `password` - хешированный пароль (BCrypt)
- `role` - роль (STUDENT, ADMIN, PROFESSOR)
- `enabled` - активен ли аккаунт

#### UserRepository.java
**Путь:** `src/main/java/org/example/university/repository/UserRepository.java`

**Методы:**
- `findByEmail(String email)` - найти пользователя по email
- `existsByEmail(String email)` - проверить, существует ли email

#### UserService.java
**Путь:** `src/main/java/org/example/university/service/UserService.java`

**Методы:**
- `getAllUsers()` - получить всех пользователей
- `getUserById(Long id)` - получить по ID
- `getUserByEmail(String email)` - получить по email
- `saveUser(User user)` - сохранить/обновить
- `deleteUser(Long id)` - удалить
- `updateUserRole(Long id, String role)` - изменить роль

#### AuthController.java
**Путь:** `src/main/java/org/example/university/controller/AuthController.java`

**Страницы:**
- `GET /auth/login` - показать форму входа
- `POST /auth/login` - обработать вход (проверка email/пароля)
- `GET /auth/register` - показать форму регистрации
- `POST /auth/register` - создать нового пользователя
- `GET /auth/logout` - выйти из системы

---

### 📚 Курсы

#### Course.java (Entity)
**Путь:** `src/main/java/org/example/university/model/Course.java`

**Поля:**
- `id`, `name`, `code`, `description`
- `credits` - кредиты (часы)
- `semester` - семестр
- `university` - связь с университетом
- `professor` - связь с преподавателем

#### CourseController.java (REST API)
**Путь:** `src/main/java/org/example/university/controller/CourseController.java`

**Эндпоинты:**
- `GET /api/courses` - список всех курсов
- `GET /api/courses/{id}` - один курс
- `POST /api/courses` - создать курс
- `PUT /api/courses/{id}` - обновить курс
- `DELETE /api/courses/{id}` - удалить курс

#### AdminController.java (Веб-страницы)
**Путь:** `src/main/java/org/example/university/controller/AdminController.java`

**Страницы для курсов:**
- `GET /admin/courses` - список курсов (HTML таблица)
- `GET /admin/courses/new` - форма создания курса
- `POST /admin/courses/save` - сохранить курс
- `GET /admin/courses/edit/{id}` - форма редактирования
- `POST /admin/courses/delete/{id}` - удалить курс

---

### 🏛️ Университеты

#### University.java (Entity)
**Путь:** `src/main/java/org/example/university/model/University.java`

**Поля:**
- `id`, `name`, `location`, `website`

#### UniversityController.java (REST API)
**Путь:** `src/main/java/org/example/university/controller/UniversityController.java`

**Эндпоинты:**
- `GET /api/universities` - список
- `GET /api/universities/{id}` - один
- `POST /api/universities` - создать
- `PUT /api/universities/{id}` - обновить
- `DELETE /api/universities/{id}` - удалить

#### AdminController.java (Веб-страницы)
**Страницы для университетов:**
- `GET /admin/universities` - список университетов
- `GET /admin/universities/new` - форма создания
- `POST /admin/universities/save` - сохранить
- `GET /admin/universities/edit/{id}` - редактировать
- `POST /admin/universities/delete/{id}` - удалить

---

### 👨‍🏫 Преподаватели

#### Professor.java (Entity)
**Путь:** `src/main/java/org/example/university/model/Professor.java`

**Поля:**
- `id`, `name`, `email`, `department`
- `university` - связь с университетом

#### ProfessorController.java (REST API)
**Путь:** `src/main/java/org/example/university/controller/ProfessorController.java`

**Эндпоинты:**
- `GET /api/professors` - список
- `GET /api/professors/{id}` - один
- `POST /api/professors` - создать
- `PUT /api/professors/{id}` - обновить
- `DELETE /api/professors/{id}` - удалить

#### AdminController.java (Веб-страницы)
**Страницы для преподавателей:**
- `GET /admin/professors` - список
- `GET /admin/professors/new` - создать
- `POST /admin/professors/save` - сохранить
- `GET /admin/professors/edit/{id}` - редактировать
- `POST /admin/professors/delete/{id}` - удалить

---

### 📝 Регистрации на курсы (Enrollments)

#### Enrollment.java (Entity)
**Путь:** `src/main/java/org/example/university/model/Enrollment.java`

**Поля:**
- `id`
- `student` - связь с User (студент)
- `course` - связь с Course
- `enrolledAt` - дата регистрации
- `status` - статус (ACTIVE, COMPLETED, DROPPED)

#### EnrollmentController.java (REST API)
**Путь:** `src/main/java/org/example/university/controller/EnrollmentController.java`

**Эндпоинты:**
- `GET /api/enrollments` - все регистрации
- `GET /api/enrollments/{id}` - одна
- `POST /api/enrollments` - записать студента на курс
- `DELETE /api/enrollments/{id}` - отменить регистрацию

---

### 🌐 HTML Страницы

**Путь:** `src/main/resources/templates/`

#### Публичные страницы:
- `index.html` - главная страница
- `catalog.html` - каталог курсов, преподавателей, университетов
- `about.html` - о проекте
- `login.html` - форма входа
- `register.html` - форма регистрации

#### Админ-панель (только ADMIN):
- `admin-dashboard.html` - главная админки
- `admin-course-form.html` - форма курса
- `admin-professor-form.html` - форма преподавателя
- `admin-university-form.html` - форма университета

#### Студенческий кабинет:
- `student-dashboard.html` - кабинет студента
- `dashboard.html` - общий дашборд

#### API документация:
- `api-docs.html` - документация REST API

---

### ⚙️ Конфигурация

#### application.yml
**Путь:** `src/main/resources/application.yml`

**Что настроено:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/university_db
    username: postgres
    password: 123261181
    
  jpa:
    hibernate:
      ddl-auto: update  # автоматически создаёт/обновляет таблицы
    show-sql: true      # показывает SQL запросы в логах
    
server:
  port: 8080            # порт приложения
```

#### DataInitializer.java
**Путь:** `src/main/java/org/example/university/config/DataInitializer.java`

**Что делает:**
- Запускается при старте приложения
- Проверяет, есть ли данные в БД
- Если нет - создаёт тестовые данные:
  - 3 университета
  - 5 преподавателей
  - 7 курсов
  - 3 пользователя (admin, 2 студента)

---

## 🗄️ База данных PostgreSQL

### Таблицы:

#### users
```sql
id          BIGINT PRIMARY KEY
name        VARCHAR(255) NOT NULL
email       VARCHAR(255) NOT NULL UNIQUE
password    VARCHAR(255) NOT NULL  -- BCrypt hash
role        VARCHAR(50) NOT NULL   -- STUDENT, ADMIN, PROFESSOR
enabled     BOOLEAN NOT NULL
```

#### universities
```sql
id          BIGINT PRIMARY KEY
name        VARCHAR(255) NOT NULL
location    VARCHAR(255)
website     VARCHAR(255)
```

#### professors
```sql
id              BIGINT PRIMARY KEY
name            VARCHAR(255) NOT NULL
email           VARCHAR(255) NOT NULL UNIQUE
department      VARCHAR(255)
university_id   BIGINT REFERENCES universities(id)
```

#### courses
```sql
id              BIGINT PRIMARY KEY
name            VARCHAR(255) NOT NULL
code            VARCHAR(50) NOT NULL UNIQUE
description     TEXT
credits         INTEGER
semester        VARCHAR(50)
university_id   BIGINT REFERENCES universities(id)
professor_id    BIGINT REFERENCES professors(id)
```

#### enrollments
```sql
id              BIGINT PRIMARY KEY
student_id      BIGINT NOT NULL REFERENCES users(id)
course_id       BIGINT NOT NULL REFERENCES courses(id)
enrolled_at     TIMESTAMP NOT NULL
status          VARCHAR(50) NOT NULL  -- ACTIVE, COMPLETED, DROPPED
UNIQUE(student_id, course_id)
```

---

## 🚀 Как запустить проект

### 1. Убедитесь, что PostgreSQL запущен
```sql
-- В pgAdmin или psql проверьте:
SELECT version();
```

### 2. Запустите приложение
```bash
cd C:\jakarta\university
start.bat
```

### 3. Откройте браузер
```
http://localhost:8080
```

---

## 🔑 Как войти

### Администратор:
```
Email: admin@university.kz
Пароль: admin123
```

### Студент:
```
Email: asel@student.kz
Пароль: 123456
```

---

## 🛠️ Как добавить новые данные

### Через админ-панель (веб-интерфейс):
1. Войти как администратор
2. Открыть `/admin/dashboard`
3. Выбрать "Университеты", "Преподаватели" или "Курсы"
4. Нажать "Добавить"
5. Заполнить форму
6. Сохранить

### Через REST API (Postman/curl):

#### Создать университет:
```bash
POST http://localhost:8080/api/universities
Content-Type: application/json

{
  "name": "Новый Университет",
  "location": "Алматы",
  "website": "https://new-uni.kz"
}
```

#### Создать курс:
```bash
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "name": "Новый курс",
  "code": "CS999",
  "description": "Описание курса",
  "credits": 5,
  "semester": "Spring 2026",
  "university": {"id": 1},
  "professor": {"id": 1}
}
```

---

## 📖 Документация

### Основные файлы:
- **README.md** - описание проекта, возможности, инструкции
- **DOCUMENTATION.md** - техническая документация, архитектура, API
- **AUTHENTICATION_SETUP_COMPLETE.md** - описание системы безопасности

### Где найти примеры:
- REST API примеры: `DOCUMENTATION.md` секция "API Reference"
- SQL схема: `DOCUMENTATION.md` секция "База данных"
- Конфигурация безопасности: `README.md` секция "Безопасность"

---

## 🎯 Основные URL приложения

### Публичные:
- `http://localhost:8080/` - главная страница
- `http://localhost:8080/catalog` - каталог курсов
- `http://localhost:8080/about` - о проекте
- `http://localhost:8080/auth/login` - вход
- `http://localhost:8080/auth/register` - регистрация

### Админ-панель (только ADMIN):
- `http://localhost:8080/admin/dashboard` - главная админки
- `http://localhost:8080/admin/universities` - управление университетами
- `http://localhost:8080/admin/professors` - управление преподавателями
- `http://localhost:8080/admin/courses` - управление курсами

### Студенческий кабинет:
- `http://localhost:8080/student/dashboard` - личный кабинет

### REST API:
- `http://localhost:8080/api/universities` - API университетов
- `http://localhost:8080/api/professors` - API преподавателей
- `http://localhost:8080/api/courses` - API курсов
- `http://localhost:8080/api/enrollments` - API регистраций

### Документация:
- `http://localhost:8080/api-docs` - документация API

---

## 🔍 Поиск в коде

### Если нужно найти, где что делается:

**Аутентификация:**
- `AuthController.java` - логика входа/регистрации
- `SecurityConfig.java` - настройки безопасности
- `CustomUserDetailsService.java` - загрузка пользователя

**CRUD операции:**
- `*Controller.java` в папке `controller/` - REST API
- `AdminController.java` - веб-страницы админки
- `*Service.java` в папке `service/` - бизнес-логика
- `*Repository.java` в папке `repository/` - запросы к БД

**HTML страницы:**
- Все `.html` файлы в `src/main/resources/templates/`

**Конфигурация:**
- `application.yml` - настройки приложения
- `SecurityConfig.java` - безопасность
- `DataInitializer.java` - тестовые данные

---

## ✅ Чек-лист реализованного

- [x] Spring Boot 3.4.0 приложение
- [x] PostgreSQL база данных
- [x] Spring Security аутентификация
- [x] CustomUserDetailsService
- [x] BCrypt хеширование паролей
- [x] Форма входа и регистрации
- [x] Управление сессиями
- [x] Разделение по ролям (ADMIN, STUDENT)
- [x] CRUD университетов
- [x] CRUD преподавателей
- [x] CRUD курсов
- [x] REST API для всех сущностей
- [x] Веб-интерфейс (Thymeleaf)
- [x] Админ-панель
- [x] Студенческий кабинет
- [x] Каталог курсов
- [x] Регистрация на курсы (Enrollments)
- [x] Автозагрузка тестовых данных
- [x] Документация
- [x] Git репозиторий

---

## 🎓 ИТОГО

Проект полностью настроен и готов к использованию!

Все компоненты работают вместе:
- PostgreSQL хранит данные
- Spring Security защищает доступ
- REST API позволяет работать с данными программно
- Веб-интерфейс позволяет работать через браузер
- Документация объясняет, как всё работает

**Удачи в разработке! 🚀**

