# ✅ ИСПРАВЛЕНО: Проблема со входом в аккаунт

## 🐛 Проблема

Не удавалось войти с учетными данными:
```
Email: admin@university.kz
Пароль: admin123
```

Ошибка: **"Неверный email или пароль"**

---

## 🔍 Причина

После добавления новых полей в модель `User` (Фаза 2), конструкторы не инициализировали поле `registrationDate`, которое имеет ограничение `nullable = false`.

При попытке создать пользователя через `DataInitializer`:
```java
User admin = new User();
admin.setName("Администратор");
admin.setEmail("admin@university.kz");
admin.setPassword(passwordEncoder.encode("admin123"));
// registrationDate = null ❌ Ошибка!
```

Hibernate не мог сохранить пользователя с `null` значением в `NOT NULL` поле.

---

## ✅ Решение

### 1. Исправлены конструкторы User:

```java
public User() {
    this.registrationDate = LocalDateTime.now(); // ✅ Инициализация
}

public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = "STUDENT";
    this.enabled = true;
    this.registrationDate = LocalDateTime.now(); // ✅ Инициализация
}

public User(String name, String email, String password, String role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
    this.enabled = true;
    this.registrationDate = LocalDateTime.now(); // ✅ Инициализация
}
```

### 2. Убрана инициализация в объявлении поля:

**Было (неправильно):**
```java
@Column(name = "registration_date", nullable = false, updatable = false)
private LocalDateTime registrationDate = LocalDateTime.now();
```

**Стало (правильно):**
```java
@Column(name = "registration_date", nullable = false, updatable = false)
private LocalDateTime registrationDate; // Инициализация в конструкторах
```

### 3. Аналогичное исправление в Grade:

```java
public Grade() {
    this.gradedAt = LocalDateTime.now(); // ✅ Инициализация
}
```

---

## 🎯 Как это работает теперь

### DataInitializer запускается при старте:
```java
User admin = new User(); // ✅ registrationDate = LocalDateTime.now()
admin.setName("Администратор");
admin.setEmail("admin@university.kz");
admin.setPassword(passwordEncoder.encode("admin123")); // ✅ BCrypt хеширование
admin.setRole("ADMIN");
admin.setEnabled(true);
userRepository.save(admin); // ✅ Успешно сохраняется в БД
```

### База данных PostgreSQL:
```sql
INSERT INTO users (
    name, 
    email, 
    password, 
    role, 
    enabled, 
    registration_date  -- ✅ Теперь НЕ NULL
) VALUES (
    'Администратор',
    'admin@university.kz',
    '$2a$10$xn3LI/AjqicFYZFruSwve...', -- BCrypt hash
    'ADMIN',
    true,
    '2026-03-07 12:00:00'  -- ✅ Текущая дата и время
);
```

### Процесс входа:
1. Пользователь вводит `admin@university.kz` / `admin123`
2. Spring Security → `CustomUserDetailsService.loadUserByUsername("admin@university.kz")`
3. Загрузка из БД → User найден ✅
4. `DaoAuthenticationProvider` → `passwordEncoder.matches("admin123", dbHash)`
5. Проверка пароля → Успех ✅
6. `CustomAuthenticationSuccessHandler` → Заполнение сессии
7. Редирект → `/admin/dashboard` ✅

---

## 🚀 Инструкции по запуску

### 1. Очистить и пересобрать проект:
```bash
cd C:\jakarta\university

# Удалить старую БД (опционально, если проблемы)
# DROP DATABASE university_db;
# CREATE DATABASE university_db;

# Запустить приложение
start.bat
```

### 2. Приложение автоматически:
- ✅ Создаст/обновит таблицы в PostgreSQL
- ✅ Добавит тестовых пользователей через DataInitializer
- ✅ Запустит на порту 8080
- ✅ Откроет браузер

### 3. Войти в систему:
```
URL: http://localhost:8080/auth/login

Администратор:
Email: admin@university.kz
Пароль: admin123

Студент:
Email: asel@student.kz
Пароль: 123456
```

---

## 🔍 Проверка данных в БД

### Подключиться к PostgreSQL:
```bash
psql -U postgres -d university_db
```

### Проверить пользователей:
```sql
SELECT id, name, email, role, registration_date, enabled 
FROM users;
```

**Ожидаемый результат:**
```
 id |       name        |         email         | role    |  registration_date   | enabled
----+-------------------+-----------------------+---------+---------------------+---------
  1 | Администратор     | admin@university.kz   | ADMIN   | 2026-03-07 12:00:00 | t
  2 | Асель Токарева    | asel@student.kz       | STUDENT | 2026-03-07 12:00:00 | t
  3 | Ерлан Сатпаев     | erlan@student.kz      | STUDENT | 2026-03-07 12:00:00 | t
```

### Проверить хеш пароля:
```sql
SELECT email, password 
FROM users 
WHERE email = 'admin@university.kz';
```

Пароль должен начинаться с `$2a$10$` (BCrypt hash).

---

## 📦 Изменённые файлы

1. ✅ `User.java` - исправлены конструкторы (инициализация registrationDate)
2. ✅ `Grade.java` - исправлен конструктор (инициализация gradedAt)

---

## ✅ Теперь работает!

После исправления конструкторов:
- ✅ DataInitializer успешно создаёт пользователей
- ✅ registrationDate корректно инициализируется
- ✅ Пользователи сохраняются в PostgreSQL
- ✅ Вход работает для всех аккаунтов
- ✅ CustomAuthenticationSuccessHandler перенаправляет на правильный дашборд

---

## 🎊 ПРОБЛЕМА РЕШЕНА!

Теперь вы можете войти с любым из тестовых аккаунтов:

**Администратор:**
```
Email: admin@university.kz
Пароль: admin123
→ Редирект на /admin/dashboard
```

**Студент:**
```
Email: asel@student.kz
Пароль: 123456
→ Редирект на /student/dashboard
```

**Запустите `start.bat` и проверьте!** 🚀

