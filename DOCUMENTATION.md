# 📚 Документация проекта University Management System

## 📖 Оглавление

1. [Обзор проекта](#обзор-проекта)
2. [Архитектура](#архитектура)
3. [Структура проекта](#структура-проекта)
4. [Компоненты системы](#компоненты-системы)
5. [Настройка и запуск](#настройка-и-запуск)
6. [API Reference](#api-reference)
7. [База данных](#база-данных)
8. [Безопасность](#безопасность)

---

## 🎯 Обзор проекта

**University Management System** - это современное веб-приложение для управления университетом, построенное на Spring Boot.

### Основные возможности:

- ✅ **Аутентификация и авторизация** (Spring Security + BCrypt)
- ✅ **Управление пользователями** (студенты, преподаватели, администраторы)
- ✅ **Управление курсами** (создание, редактирование, удаление)
- ✅ **Управление университетами** (мультитенантность)
- ✅ **Управление преподавателями**
- ✅ **Регистрация на курсы** (Enrollments)
- ✅ **REST API** для всех операций
- ✅ **Веб-интерфейс** (Thymeleaf templates)
- ✅ **Красивый UI** с современным дизайном

---

## 🏗 Архитектура

Проект построен по **многослойной архитектуре**:

```
┌─────────────────────────────────────┐
│   Presentation Layer (Controllers)   │
│   - REST Controllers (/api/*)       │
│   - MVC Controllers (страницы)      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Service Layer (Business)       │
│   - UserService                     │
│   - CourseService                   │
│   - UniversityService               │
│   - ProfessorService                │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│   Data Access Layer (Repository)    │
│   - JPA Repositories                │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Database (PostgreSQL)        │
└─────────────────────────────────────┘
```

---

## 📁 Структура проекта

```
university/
│
├── src/main/java/org/example/university/
│   │
│   ├── Application.java                     # Точка входа приложения
│   │
│   ├── config/                              # Конфигурации
│   │   ├── SecurityConfig.java             # Настройки безопасности
│   │   └── DataInitializer.java            # Инициализация тестовых данных
│   │
│   ├── controller/                          # Контроллеры
│   │   ├── AuthController.java             # Аутентификация (login/register)
│   │   ├── HomeController.java             # Главные страницы (/, /catalog)
│   │   ├── AdminController.java            # Админ-панель (/admin/*)
│   │   ├── StudentController.java          # Студенческий кабинет (/student/*)
│   │   ├── UserController.java             # REST API пользователей
│   │   ├── CourseController.java           # REST API курсов
│   │   ├── UniversityController.java       # REST API университетов
│   │   └── ProfessorController.java        # REST API преподавателей
│   │
│   ├── model/                               # Модели данных (Entity)
│   │   ├── User.java                       # Пользователь
│   │   ├── Course.java                     # Курс
│   │   ├── University.java                 # Университет
│   │   ├── Professor.java                  # Преподаватель
│   │   └── Enrollment.java                 # Регистрация на курс
│   │
│   ├── repository/                          # Репозитории (JPA)
│   │   ├── UserRepository.java
│   │   ├── CourseRepository.java
│   │   ├── UniversityRepository.java
│   │   ├── ProfessorRepository.java
│   │   └── EnrollmentRepository.java
│   │
│   └── service/                             # Сервисы (бизнес-логика)
│       ├── UserService.java
│       ├── CourseService.java
│       ├── UniversityService.java
│       ├── ProfessorService.java
│       ├── EnrollmentService.java
│       └── CustomUserDetailsService.java   # Spring Security UserDetailsService
│
├── src/main/resources/
│   ├── application.yml                      # Конфигурация приложения
│   ├── templates/                           # HTML шаблоны (Thymeleaf)
│   │   ├── index.html                      # Главная страница
│   │   ├── login.html                      # Страница входа
│   │   ├── register.html                   # Страница регистрации
│   │   ├── catalog.html                    # Каталог курсов
│   │   ├── dashboard.html                  # Дашборд
│   │   ├── admin-dashboard.html            # Админ-панель
│   │   ├── student-dashboard.html          # Студенческий кабинет
│   │   ├── admin-course-form.html          # Форма курса
│   │   ├── admin-professor-form.html       # Форма преподавателя
│   │   ├── admin-university-form.html      # Форма университета
│   │   ├── api-docs.html                   # Документация API
│   │   └── about.html                      # О проекте
│   │
│   └── static/                              # Статические ресурсы
│       └── css/
│           └── styles.css                  # Стили
│
├── pom.xml                                  # Maven dependencies
├── .gitignore                               # Git ignore правила
└── README.md                                # Описание проекта
```

---

## 🧩 Компоненты системы

### 1. Модели данных (Entity)

#### User (Пользователь)
```java
@Entity
@Table(name = "users")
public class User {
    private Long id;
    private String name;
    private String email;          // используется как username
    private String password;       // хешируется BCrypt
    private String role;           // STUDENT, ADMIN, PROFESSOR
    private boolean enabled;
}
```

#### Course (Курс)
```java
@Entity
@Table(name = "courses")
public class Course {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer credits;
    private String semester;
    private University university;
    private Professor professor;
}
```

#### University (Университет)
```java
@Entity
@Table(name = "universities")
public class University {
    private Long id;
    private String name;
    private String location;
    private String website;
}
```

#### Professor (Преподаватель)
```java
@Entity
@Table(name = "professors")
public class Professor {
    private Long id;
    private String name;
    private String email;
    private String department;
    private University university;
}
```

#### Enrollment (Регистрация на курс)
```java
@Entity
@Table(name = "enrollments")
public class Enrollment {
    private Long id;
    private User student;
    private Course course;
    private LocalDateTime enrolledAt;
    private String status;  // ACTIVE, COMPLETED, DROPPED
}
```

---

### 2. Контроллеры

#### AuthController
**Путь:** `/auth`

**Методы:**
- `GET /auth/login` - страница входа
- `POST /auth/login` - обработка входа
- `GET /auth/register` - страница регистрации
- `POST /auth/register` - обработка регистрации
- `GET /auth/logout` - выход

#### HomeController
**Путь:** `/`

**Методы:**
- `GET /` - главная страница
- `GET /catalog` - каталог курсов
- `GET /about` - о проекте
- `GET /api-docs` - документация API

#### AdminController
**Путь:** `/admin`

**Доступ:** только для роли `ADMIN`

**Методы:**
- `GET /admin/dashboard` - админ-панель
- `GET /admin/courses` - управление курсами
- `GET /admin/courses/new` - создание курса
- `GET /admin/courses/edit/{id}` - редактирование курса
- `POST /admin/courses/delete/{id}` - удаление курса
- `GET /admin/professors` - управление преподавателями
- `GET /admin/universities` - управление университетами

#### REST API Controllers

**UserController** - `/api/users`
- `GET /api/users` - список всех пользователей
- `GET /api/users/{id}` - получить пользователя
- `POST /api/users` - создать пользователя
- `PUT /api/users/{id}` - обновить пользователя
- `DELETE /api/users/{id}` - удалить пользователя

**CourseController** - `/api/courses`
- `GET /api/courses` - список всех курсов
- `GET /api/courses/{id}` - получить курс
- `POST /api/courses` - создать курс
- `PUT /api/courses/{id}` - обновить курс
- `DELETE /api/courses/{id}` - удалить курс

**UniversityController** - `/api/universities`
- `GET /api/universities` - список университетов
- `GET /api/universities/{id}` - получить университет
- `POST /api/universities` - создать университет
- `PUT /api/universities/{id}` - обновить университет
- `DELETE /api/universities/{id}` - удалить университет

**ProfessorController** - `/api/professors`
- `GET /api/professors` - список преподавателей
- `GET /api/professors/{id}` - получить преподавателя
- `POST /api/professors` - создать преподавателя
- `PUT /api/professors/{id}` - обновить преподавателя
- `DELETE /api/professors/{id}` - удалить преподавателя

---

### 3. Сервисы

#### UserService
Бизнес-логика для работы с пользователями:
- Создание, обновление, удаление пользователей
- Поиск по email
- Проверка существования
- Управление ролями

#### CustomUserDetailsService
Spring Security сервис для аутентификации:
- Загрузка пользователя по email
- Создание `UserDetails` для Spring Security

#### CourseService
Управление курсами:
- CRUD операции
- Поиск по университету, преподавателю
- Фильтрация по семестру

#### UniversityService
Управление университетами

#### ProfessorService
Управление преподавателями

#### EnrollmentService
Управление регистрациями на курсы

---

### 4. Конфигурация безопасности (SecurityConfig)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        // Открытые URL: /, /auth/*, /api/*
        // Защищенные URL: /admin/* (только ADMIN)
        // Остальные: требуют аутентификации
    }
}
```

**Защита:**
- Пароли хешируются с помощью **BCrypt**
- Сессии управляются Spring Security
- CSRF защита отключена для API
- CORS настроен для кросс-доменных запросов

---

## ⚙️ Настройка и запуск

### 1. Требования

- **Java 24**
- **Maven 3.x**
- **PostgreSQL 12+**

### 2. Настройка базы данных

Создайте базу данных PostgreSQL:

```sql
CREATE DATABASE university_db;
```

### 3. Конфигурация

Отредактируйте `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/university_db
    username: postgres
    password: ваш_пароль
    
  jpa:
    hibernate:
      ddl-auto: update  # create | update | validate
```

### 4. Запуск приложения

**Windows:**
```batch
start.bat
```

**Linux/Mac:**
```bash
./run.sh
```

**Maven:**
```bash
mvn clean install
mvn spring-boot:run
```

### 5. Доступ к приложению

Откройте браузер: **http://localhost:8080**

---

## 🔐 Безопасность

### Аутентификация

- **Метод:** Form-based authentication (Spring Security)
- **Сессии:** HTTP Session с cookie JSESSIONID
- **Пароли:** Хешируются с BCrypt (сила 10)

### Роли пользователей

| Роль | Описание | Доступ |
|------|----------|--------|
| **STUDENT** | Студент | Просмотр курсов, регистрация на курсы |
| **PROFESSOR** | Преподаватель | Просмотр своих курсов |
| **ADMIN** | Администратор | Полный доступ ко всем функциям |

### Защищенные URL

| URL | Доступ |
|-----|--------|
| `/`, `/auth/*`, `/api/*` | Публичные |
| `/admin/*` | Только ADMIN |
| `/student/*` | Аутентифицированные |

---

## 🗄️ База данных

### Схема PostgreSQL

```sql
-- Таблица пользователей
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true
);

-- Таблица университетов
CREATE TABLE universities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    website VARCHAR(255)
);

-- Таблица преподавателей
CREATE TABLE professors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(255),
    university_id BIGINT,
    FOREIGN KEY (university_id) REFERENCES universities(id)
);

-- Таблица курсов
CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    credits INTEGER,
    semester VARCHAR(50),
    university_id BIGINT,
    professor_id BIGINT,
    FOREIGN KEY (university_id) REFERENCES universities(id),
    FOREIGN KEY (professor_id) REFERENCES professors(id)
);

-- Таблица регистраций
CREATE TABLE enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    UNIQUE(student_id, course_id)
);
```

---

## 📝 Тестовые данные

При первом запуске автоматически создаются тестовые данные:

**Администратор:**
- Email: `admin@university.com`
- Пароль: `admin123`

**Студент:**
- Email: `student@university.com`
- Пароль: `student123`

---

## 🚀 API Reference

### Формат ответов

Все REST API возвращают JSON:

```json
{
  "id": 1,
  "name": "Spring Boot Course",
  "code": "CS101",
  "credits": 4
}
```

### Ошибки

```json
{
  "timestamp": "2024-03-07T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "Course not found",
  "path": "/api/courses/999"
}
```

---

## 📞 Контакты

Если у вас возникли вопросы или проблемы, создайте Issue в репозитории.

---

## 📜 Лицензия

MIT License

