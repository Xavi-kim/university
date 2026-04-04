-- ════════════════════════════════════════════════════════════════
-- Flyway Migration V1: Исправление длины поля password
-- ════════════════════════════════════════════════════════════════
-- Дата: 2026-03-07
-- Описание: Увеличение длины поля password до 255 символов
--           для корректного хранения BCrypt хешей (60 символов)
-- ════════════════════════════════════════════════════════════════

-- Изменить тип и длину поля password
ALTER TABLE users
ALTER COLUMN password TYPE VARCHAR(255);

-- Добавить комментарий к полю
COMMENT ON COLUMN users.password IS 'BCrypt хеш пароля (60 символов), VARCHAR(255) с запасом';

-- Вывести информацию об изменении
DO $$
BEGIN
    RAISE NOTICE '✅ Migration V1 completed: password field length increased to 255';
END $$;

