# Примеры REST API запросов для Postman

## Universities (Университеты)

### 1. Получить все университеты
```
GET http://localhost:8080/api/universities
```

### 2. Получить университет по ID
```
GET http://localhost:8080/api/universities/1
```

### 3. Создать новый университет
```
POST http://localhost:8080/api/universities
Content-Type: application/json

{
  "name": "Назарбаев Университет",
  "address": "Кабанбай батыра, 53",
  "city": "Астана",
  "country": "Казахстан",
  "website": "https://nu.edu.kz",
  "description": "Исследовательский университет мирового уровня"
}
```

### 4. Обновить университет
```
PUT http://localhost:8080/api/universities/1
Content-Type: application/json

{
  "name": "КазНУ им. аль-Фараби",
  "address": "пр. аль-Фараби, 71",
  "city": "Алматы",
  "country": "Казахстан",
  "website": "https://www.kaznu.kz",
  "description": "Обновленное описание"
}
```

### 5. Поиск университетов
```
GET http://localhost:8080/api/universities/search?name=Казахстан
```

## Professors (Преподаватели)

### 1. Получить всех преподавателей
```
GET http://localhost:8080/api/professors
```

### 2. Создать нового преподавателя
```
POST http://localhost:8080/api/professors
Content-Type: application/json

{
  "name": "Алия Нурова",
  "email": "aliya.nurova@kaznu.kz",
  "department": "Физика",
  "bio": "Доцент кафедры теоретической физики",
  "university": {
    "id": 1
  }
}
```

### 3. Получить преподавателей по университету
```
GET http://localhost:8080/api/professors/university/1
```

### 4. Получить преподавателей по отделению
```
GET http://localhost:8080/api/professors/department/Информатика
```

### 5. Поиск преподавателей по имени
```
GET http://localhost:8080/api/professors/search?name=Айгуль
```

### 6. Обновить преподавателя
```
PUT http://localhost:8080/api/professors/1
Content-Type: application/json

{
  "name": "Айгуль Нурбекова",
  "email": "aigul.nurbekova@kaznu.kz",
  "department": "Информатика",
  "bio": "Обновленная биография: профессор, доктор технических наук",
  "university": {
    "id": 1
  }
}
```

## Courses (Курсы)

### 1. Получить все курсы
```
GET http://localhost:8080/api/courses
```

### 2. Создать новый курс
```
POST http://localhost:8080/api/courses
Content-Type: application/json

{
  "title": "Облачные вычисления",
  "description": "Изучение основ облачных технологий: AWS, Azure, Google Cloud",
  "department": "Информатика",
  "semester": "Весна 2025",
  "professor": {
    "id": 1
  },
  "university": {
    "id": 1
  }
}
```

### 3. Получить курсы по преподавателю
```
GET http://localhost:8080/api/courses/professor/1
```

### 4. Получить курсы по университету
```
GET http://localhost:8080/api/courses/university/1
```

### 5. Получить курсы по отделению
```
GET http://localhost:8080/api/courses/department/Информатика
```

### 6. Обновить курс
```
PUT http://localhost:8080/api/courses/1
Content-Type: application/json

{
  "title": "Искусственный интеллект и машинное обучение",
  "description": "Обновленное описание курса",
  "department": "Информатика",
  "semester": "Осень 2024",
  "professor": {
    "id": 1
  },
  "university": {
    "id": 1
  }
}
```

### 7. Деактивировать курс
```
PATCH http://localhost:8080/api/courses/1/status?active=false
```

### 8. Активировать курс
```
PATCH http://localhost:8080/api/courses/1/status?active=true
```

### 9. Удалить курс
```
DELETE http://localhost:8080/api/courses/1
```

## Main Controller (Основные эндпоинты)

### 1. Приветствие
```
GET http://localhost:8080/api/main
```

### 2. Информация об API
```
GET http://localhost:8080/api/main/info
```

### 3. Получить данные студента
```
GET http://localhost:8080/api/main/special
```

### 4. Создать студента с именем
```
POST http://localhost:8080/api/main/special?name=Асель Токарева
```

### 5. Сериализация студента
```
POST http://localhost:8080/api/main/serialize?name=Ерлан Сатпаев&age=22
```

## Примечания

1. **Content-Type**: Все POST/PUT запросы должны иметь заголовок `Content-Type: application/json`

2. **ID связанных объектов**: При создании курса или преподавателя нужно указывать только ID университета:
```json
{
  "university": {
    "id": 1
  }
}
```

3. **Обработка ошибок**: 
   - 404 - Объект не найден
   - 400 - Некорректные данные
   - 500 - Ошибка сервера

4. **Статус активности**: Используйте PATCH запросы для изменения статуса без полного обновления объекта

5. **Кириллица в URL**: В Postman автоматически кодируется, но в браузере нужно вручную кодировать:
   - `Информатика` → `%D0%98%D0%BD%D1%84%D0%BE%D1%80%D0%BC%D0%B0%D1%82%D0%B8%D0%BA%D0%B0`

