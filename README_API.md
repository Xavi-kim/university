# University Management System - REST API

## Описание проекта

Система управления университетом с полнофункциональным REST API для управления:
- **Университетами (Universities)** - информация об учебных заведениях
- **Преподавателями (Professors)** - данные о профессорско-преподавательском составе
- **Курсами (Courses)** - информация о курсах с привязкой к преподавателям и университетам

## Структура проекта

```
university/
├── src/main/java/org/example/university/
│   ├── model/              # Модели данных (Entity)
│   │   ├── Course.java         - Курсы
│   │   ├── Professor.java      - Преподаватели
│   │   ├── University.java     - Университеты
│   │   └── User.java           - Пользователи
│   ├── repository/         # Репозитории (JPA)
│   │   ├── CourseRepository.java
│   │   ├── ProfessorRepository.java
│   │   ├── UniversityRepository.java
│   │   └── UserRepository.java
│   ├── service/            # Бизнес-логика
│   │   ├── CourseService.java
│   │   ├── ProfessorService.java
│   │   ├── UniversityService.java
│   │   └── UserService.java
│   ├── controller/         # REST контроллеры
│   │   ├── CourseController.java
│   │   ├── ProfessorController.java
│   │   ├── UniversityController.java
│   │   ├── UserController.java
│   │   ├── MainController.java
│   │   └── HomeController.java
│   ├── config/             # Конфигурация
│   │   ├── SecurityConfig.java     - Настройки безопасности
│   │   └── DataInitializer.java   - Начальные данные
│   ├── dto/                # Data Transfer Objects
│   │   └── StudentDTO.java
│   └── Application.java    # Точка входа
└── src/main/resources/
    ├── application.yml     # Конфигурация приложения
    └── templates/          # Thymeleaf шаблоны
```

## Технологический стек

- **Java 24** - Язык программирования
- **Spring Boot 3.4.0** - Фреймворк
- **Spring Data JPA** - Работа с базой данных
- **Spring Security** - Безопасность (открытый доступ для разработки)
- **H2 Database** - Встроенная база данных
- **Hibernate** - ORM
- **Maven** - Система сборки
- **Thymeleaf** - Шаблонизатор

## Установка и запуск

### Предварительные требования

- Java Development Kit (JDK) 24
- Maven 3.9+ (или используйте встроенный mvnw)

### Шаг 1: Установка переменных окружения

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-24"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

### Шаг 2: Компиляция проекта

```powershell
cd C:\jakarta\university
.\mvnw.cmd clean package
```

### Шаг 3: Запуск приложения

```powershell
.\mvnw.cmd spring-boot:run
```

Или через JAR файл:

```powershell
java -jar target\university-1.0-SNAPSHOT.jar
```

### Шаг 4: Проверка работы

Откройте браузер и перейдите по адресу:
- **Главная страница**: http://localhost:8080/
- **API документация**: http://localhost:8080/api/main/info
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: _(оставьте пустым)_

## REST API Endpoints

### Курсы (Courses)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/api/courses` | Получить все активные курсы |
| GET | `/api/courses/all` | Получить все курсы |
| GET | `/api/courses/{id}` | Получить курс по ID |
| GET | `/api/courses/department/{department}` | Получить курсы по отделению |
| GET | `/api/courses/professor/{professorId}` | Получить курсы преподавателя |
| GET | `/api/courses/university/{universityId}` | Получить курсы университета |
| POST | `/api/courses` | Создать новый курс |
| PUT | `/api/courses/{id}` | Обновить курс |
| DELETE | `/api/courses/{id}` | Удалить курс |
| PATCH | `/api/courses/{id}/status?active=true` | Обновить статус курса |

### Преподаватели (Professors)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/api/professors` | Получить всех активных преподавателей |
| GET | `/api/professors/all` | Получить всех преподавателей |
| GET | `/api/professors/{id}` | Получить преподавателя по ID |
| GET | `/api/professors/email/{email}` | Получить преподавателя по email |
| GET | `/api/professors/department/{department}` | Получить преподавателей по отделению |
| GET | `/api/professors/university/{universityId}` | Получить преподавателей университета |
| GET | `/api/professors/search?name=NAME` | Поиск преподавателей |
| POST | `/api/professors` | Создать нового преподавателя |
| PUT | `/api/professors/{id}` | Обновить преподавателя |
| DELETE | `/api/professors/{id}` | Удалить преподавателя |
| PATCH | `/api/professors/{id}/status?active=true` | Обновить статус |

