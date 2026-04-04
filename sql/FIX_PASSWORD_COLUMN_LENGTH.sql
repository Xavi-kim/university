-- ════════════════════════════════════════════════════════════════
-- ОБНОВЛЕНИЕ СТРУКТУРЫ ТАБЛИЦЫ USERS - УВЕЛИЧЕНИЕ ДЛИНЫ PASSWORD
-- ════════════════════════════════════════════════════════════════

-- 1. ПРОВЕРИТЬ ТЕКУЩУЮ ДЛИНУ
SELECT
    'Текущая структура поля password:' as info,
    column_name,
    data_type,
    character_maximum_length as current_length
FROM information_schema.columns
WHERE table_name = 'users'
AND column_name = 'password';

-- 2. УВЕЛИЧИТЬ ДЛИНУ ПОЛЯ PASSWORD ДО 255
ALTER TABLE users
ALTER COLUMN password TYPE VARCHAR(255);

-- 3. ПРОВЕРИТЬ НОВУЮ ДЛИНУ
SELECT
    'Новая структура поля password:' as info,
    column_name,
    data_type,
    character_maximum_length as new_length
FROM information_schema.columns
WHERE table_name = 'users'
AND column_name = 'password';

-- 4. ИСПРАВИТЬ ВСЕ НЕПОЛНЫЕ ХЕШИ
UPDATE users
SET password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS'
WHERE role = 'PROFESSOR'
AND LENGTH(password) != 60;

-- 5. ПРОВЕРИТЬ РЕЗУЛЬТАТ
SELECT
    '✅ Результат:' as info,
    COUNT(*) as total_professors,
    SUM(CASE WHEN LENGTH(password) = 60 THEN 1 ELSE 0 END) as correct_passwords,
    SUM(CASE WHEN LENGTH(password) != 60 THEN 1 ELSE 0 END) as incorrect_passwords
FROM users
WHERE role = 'PROFESSOR';

-- ════════════════════════════════════════════════════════════════
-- ✅ ГОТОВО!
-- ════════════════════════════════════════════════════════════════
-- Теперь поле password имеет правильную длину
-- И все хеши исправлены
-- ════════════════════════════════════════════════════════════════

