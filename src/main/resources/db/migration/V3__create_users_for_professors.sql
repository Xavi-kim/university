-- ════════════════════════════════════════════════════════════════
-- Flyway Migration V3: Создание пользователей для профессоров
-- ════════════════════════════════════════════════════════════════
-- Дата: 2026-03-07
-- Описание: Автоматическое создание пользователей для всех профессоров
--           из таблицы professors, у которых еще нет пользователей
-- ════════════════════════════════════════════════════════════════

-- Создать пользователей для профессоров, у которых их нет
INSERT INTO users (
    name,
    email,
    password,
    role,
    enabled,
    registration_date,
    email_verified,
    avatar_url,
    bio
)
SELECT
    p.name,
    p.email,
    '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS', -- professor123
    'PROFESSOR',
    true,
    CURRENT_TIMESTAMP,
    true,
    'https://i.pravatar.cc/300?img=' || (10 + p.id),
    COALESCE(p.bio, 'Преподаватель кафедры ' || p.department)
FROM professors p
WHERE NOT EXISTS (
    SELECT 1 FROM users u WHERE u.email = p.email
);

-- Обновить существующих пользователей-профессоров
UPDATE users u
SET
    role = 'PROFESSOR',
    enabled = true,
    email_verified = true
WHERE EXISTS (
    SELECT 1 FROM professors p WHERE p.email = u.email
)
AND u.role != 'PROFESSOR';

-- Получить статистику
DO $$
DECLARE
    total_professors INTEGER;
    total_users INTEGER;
    professors_with_users INTEGER;
BEGIN
    SELECT COUNT(*) INTO total_professors FROM professors;
    SELECT COUNT(*) INTO total_users FROM users WHERE role = 'PROFESSOR';
    SELECT COUNT(*) INTO professors_with_users
    FROM professors p
    INNER JOIN users u ON p.email = u.email;

    RAISE NOTICE '✅ Migration V3 completed: Created users for professors';
    RAISE NOTICE '   Total professors in DB: %', total_professors;
    RAISE NOTICE '   Total PROFESSOR users: %', total_users;
    RAISE NOTICE '   Professors with user accounts: %', professors_with_users;

    IF professors_with_users < total_professors THEN
        RAISE WARNING '⚠️ Some professors still do not have user accounts!';
    ELSE
        RAISE NOTICE '✅ All professors have user accounts';
    END IF;
END $$;

