# ⚠️ НАЙДЕНА ПРОБЛЕМА: PostgreSQL не подключена!

## 🔴 Ошибка

```
❌ ERROR: Cannot connect to PostgreSQL
```

**Это означает:**
- PostgreSQL не запущен
- База данных `university_db` не существует
- Неправильный пароль

**Из-за этого:** Пользователи не создаются → вход не работает!

---

## ✅ РЕШЕНИЕ

### Шаг 1: Запустите PostgreSQL

#### Способ 1: Через Services (GUI)

1. Нажмите `Win + R`
2. Введите: `services.msc`
3. Нажмите Enter
4. Найдите: `postgresql-x64-16` (или `postgresql-x64-15`)
5. Правый клик → **Start** (Запустить)
6. Убедитесь что статус: **Running**

#### Способ 2: Через командную строку

```bash
# Запустите PowerShell как Администратор
net start postgresql-x64-16
```

### Шаг 2: Создайте базу данных

#### Способ 1: Через командную строку

```bash
# Откройте командную строку (cmd)
cd C:\Program Files\PostgreSQL\16\bin

# Подключитесь к PostgreSQL
psql -U postgres

# Введите пароль: 123261181

# Создайте базу данных
CREATE DATABASE university_db;

# Проверьте
\l

# Выйдите
\q
```

#### Способ 2: Через pgAdmin

1. Откройте **pgAdmin 4**
2. Подключитесь к серверу (пароль: `123261181`)
3. Правый клик на **Databases** → **Create** → **Database**
4. Name: `university_db`
5. Owner: `postgres`
6. Нажмите **Save**

### Шаг 3: Проверьте подключение

```bash
psql -U postgres -d university_db -c "SELECT 1;"
```

**Должно вывести:**
```
 ?column? 
----------
        1
```

### Шаг 4: Запустите приложение

```bash
cd C:\jakarta\university
start-debug.bat
```

**Теперь должно быть:**
```
✅ PostgreSQL connection OK
✅ Found 0 user(s) in database
⚠️  WARNING: No users found in database
DataInitializer will create test accounts on startup
```

**Подождите ~30 секунд**, пока приложение запустится.

**Ищите в консоли:**
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

### Шаг 5: Войдите в систему

1. Откройте браузер (должен открыться автоматически)
2. Перейдите на: `http://localhost:8080/auth/login`
3. Введите:
   ```
   Email: admin@university.kz
   Пароль: admin123
   ```
4. Нажмите **Войти**

**Результат:** ✅ Перенаправление на `/admin/dashboard`

---

## 🔧 АЛЬТЕРНАТИВНОЕ РЕШЕНИЕ (Если PostgreSQL не установлен)

### Установите PostgreSQL 16:

1. Скачайте: https://www.postgresql.org/download/windows/
2. Запустите установщик
3. При установке:
   - **Password:** `123261181` (важно!)
   - **Port:** `5432`
   - **Locale:** `Russian, Russia`
4. После установки PostgreSQL запустится автоматически
5. Выполните Шаг 2 (создайте базу данных)

---

## 📋 ПРОВЕРКА ПОСЛЕ ИСПРАВЛЕНИЯ

После выполнения всех шагов проверьте:

```bash
# 1. PostgreSQL запущен
services.msc
# Найдите postgresql → статус Running

# 2. База данных существует
psql -U postgres -l | findstr university_db
# Должна быть строка с university_db

# 3. Пользователи созданы
psql -U postgres -d university_db -c "SELECT COUNT(*) FROM users;"
# Должно быть: 3

# 4. Приложение запущено
curl http://localhost:8080
# Должна открыться главная страница
```

---

## ⚡ БЫСТРОЕ РЕШЕНИЕ (1 КОМАНДА)

Если PostgreSQL установлен, но не запущен:

```bash
# В PowerShell (от администратора):
net start postgresql-x64-16 ; psql -U postgres -c "CREATE DATABASE IF NOT EXISTS university_db;" ; cd C:\jakarta\university ; .\start.bat
```

Введите пароль `123261181` когда попросит.

---

## 🎊 ГОТОВО!

После исправления:

✅ PostgreSQL запущен
✅ База данных создана
✅ Приложение запущено
✅ Пользователи созданы
✅ Можно войти в систему!

**Теперь всё работает!** 🚀

