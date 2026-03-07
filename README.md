# 🎓 University Management System

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen?logo=springboot)
![Java](https://img.shields.io/badge/Java-24-orange?logo=openjdk)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL%2016-blue?logo=postgresql)
![Spring Security](https://img.shields.io/badge/Spring%20Security-✓-brightgreen?logo=springsecurity)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-green?logo=thymeleaf)
![License](https://img.shields.io/badge/License-MIT-blue)
![Status](https://img.shields.io/badge/Status-🟢%20Active-success)
![Version](https://img.shields.io/badge/Version-1.0--SNAPSHOT-yellow)

**Полнофункциональная платформа для управления университетом**  
*Вдохновлена Canvas LMS · Moodle · Coursera · Google Classroom*

[🚀 Быстрый старт](#-быстрый-старт) · [✨ Возможности](#-возможности) · [📁 Структура](#-структура-проекта) · [🔌 API](#-rest-api) · [🔐 Безопасность](#-безопасность) · [📖 Документация](DOCUMENTATION.md)

</div>

---

## 📋 О проекте

**University Management System (UMS)** — современная веб-платформа для автоматизации учебного процесса с полноценной системой аутентификации и авторизации.

```
┌──────────────────────────────────────────────────────────────┐
│               University Management System                    │
│                                                              │
│   👑 Администратор              🎓 Студент                   │
│   ├── Добавление университетов  ├── Запись на курсы          │
│   ├── Управление преподавателями├── Просмотр своих курсов    │
│   ├── CRUD курсов               └── Личный кабинет          │
│   └── Статистика системы                                     │
│                                                              │
│   🌐 Каталог  ·  🔐 Auth  ·  🗄 PostgreSQL  ·  🔌 REST API  │
│   ✅ Spring Security  ·  🔒 BCrypt  ·  🎫 Session Management │
└──────────────────────────────────────────────────────────────┘
```

---

## 🎯 Ключевые особенности

✅ **Полная аутентификация и авторизация**
- Регистрация пользователей с валидацией
- Вход через форму (Form-based authentication)
- Хеширование паролей с BCrypt
- Управление сессиями через Spring Security
- Разделение доступа по ролям (ADMIN, STUDENT, PROFESSOR)

✅ **Современный веб-интерфейс**
- Красивые HTML страницы с Thymeleaf
- Адаптивный дизайн
- Интерактивные формы с валидацией
- Удобная навигация

✅ **REST API**
- CRUD операции для всех сущностей
- JSON формат данных
- CORS конфигурация
- Документированные эндпоинты

✅ **Безопасность**
- Spring Security integration
- CustomUserDetailsService для загрузки пользователей из БД
- PasswordEncoder (BCrypt)
- Защита от несанкционированного доступа

---

## ✨ Возможности

### 🔐 Система аутентификации

| Функция | Описание | Статус |
|---------|----------|--------|
| Регистрация | Создание нового аккаунта с проверкой email | ✅ |
| Вход | Аутентификация через email/password | ✅ |
| Выход | Завершение сессии | ✅ |
| Хеширование паролей | BCrypt (сила 10) | ✅ |
| Управление сессиями | Spring Security Session Management | ✅ |
| Разделение по ролям | ADMIN, STUDENT, PROFESSOR | ✅ |

### 🏛 Управление данными (Админ-панель)

| Сущность | Добавить | Редактировать | Удалить | Просмотр |
|----------|----------|---------------|---------|----------|
| Университеты | ✅ | ✅ | ✅ | ✅ |
| Преподаватели | ✅ | ✅ | ✅ | ✅ |
| Курсы | ✅ | ✅ | ✅ | ✅ |

### 🔐 Авторизация
| Функция | Описание |
|---------|----------|
| Регистрация | По email и паролю |
| Вход / Выход | Сессионная аутентификация |
| BCrypt | Шифрование паролей |
| RBAC | Роли: `ADMIN` / `STUDENT` |

### 🌐 Публичный каталог
- Просмотр всех курсов с фильтрацией и поиском
- Карточки преподавателей с биографией
- Список университетов с описанием и ссылками
- Вкладки: Курсы / Преподаватели / Университеты

---

## 🚀 Быстрый старт

### Требования
- Java 24+
- PostgreSQL 16
- Maven (встроен через `mvnw`)

### 1. Настройка базы данных
```sql
-- В PostgreSQL создайте базу данных:
CREATE DATABASE university_db;
```

### 2. Настройка подключения
Файл: `src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/university_db
    username: postgres
    password: ВАШ_ПАРОЛЬ
```

### 3. Сборка и запуск
```powershell
# Windows
.\mvnw.cmd clean package -DskipTests
java --enable-native-access=ALL-UNNAMED -jar target\university-1.0-SNAPSHOT.jar
```

```bash
# Linux / Mac
./mvnw clean package -DskipTests
java -jar target/university-1.0-SNAPSHOT.jar
```

### 4. Открыть в браузере
```
http://localhost:8080
```

> При первом запуске автоматически загружаются тестовые данные:  
> 3 университета · 5 преподавателей · 7 курсов · 3 пользователя

---

## 🔑 Тестовые аккаунты

| Роль | Email | Пароль |
|------|-------|--------|
| 👑 Администратор | `admin@university.kz` | `admin123` |
| 👨‍🎓 Студент | `asel@student.kz` | `123456` |
| 👨‍🎓 Студент | `erlan@student.kz` | `123456` |

---

## 🌐 Страницы системы

| URL | Доступ | Описание |
|-----|--------|---------|
| `/` | Все | Главная страница |
| `/catalog` | Все | Каталог курсов, преподавателей, университетов |
| `/auth/login` | Все | Вход в систему |
| `/auth/register` | Все | Регистрация |
| `/admin/dashboard` | ADMIN | Панель администратора |
| `/admin/universities/new` | ADMIN | Добавить университет |
| `/admin/professors/new` | ADMIN | Добавить преподавателя |
| `/admin/courses/new` | ADMIN | Добавить курс |
| `/student/dashboard` | STUDENT | Личный кабинет студента |

---

## 📁 Структура проекта

```
src/main/
├── java/org/example/university/
│   ├── Application.java              ← Точка входа, авто-открытие браузера
│   ├── config/
│   │   ├── SecurityConfig.java       ← Настройки безопасности
│   │   └── DataInitializer.java      ← Загрузка тестовых данных
│   ├── controller/
│   │   ├── AdminController.java      ← /admin/** (CRUD всего)
│   │   ├── AuthController.java       ← /auth/** (вход/выход/регистрация)
│   │   ├── HomeController.java       ← / /catalog /browse
│   │   ├── CourseController.java     ← /api/courses/**
│   │   ├── ProfessorController.java  ← /api/professors/**
│   │   ├── UniversityController.java ← /api/universities/**
│   │   ├── EnrollmentController.java ← /api/enrollments/**
│   │   └── StudentController.java    ← /student/**
│   ├── model/
│   │   ├── University.java           ← Таблица universities
│   │   ├── Professor.java            ← Таблица professors
│   │   ├── Course.java               ← Таблица courses
│   │   ├── User.java                 ← Таблица users
│   │   └── Enrollment.java           ← Таблица enrollments
│   ├── repository/                   ← JPA репозитории (запросы к БД)
│   └── service/                      ← Бизнес-логика
└── resources/
    ├── application.yml               ← Конфигурация
    └── templates/
        ├── index.html                ← Главная страница
        ├── catalog.html              ← Каталог (курсы/препода/вузы)
        ├── admin-dashboard.html      ← Панель администратора
        ├── admin-course-form.html    ← Форма курса
        ├── admin-professor-form.html ← Форма преподавателя
        ├── admin-university-form.html← Форма университета
        ├── login.html                ← Вход
        ├── register.html             ← Регистрация
        └── student-dashboard.html   ← Кабинет студента
```

---

## 🗄️ База данных

**СУБД:** PostgreSQL 16  
**Схема создаётся автоматически** через Hibernate (`ddl-auto: update`)

```
universities ──< professors ──< courses ──< enrollments >── users
```

| Таблица | Назначение |
|---------|-----------|
| `universities` | Учебные заведения |
| `professors` | Преподаватели (linked → university) |
| `courses` | Курсы (linked → professor, university) |
| `users` | Пользователи (ADMIN / STUDENT) |
| `enrollments` | Записи студентов на курсы |

---

## 🔌 REST API

<details>
<summary>📚 Курсы</summary>

| Метод | URL | Описание |
|-------|-----|---------|
| GET | `/api/courses` | Все курсы |
| GET | `/api/courses/{id}` | Курс по ID |
| POST | `/api/courses` | Создать курс |
| PUT | `/api/courses/{id}` | Обновить курс |
| DELETE | `/api/courses/{id}` | Удалить курс |

</details>

<details>
<summary>👨‍🏫 Преподаватели</summary>

| Метод | URL | Описание |
|-------|-----|---------|
| GET | `/api/professors` | Все преподаватели |
| GET | `/api/professors/{id}` | Преподаватель по ID |
| POST | `/api/professors` | Создать преподавателя |
| PUT | `/api/professors/{id}` | Обновить |
| DELETE | `/api/professors/{id}` | Удалить |

</details>

<details>
<summary>🏛 Университеты</summary>

| Метод | URL | Описание |
|-------|-----|---------|
| GET | `/api/universities` | Все университеты |
| GET | `/api/universities/{id}` | По ID |
| POST | `/api/universities` | Создать |
| PUT | `/api/universities/{id}` | Обновить |
| DELETE | `/api/universities/{id}` | Удалить |

</details>

---

## 🔐 Безопасность

### Архитектура безопасности

```
┌──────────────────────────────────────────────────────┐
│              Spring Security Layer                    │
├──────────────────────────────────────────────────────┤
│  1. SecurityFilterChain - URL защита                 │
│  2. CustomUserDetailsService - загрузка из БД        │
│  3. DaoAuthenticationProvider - аутентификация       │
│  4. BCryptPasswordEncoder - хеширование              │
│  5. Session Management - управление сессиями         │
└──────────────────────────────────────────────────────┘
```

### Реализованные компоненты

#### 1. CustomUserDetailsService
Загружает пользователей из базы данных PostgreSQL для Spring Security:

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(...));
        
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole())
            .disabled(!user.isEnabled())
            .build();
    }
}
```

#### 2. SecurityConfig
Конфигурация безопасности с разделением доступа:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Сила 10
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/auth/**", "/api/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .invalidateHttpSession(true)
            );
    }
}
```

### Защищенные URL

| URL Pattern | Доступ | Описание |
|-------------|--------|----------|
| `/`, `/catalog`, `/about` | Публично | Главные страницы |
| `/auth/login`, `/auth/register` | Публично | Аутентификация |
| `/api/**` | Публично* | REST API (для разработки) |
| `/admin/**` | `ROLE_ADMIN` | Админ-панель |
| `/student/**` | Аутентифицированные | Студенческий кабинет |

*В production рекомендуется защитить API токенами (JWT)

### Хеширование паролей

- **Алгоритм:** BCrypt
- **Сила:** 10 (по умолчанию)
- **Пример:** `admin123` → `$2a$10$xn3LI/AjqicFYZFruSwve...`

### Управление сессиями

```java
.sessionManagement(session -> session
    .maximumSessions(1)              // Максимум 1 сессия на пользователя
    .maxSessionsPreventsLogin(false) // Новый вход завершает старую сессию
);
```

- **Тип сессии:** HTTP Session (JSESSIONID cookie)
- **Время жизни:** По умолчанию 30 минут без активности
- **Хранение:** In-memory (можно настроить Redis/JDBC)

### Защита от атак

| Угроза | Защита | Статус |
|--------|--------|--------|
| SQL Injection | JPA/Hibernate параметризованные запросы | ✅ |
| XSS | Thymeleaf auto-escaping | ✅ |
| CSRF | Включено для форм, отключено для API | ✅ |
| Session Fixation | Spring Security защита | ✅ |
| Password Storage | BCrypt hashing | ✅ |

### Роли и права доступа

| Роль | Возможности |
|------|-------------|
| **GUEST** (не авторизован) | Просмотр каталога, регистрация, вход |
| **STUDENT** | Запись на курсы, просмотр своих курсов, личный кабинет |
| **PROFESSOR** | (Будущая функциональность) Управление своими курсами, оценки |
| **ADMIN** | Полный доступ: CRUD всех сущностей, статистика, управление пользователями |

---

## 🛠️ Технологии

| Категория | Технология |
|-----------|-----------|
| Backend | Java 24, Spring Boot 3.4.0 |
| Security | Spring Security, BCrypt, CustomUserDetailsService |
| Database | PostgreSQL 16, Spring Data JPA, Hibernate |
| Frontend | Thymeleaf, HTML5, CSS3, JavaScript |
| Build | Maven, Spring Boot Maven Plugin |
| Connection Pool | HikariCP |

---

## 📖 Документация

- **[DOCUMENTATION.md](DOCUMENTATION.md)** - Полная техническая документация
  - Архитектура проекта
  - Описание всех компонентов
  - REST API Reference
  - Схема базы данных
  - Инструкции по настройке

---

## 🗺 Roadmap

### Реализовано ✅
- [x] Аутентификация и авторизация (Spring Security)
- [x] Регистрация пользователей с валидацией
- [x] CustomUserDetailsService для загрузки из БД
- [x] BCrypt хеширование паролей
- [x] Управление сессиями
- [x] Разделение доступа по ролям (ADMIN/STUDENT)
- [x] CRUD университетов, преподавателей, курсов
- [x] Публичный каталог с поиском
- [x] Подключение PostgreSQL
- [x] Автозагрузка тестовых данных

### Запланировано 🚧
- [ ] JWT токены для REST API
- [ ] OAuth2 интеграция (Google, GitHub)
- [ ] Загрузка фото преподавателей
- [ ] Email-уведомления при записи на курс
- [ ] Система оценок и успеваемости
- [ ] Расписание занятий
- [ ] Мобильная адаптация
- [ ] Docker / docker-compose
- [ ] Роль PROFESSOR с управлением своими курсами
- [ ] Система комментариев и отзывов

---

## 📄 Лицензия

MIT License © 2026 University Management System
