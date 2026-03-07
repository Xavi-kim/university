# 🔐 ИСПРАВЛЕНИЕ ПРОБЛЕМЫ АУТЕНТИФИКАЦИИ

## 🐛 Проблема

**Симптомы:**
- При попытке войти с `admin@university.kz` / `admin123`
- Появляется ошибка: "Неверный email или пароль"
- Аутентификация не работает

## 🔍 Возможные причины

### 1. База данных не инициализирована
- DataInitializer не запустился
- Пользователи не созданы в PostgreSQL
- Таблица `users` пустая

### 2. Неправильный хеш пароля
- Пароль захеширован неправильно
- BCrypt не работает
- Конфликт версий PasswordEncoder

### 3. Spring Security неправильно настроен
- CustomUserDetailsService не вызывается
- Параметры формы не совпадают (email vs username)
- AuthenticationProvider не работает

### 4. База данных недоступна
- PostgreSQL не запущен
- База данных `university_db` не существует
- Неправильный пароль в `application.yml`

## ✅ ПОШАГОВОЕ РЕШЕНИЕ

### Шаг 1: Проверка PostgreSQL

#### 1.1 Убедитесь что PostgreSQL запущен:
```bash
# Откройте Services (services.msc)
# Найдите "postgresql-x64-16"
# Статус должен быть: Running
```

#### 1.2 Проверьте существование базы данных:
```bash
psql -U postgres
\l
# Должна быть база данных: university_db
```

#### 1.3 Если база не существует, создайте её:
```sql
CREATE DATABASE university_db;
\q
```

### Шаг 2: Проверка application.yml

Откройте `src/main/resources/application.yml` и проверьте:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/university_db
    username: postgres
    password: 123261181  # ✅ Ваш пароль PostgreSQL
    driver-class-name: org.postgresql.Driver
    
  jpa:
    hibernate:
      ddl-auto: update  # ✅ Должно быть update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

**ВАЖНО:** Пароль должен совпадать с паролем PostgreSQL!

### Шаг 3: Очистка и пересоздание базы данных

Если проблема продолжается, пересоздайте БД:

```sql
-- В psql:
DROP DATABASE IF EXISTS university_db;
CREATE DATABASE university_db;
```

### Шаг 4: Перезапуск приложения

1. **Остановите** все Java процессы:
```bash
taskkill /F /IM java.exe
```

2. **Удалите** скомпилированные классы:
```bash
cd C:\jakarta\university
rmdir /S /Q target
```

3. **Запустите** приложение:
```bash
start.bat
```

### Шаг 5: Проверка логов при запуске

При запуске приложения ищите в консоли:

#### ✅ Успешная инициализация:
```
✅ База данных инициализирована тестовыми данными:
   👤 Пользователей: 3
   📚 Университетов: 3
   👨‍🏫 Преподавателей: 5
   📖 Курсов: 7

🔑 Тестовые аккаунты:
   АДМИН: admin@university.kz / admin123
   СТУДЕНТ: asel@student.kz / 123456
   СТУДЕНТ: erlan@student.kz / 123456
```

#### ❌ Ошибка подключения к БД:
```
Error creating bean with name 'dataSource'
Connection to localhost:5432 refused
```
**Решение:** Запустите PostgreSQL

#### ❌ Ошибка SQL:
```
PSQLException: ERROR: column "registration_date" does not exist
```
**Решение:** Пересоздайте базу данных (см. Шаг 3)

### Шаг 6: Ручная проверка пользователей

Используйте скрипт `check-db.bat`:

```bash
cd C:\jakarta\university
check-db.bat
```

Или вручную в psql:

```sql
-- Подключитесь к базе
psql -U postgres -d university_db

-- Проверьте пользователей
SELECT id, name, email, role, enabled 
FROM users;

-- Проверьте хеш пароля админа
SELECT email, 
       LEFT(password, 60) as password_hash 
FROM users 
WHERE email = 'admin@university.kz';
```

**Ожидаемый результат:**
```
 email               | password_hash
---------------------+--------------------------------------------------------------
 admin@university.kz | $2a$10$xn3LI/AjqicFYZFruSwve.ClZXR7.dZzL0VvG...
```

Пароль должен начинаться с `$2a$10$` (BCrypt hash).

### Шаг 7: Тестирование входа

1. Откройте браузер: `http://localhost:8080/auth/login`

2. Введите:
   ```
   Email: admin@university.kz
   Пароль: admin123
   ```

3. Нажмите "Войти"

**Если успешно:** Редирект на `/admin/dashboard` ✅

**Если ошибка:** Проверьте консоль браузера (F12) на JavaScript ошибки.

## 🔧 АЛЬТЕРНАТИВНОЕ РЕШЕНИЕ: Ручное создание пользователя

Если DataInitializer не работает, создайте пользователя вручную:

### 1. Захешируйте пароль онлайн:

Перейдите на: https://bcrypt-generator.com/
- Введите: `admin123`
- Rounds: `10`
- Скопируйте хеш (начинается с `$2a$10$`)

