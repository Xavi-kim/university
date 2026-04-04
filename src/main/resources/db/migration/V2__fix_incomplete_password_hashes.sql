-- ════════════════════════════════════════════════════════════════
-- Flyway Migration V2: Исправление неполных BCrypt хешей
-- ════════════════════════════════════════════════════════════════
-- Дата: 2026-03-07
-- Описание: Исправление всех неполных BCrypt хешей паролей для профессоров
--           Установка правильного хеша для пароля "professor123"
-- ════════════════════════════════════════════════════════════════

-- Исправить все неполные хеши для профессоров
UPDATE users
SET
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    enabled = true,
    email_verified = true
WHERE role = 'PROFESSOR'
AND LENGTH(password) != 60;

-- Получить статистику
DO $$
DECLARE
    fixed_count INTEGER;
    total_professors INTEGER;
BEGIN
    SELECT COUNT(*) INTO total_professors FROM users WHERE role = 'PROFESSOR';
    SELECT COUNT(*) INTO fixed_count FROM users WHERE role = 'PROFESSOR' AND LENGTH(password) = 60;

    RAISE NOTICE '✅ Migration V2 completed: Fixed password hashes for professors';
    RAISE NOTICE '   Total professors: %', total_professors;
    RAISE NOTICE '   Correct hashes: %', fixed_count;
END $$;

