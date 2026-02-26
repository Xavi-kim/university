# Итоговая документация проекта

## ✅ Что было реализовано

### 1. Модели данных (Entity)
- ✅ **University** - Университеты с полями: название, адрес, город, страна, сайт, описание
- ✅ **Professor** - Преподаватели с полями: имя, email, отделение, биография, связь с университетом
- ✅ **Course** - Курсы с полями: название, описание, отделение, семестр, связь с преподавателем и университетом
- ✅ **User** - Пользователи (для будущей авторизации)

### 2. Связи между сущностями
- ✅ University → Professor (One-to-Many)
- ✅ University → Course (One-to-Many)
- ✅ Professor → Course (One-to-Many)

### 3. Репозитории (JPA)
- ✅ UniversityRepository - с методами поиска по городу, стране, названию
- ✅ ProfessorRepository - с методами поиска по отделению, университету, email
- ✅ CourseRepository - с методами поиска по отделению, преподавателю, университету
- ✅ UserRepository - для управления пользователями

### 4. Сервисы (Business Logic)
- ✅ UniversityService - полный CRUD + поиск + статус
- ✅ ProfessorService - полный CRUD + поиск + статус
- ✅ CourseService - полный CRUD + поиск + статус
- ✅ UserService - управление пользователями

### 5. REST API Контроллеры
- ✅ **UniversityController** - 11 эндпоинтов
- ✅ **ProfessorController** - 11 эндпоинтов
- ✅ **CourseController** - 9 эндпоинтов
- ✅ **MainController** - 5 эндпоинтов
- ✅ **HomeController** - 4 страницы
- ✅ **UserController** - управление пользователями

### 6. Конфигурация
- ✅ **SecurityConfig** - открытый доступ для разработки
- ✅ **DataInitializer** - автоматическая загрузка тестовых данных
- ✅ **application.yml** - настройки приложения

### 7. Тестовые данные
- ✅ 3 университета (КазНУ, ЕНУ, КБТУ)
- ✅ 5 преподавателей по разным специальностям
- ✅ 7 курсов по различным дисциплинам

### 8. Лабораторная работа №3
- ✅ Реализованы методы doGet() и doPost() (через REST контроллеры)
- ✅ Добавлены множественные параметры
- ✅ Данные выводятся в JSON формате
- ✅ Современный подход вместо классических сервлетов

## 🎯 Структура API

### Всего эндпоинтов: 40+

#### Universities API (11 эндпоинтов)
```
GET    /api/universities                      - Все активные
GET    /api/universities/all                  - Все (с неактивными)
GET    /api/universities/{id}                 - По ID
GET    /api/universities/name/{name}          - По названию
GET    /api/universities/city/{city}          - По городу
GET    /api/universities/country/{country}    - По стране
GET    /api/universities/search?name=NAME     - Поиск
POST   /api/universities                      - Создать
PUT    /api/universities/{id}                 - Обновить
DELETE /api/universities/{id}                 - Удалить
PATCH  /api/universities/{id}/status          - Изменить статус
```

#### Professors API (11 эндпоинтов)
```
GET    /api/professors                              - Все активные
GET    /api/professors/all                          - Все (с неактивными)
GET    /api/professors/{id}                         - По ID
GET    /api/professors/email/{email}                - По email
GET    /api/professors/department/{department}      - По отделению
GET    /api/professors/university/{universityId}    - По университету
GET    /api/professors/search?name=NAME             - Поиск
POST   /api/professors                              - Создать
PUT    /api/professors/{id}                         - Обновить
DELETE /api/professors/{id}                         - Удалить
PATCH  /api/professors/{id}/status                  - Изменить статус
```

#### Courses API (9 эндпоинтов)
```
GET    /api/courses                              - Все активные
GET    /api/courses/all                          - Все (с неактивными)
GET    /api/courses/{id}                         - По ID
GET    /api/courses/department/{department}      - По отделению
GET    /api/courses/professor/{professorId}      - По преподавателю
GET    /api/courses/university/{universityId}    - По университету
POST   /api/courses                              - Создать
PUT    /api/courses/{id}                         - Обновить
DELETE /api/courses/{id}                         - Удалить
PATCH  /api/courses/{id}/status                  - Изменить статус
```

#### Main API (5 эндпоинтов)
```
GET    /api/main                     - Приветствие
GET    /api/main/info                - Информация об API
GET    /api/main/special             - Данные студента
POST   /api/main/special?name=NAME   - Создать студента
POST   /api/main/serialize           - Сериализация
```

## 📊 База данных

### Схема H2 Database (In-Memory)