### 2. Вставьте в PostgreSQL:

```sql
-- Подключитесь к БД
psql -U postgres -d university_db

-- Удалите старого админа (если есть)
DELETE FROM users WHERE email = 'admin@university.kz';

-- Вставьте нового с захешированным паролем
INSERT INTO users (name, email, password, role, enabled, registration_date, email_verified)
VALUES (
    'Администратор',
    'admin@university.kz',
    '$2a$10$xn3LI/AjqicFYZFruSwve.ClZXR7.dZzL0VvGxfF8fxQC8dNPKm7C',  -- admin123
    'ADMIN',
    true,
    NOW(),
    false
);

-- Проверьте
SELECT * FROM users WHERE email = 'admin@university.kz';
```

### 3. Перезапустите приложение и попробуйте войти.

## 🧪 ТЕСТИРОВАНИЕ BCRYPT

Создайте тестовый класс для проверки BCrypt:

```java
// src/test/java/org/example/university/PasswordTest.java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String plainPassword = "admin123";
        String hashedPassword = encoder.encode(plainPassword);
        
        System.out.println("Plain password: " + plainPassword);
        System.out.println("Hashed password: " + hashedPassword);
        System.out.println("Match: " + encoder.matches(plainPassword, hashedPassword));
        
        // Тест с существующим хешем из БД
        String dbHash = "$2a$10$..."; // Вставьте хеш из БД
        System.out.println("DB Match: " + encoder.matches(plainPassword, dbHash));
    }
}
```

## 📋 ЧЕКЛИСТ ДИАГНОСТИКИ

Пройдитесь по списку:

- [ ] PostgreSQL запущен (проверьте Services)
- [ ] База данных `university_db` существует
- [ ] Пароль в `application.yml` правильный (`123261181`)
- [ ] `ddl-auto` установлен в `update` (не `create-drop`)
- [ ] Приложение запускается без ошибок
- [ ] В консоли видно сообщение "База данных инициализирована"
- [ ] В таблице `users` есть записи (проверьте в psql)
- [ ] Пароль захеширован BCrypt (`$2a$10$...`)
- [ ] Страница `/auth/login` открывается
- [ ] Нет ошибок в консоли браузера (F12)

## 🔍 ОТЛАДКА SPRING SECURITY

Добавьте логирование в `application.yml`:

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.example.university: DEBUG
```

Перезапустите и проверьте логи при попытке входа.

## 💡 ЧАСТЫЕ ОШИБКИ

### Ошибка 1: "User not found"
**Причина:** Пользователь не создан в БД
**Решение:** Проверьте DataInitializer выполнился

### Ошибка 2: "Bad credentials"
**Причина:** Хеш пароля не совпадает
**Решение:** Пересоздайте пользователя с новым хешем

### Ошибка 3: "Access Denied"
**Причина:** Неправильная роль пользователя
**Решение:** Убедитесь что role = "ADMIN" (без "ROLE_" префикса)

### Ошибка 4: Редирект на `/auth/login` после входа
**Причина:** CustomAuthenticationSuccessHandler не работает
**Решение:** Проверьте SecurityConfig содержит `.successHandler(successHandler)`

## 🎯 ИТОГОВОЕ РЕШЕНИЕ

**Самый надёжный способ:**

1. **Остановите** приложение
2. **Пересоздайте** базу данных:
   ```sql
   DROP DATABASE university_db;
   CREATE DATABASE university_db;
   ```
3. **Удалите** папку `target`:
   ```bash
   rmdir /S /Q target
   ```
4. **Запустите** приложение:
   ```bash
   start.bat
   ```
5. **Дождитесь** сообщения "База данных инициализирована"
6. **Попробуйте** войти: `admin@university.kz` / `admin123`

## ✅ ПРОВЕРКА УСПЕШНОСТИ

После входа вы должны увидеть:

1. ✅ Редирект на `/admin/dashboard`
2. ✅ Имя пользователя в шапке: "Администратор"
3. ✅ Доступные функции: управление курсами, университетами, преподавателями
4. ✅ URL профиля работает: `/profile`

## 📞 ЧТО ДЕЛАТЬ ЕСЛИ НЕ ПОМОГЛО

1. Скопируйте **полный лог** из консоли при запуске
2. Проверьте **версию PostgreSQL**: `SELECT version();`
3. Проверьте **Java версию**: `java -version`
4. Убедитесь что **порт 5432** (PostgreSQL) не занят
5. Попробуйте изменить порт приложения в `application.yml`:
   ```yaml
   server:
     port: 8081
   ```

---

## 🎊 ПОСЛЕ ИСПРАВЛЕНИЯ

После успешного входа:

✅ Протестируйте все функции
✅ Проверьте профиль пользователя
✅ Попробуйте выйти и войти снова
✅ Зарегистрируйте нового пользователя
✅ Войдите как студент

**Приложение должно работать стабильно!** 🚀

