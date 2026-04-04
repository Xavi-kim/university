# ✅ ИСПРАВЛЕНИЕ ОШИБКИ ВХОДА - aigul.nurbekova@kaznu.kz

## ❌ ПРОБЛЕМА
```
[AUTH] Пользователь не найден: aigul.nurbekova@kaznu.kz
[AUTH FAILURE] BadCredentialsException: Bad credentials
```

## 🔍 ПРИЧИНА
Пользователь с email `aigul.nurbekova@kaznu.kz` не существует в таблице `users`.

---

## ✅ РЕШЕНИЕ 1: Автоматический скрипт (БЫСТРО)

### PowerShell:
```powershell
cd C:\jakarta\university
.\scripts\setup\fix-aigul-nurbekova.ps1
```

### Или запустите напрямую:
```powershell
$env:PGPASSWORD = "1234"
psql -U postgres -d university_db -f "sql\FIX_AIGUL_NURBEKOVA.sql"
```

---

## ✅ РЕШЕНИЕ 2: Вручную через pgAdmin (НАДЕЖНО)

### Шаг 1: Откройте pgAdmin
1. Запустите **pgAdmin**
2. Подключитесь к базе данных `university_db`
3. Откройте **Query Tool** (правой кнопкой на базе → Query Tool)

### Шаг 2: Выполните SQL команды

Скопируйте и выполните эти команды:

```sql
-- 1. Проверить, есть ли профессор в таблице professors
SELECT * FROM professors WHERE email = 'aigul.nurbekova@kaznu.kz';

-- 2. ЕСЛИ ПРОФЕССОР СУЩЕСТВУЕТ - создать только пользователя
INSERT INTO users (name, email, password, role, enabled, registration_date, email_verified)
SELECT 
    p.name,
    p.email,
    '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    'PROFESSOR',
    true,
    NOW(),
    true
FROM professors p
WHERE p.email = 'aigul.nurbekova@kaznu.kz'
ON CONFLICT (email) DO UPDATE SET
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    role = 'PROFESSOR',
    enabled = true;

-- 3. ЕСЛИ ПРОФЕССОРА НЕТ - создать и профессора, и пользователя
-- Создать профессора
INSERT INTO professors (name, email, department, bio, active)
VALUES (
    'Айгуль Нурбекова',
    'aigul.nurbekova@kaznu.kz',
    'Информационные технологии',
    'Преподаватель кафедры Информационных технологий',
    true
)
ON CONFLICT (email) DO NOTHING;

-- Создать пользователя
INSERT INTO users (name, email, password, role, enabled, registration_date, email_verified)
VALUES (
    'Айгуль Нурбекова',
    'aigul.nurbekova@kaznu.kz',
    '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    'PROFESSOR',
    true,
    NOW(),
    true
)
ON CONFLICT (email) DO UPDATE SET
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    role = 'PROFESSOR',
    enabled = true;

-- 4. Проверить результат
SELECT 
    u.id,
    u.name,
    u.email,
    u.role,
    u.enabled,
    '✅ Готово!' as status
FROM users u
WHERE u.email = 'aigul.nurbekova@kaznu.kz';
```

### Шаг 3: Нажмите F5 или кнопку Execute

---

## ✅ РЕШЕНИЕ 3: Быстрая команда (САМОЕ ПРОСТОЕ)

### В pgAdmin Query Tool выполните:

```sql
-- Один запрос - создает все автоматически
DO $$
BEGIN
    -- Создать профессора если нет
    INSERT INTO professors (name, email, department, bio, active)
    VALUES ('Айгуль Нурбекова', 'aigul.nurbekova@kaznu.kz', 'Информационные технологии', 'Преподаватель', true)
    ON CONFLICT (email) DO NOTHING;
    
    -- Создать пользователя
    INSERT INTO users (name, email, password, role, enabled, registration_date, email_verified)
    VALUES (
        'Айгуль Нурбекова',
        'aigul.nurbekova@kaznu.kz',
        '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
        'PROFESSOR',
        true,
        NOW(),
        true
    )
    ON CONFLICT (email) DO UPDATE SET
        password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
        role = 'PROFESSOR',
        enabled = true;
    
    RAISE NOTICE '✅ Пользователь создан!';
END $$;

-- Проверка
SELECT id, name, email, role, enabled FROM users WHERE email = 'aigul.nurbekova@kaznu.kz';
```

---

## 🔐 ДАННЫЕ ДЛЯ ВХОДА

После выполнения любого из решений:

| Параметр | Значение |
|----------|----------|
| **URL** | http://localhost:8080/login |
| **Email** | `aigul.nurbekova@kaznu.kz` |
| **Пароль** | `professor123` |

---

## 🔍 ПРОВЕРКА

### 1. Проверить в базе данных:
```sql
SELECT 
    u.name,
    u.email,
    u.role,
    u.enabled,
    CASE WHEN u.enabled THEN '✅ Активен' ELSE '❌ Отключен' END as status
FROM users u
WHERE u.email = 'aigul.nurbekova@kaznu.kz';
```

### 2. Попробовать войти:
1. Откройте http://localhost:8080/login
2. Email: `aigul.nurbekova@kaznu.kz`
3. Пароль: `professor123`
4. Должны попасть на `/professor/dashboard`

---

## 🚀 СОЗДАТЬ ПОЛЬЗОВАТЕЛЕЙ ДЛЯ ВСЕХ ПРОФЕССОРОВ

Если хотите создать пользователей для ВСЕХ профессоров сразу:

```powershell
cd C:\jakarta\university
.\scripts\setup\create-professor-users.ps1
```

Или в pgAdmin:
```sql
-- Открыть файл: sql\CREATE_USERS_FOR_PROFESSORS.sql
-- Нажать F5
```

---

## 📁 СОЗДАННЫЕ ФАЙЛЫ

- ✅ `sql/FIX_AIGUL_NURBEKOVA.sql` - SQL скрипт
- ✅ `scripts/setup/fix-aigul-nurbekova.ps1` - PowerShell скрипт
- ✅ `ИСПРАВЛЕНИЕ_AIGUL_NURBEKOVA.md` - Этот документ

---

## 🎯 ИТОГ

### ✅ Что нужно сделать:
1. Открыть **pgAdmin**
2. Подключиться к `university_db`
3. Выполнить **РЕШЕНИЕ 3** (самое простое)
4. Войти на сайт с паролем `professor123`

---

**Статус:** ✅ Решение готово  
**Время:** 1 минута  
**Сложность:** Легко

