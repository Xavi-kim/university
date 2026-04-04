-- ════════════════════════════════════════════════════════════════
-- ПРОВЕРКА СТРУКТУРЫ ТАБЛИЦЫ USERS
-- ════════════════════════════════════════════════════════════════

-- 1. Проверить тип и длину поля password
SELECT
    column_name,
    data_type,
    character_maximum_length,
    is_nullable
FROM information_schema.columns
WHERE table_name = 'users'
AND column_name = 'password';

-- 2. Если длина меньше 60 - ИСПРАВИТЬ!
-- ALTER TABLE users ALTER COLUMN password TYPE VARCHAR(255);

-- 3. Показать все поля таблицы users
SELECT
    column_name,
    data_type,
    character_maximum_length,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_name = 'users'
ORDER BY ordinal_position;

-- ════════════════════════════════════════════════════════════════
-- РЕКОМЕНДАЦИЯ:
-- Поле password должно быть VARCHAR(60) или больше
-- BCrypt хеш всегда 60 символов
-- ════════════════════════════════════════════════════════════════