### Университеты (Universities)

| Метод | URL | Описание |
|-------|-----|----------|
| GET | `/api/universities` | Получить все активные университеты |
| GET | `/api/universities/all` | Получить все университеты |
| GET | `/api/universities/{id}` | Получить университет по ID |
| GET | `/api/universities/name/{name}` | Получить университет по названию |
| GET | `/api/universities/city/{city}` | Получить университеты по городу |
| GET | `/api/universities/country/{country}` | Получить университеты по стране |
| GET | `/api/universities/search?name=NAME` | Поиск университетов |
| POST | `/api/universities` | Создать новый университет |
| PUT | `/api/universities/{id}` | Обновить университет |
| DELETE | `/api/universities/{id}` | Удалить университет |
| PATCH | `/api/universities/{id}/status?active=true` | Обновить статус |

## Примеры использования API

### Получить все университеты

```bash
GET http://localhost:8080/api/universities
```

### Создать нового преподавателя

```bash
POST http://localhost:8080/api/professors
Content-Type: application/json

{
  "name": "Иван Иванов",
  "email": "ivan.ivanov@university.kz",
  "department": "Информатика",
  "bio": "Специалист по базам данных",
  "university": {
    "id": 1
  }
}
```

### Создать новый курс

```bash
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "title": "Веб-разработка",
  "description": "Курс по созданию веб-приложений",
  "department": "Программирование",
  "semester": "Осень 2024",
  "professor": {
    "id": 1
  },
  "university": {
    "id": 1
  }
}
```

## Тестовые данные

При запуске приложения автоматически загружаются тестовые данные:

- **3 университета**: КазНУ, ЕНУ, КБТУ
- **5 преподавателей** с разными специализациями
- **7 курсов** по различным дисциплинам

## Лабораторная работа №3

### Задание

1. ✅ Реализованы методы `doGet()` и `doPost()` в DataServlet
2. ✅ Добавлены дополнительные параметры (university, course, professor, department, semester)
3. ✅ Данные выводятся в браузер через HTML формы

### Реализация через современный подход

Вместо классических сервлетов использован современный REST API подход с Spring Boot:
- REST контроллеры вместо Servlet
- JSON вместо HTML форм (но формы также доступны через UI)
- JPA/Hibernate вместо ручной работы с БД
- Автоматическая валидация и сериализация

## Структура базы данных

### Таблица: universities
- id (PK)
- name (unique)
- address
- city
- country
- website
- description
- active

### Таблица: professors
- id (PK)
- name
- email (unique)
- department
- bio
- university_id (FK → universities)
- active

### Таблица: courses
- id (PK)
- title
- description
- department
- semester
- professor_id (FK → professors)
- university_id (FK → universities)
- active

### Таблица: users
- id (PK)
- name
- email (unique)
- password
- role
- enabled

## Связи между таблицами

- **University → Professor**: One-to-Many (один университет - много преподавателей)
- **University → Course**: One-to-Many (один университет - много курсов)
- **Professor → Course**: One-to-Many (один преподаватель - много курсов)

## Особенности реализации

1. **JSON сериализация**: Автоматическое преобразование объектов в JSON
2. **Циклические зависимости**: Решены через `@JsonIgnoreProperties`
3. **Валидация**: Аннотации `@NotBlank`, `@Email` и т.д.
4. **Транзакции**: Автоматическое управление через `@Transactional`
5. **Безопасность**: Открытый доступ для разработки (можно настроить авторизацию)

## Разработка

### Добавление новой сущности

1. Создайте модель в `model/`
2. Создайте репозиторий в `repository/`
3. Создайте сервис в `service/`
4. Создайте контроллер в `controller/`
5. Обновите `DataInitializer` для тестовых данных

### Тестирование API

Рекомендуемые инструменты:
- **Postman** - GUI тестирование
- **curl** - командная строка
- **Браузер** - для GET запросов

## Авторы

Проект разработан для изучения REST API, Spring Boot и JPA.

## Лицензия

Учебный проект.