```sql
-- Таблица университетов
CREATE TABLE universities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    description TEXT,
    active BOOLEAN DEFAULT TRUE
);

-- Таблица преподавателей
CREATE TABLE professors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    department VARCHAR(255) NOT NULL,
    bio TEXT,
    university_id BIGINT,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (university_id) REFERENCES universities(id)
);

-- Таблица курсов
CREATE TABLE courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    department VARCHAR(255) NOT NULL,
    semester VARCHAR(255) NOT NULL,
    professor_id BIGINT,
    university_id BIGINT,
    active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (professor_id) REFERENCES professors(id),
    FOREIGN KEY (university_id) REFERENCES universities(id)
);
```

## 🚀 Как запустить и протестировать

### 1. Запуск приложения

```powershell
# В PowerShell
$env:JAVA_HOME="C:\Program Files\Java\jdk-24"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
cd C:\jakarta\university
.\mvnw.cmd spring-boot:run
```

### 2. Проверка работы

Откройте браузер:
- http://localhost:8080/ - Главная страница
- http://localhost:8080/api/main/info - Информация об API
- http://localhost:8080/h2-console - H2 Database Console

### 3. Тестирование через Postman

#### Пример 1: Получить все университеты
```
GET http://localhost:8080/api/universities
```

Ответ:
```json
[
  {
    "id": 1,
    "name": "Казахский Национальный Университет им. аль-Фараби",
    "address": "пр. аль-Фараби, 71",
    "city": "Алматы",
    "country": "Казахстан",
    "website": "https://www.kaznu.kz",
    "description": "Ведущий университет Казахстана...",
    "active": true
  },
  ...
]
```

#### Пример 2: Получить преподавателей университета
```
GET http://localhost:8080/api/professors/university/1
```

#### Пример 3: Создать новый курс
```
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "title": "Введение в Python",
  "description": "Базовый курс по программированию на Python",
  "department": "Программирование",
  "semester": "Весна 2025",
  "professor": { "id": 1 },
  "university": { "id": 1 }
}
```

### 4. Тестирование через браузер

Откройте в браузере:
```
http://localhost:8080/api/universities
http://localhost:8080/api/professors
http://localhost:8080/api/courses
```

## 📝 Что изучено

### 1. Spring Boot архитектура
- ✅ Разделение на слои: Model → Repository → Service → Controller
- ✅ Dependency Injection через @Autowired
- ✅ Конфигурация через аннотации
- ✅ REST API разработка

### 2. JPA / Hibernate
- ✅ Entity маппинг (@Entity, @Table)
- ✅ Связи между таблицами (@ManyToOne, @OneToMany)
- ✅ Автоматическое создание схемы БД
- ✅ CRUD операции через JpaRepository

### 3. REST API принципы
- ✅ HTTP методы: GET, POST, PUT, DELETE, PATCH
- ✅ Статус коды: 200, 201, 204, 404
- ✅ JSON сериализация/десериализация
- ✅ CORS конфигурация

### 4. Валидация данных
- ✅ @NotBlank, @Email аннотации
- ✅ @Valid для автоматической проверки
- ✅ Обработка ошибок валидации

### 5. Современные подходы
- ✅ REST вместо классических Servlet
- ✅ JSON вместо HTML форм
- ✅ Автоматическая инициализация данных
- ✅ In-Memory база данных для разработки

## 🎓 Сравнение с классическими Servlet

### Старый подход (Servlet):
```java
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Hello, " + name + "</h1>");
    }
}
```

### Современный подход (Spring REST):
```java
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
```

## 🔥 Преимущества реализованного подхода

1. **Автоматическая сериализация** - Spring автоматически преобразует объекты в JSON
2. **Типизация** - используются строго типизированные объекты вместо строк
3. **Валидация** - автоматическая проверка данных
4. **Связи** - автоматическая работа с foreign keys
5. **Транзакции** - автоматическое управление
6. **Безопасность** - встроенная защита
7. **Масштабируемость** - легко добавлять новые сущности

## 📦 Файлы проекта

```
university/
├── README_API.md              - Полная документация API
├── QUICK_START.md             - Быстрый старт
├── POSTMAN_EXAMPLES.md        - Примеры запросов
├── SUMMARY.md                 - Итоговая документация (этот файл)
├── pom.xml                    - Maven конфигурация
└── src/
    └── main/
        ├── java/
        │   └── org/example/university/
        │       ├── Application.java
        │       ├── model/              - 4 Entity класса
        │       ├── repository/         - 4 Repository интерфейса
        │       ├── service/            - 4 Service класса
        │       ├── controller/         - 6 Controller классов
        │       ├── config/             - 2 конфигурационных класса
        │       └── dto/                - 1 DTO класс
        └── resources/
            ├── application.yml         - Настройки приложения
            └── templates/              - 4 HTML шаблона
```

## ✅ Результат

Создана полнофункциональная система управления университетом с:
- **40+ REST API эндпоинтами**
- **4 связанными сущностями**
- **Автоматической инициализацией данных**
- **Полной документацией**
- **Примерами использования**

Проект готов к демонстрации и дальнейшему развитию! 🎉

