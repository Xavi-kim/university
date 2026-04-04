-- ════════════════════════════════════════════════════════════════
-- БЫСТРОЕ ИСПРАВЛЕНИЕ: Создать пользователя для aigul.nurbekova@kaznu.kz
-- ════════════════════════════════════════════════════════════════

-- 1. ПРОВЕРИТЬ, ЕСТЬ ЛИ ПРОФЕССОР В ТАБЛИЦЕ professors
SELECT
    'Проверка профессора' as info,
    id, name, email, department
FROM professors
WHERE email = 'aigul.nurbekova@kaznu.kz';

-- 2. ПРОВЕРИТЬ, ЕСТЬ ЛИ ПОЛЬЗОВАТЕЛЬ В ТАБЛИЦЕ users
SELECT
    'Проверка пользователя' as info,
    id, name, email, role, enabled
FROM users
WHERE email = 'aigul.nurbekova@kaznu.kz';

-- 3. ЕСЛИ ПРОФЕССОР СУЩЕСТВУЕТ - СОЗДАТЬ ПОЛЬЗОВАТЕЛЯ
-- Создать пользователя для Aigul Nurbekova
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
    COALESCE(p.name, 'Айгуль Нурбекова'),
    'aigul.nurbekova@kaznu.kz',
    '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',  -- professor123
    'PROFESSOR',
    true,
    CURRENT_TIMESTAMP,
    true,
    'https://i.pravatar.cc/300?img=25',
    COALESCE(p.bio, 'Преподаватель кафедры ' || COALESCE(p.department, 'Информационных технологий'))
FROM (SELECT 1) as dummy
LEFT JOIN professors p ON p.email = 'aigul.nurbekova@kaznu.kz'
WHERE NOT EXISTS (
    SELECT 1 FROM users u WHERE u.email = 'aigul.nurbekova@kaznu.kz'
);

-- 4. ЕСЛИ ПРОФЕССОРА НЕТ - СОЗДАТЬ И ПРОФЕССОРА, И ПОЛЬЗОВАТЕЛЯ
-- Создать профессора (если его нет)
INSERT INTO professors (
    name,
    email,
    department,
    bio,
    active
)
VALUES (
    'Айгуль Нурбекова',
    'aigul.nurbekova@kaznu.kz',
    'Информационные технологии',
    'Преподаватель кафедры Информационных технологий',
    true
)
ON CONFLICT (email) DO NOTHING;

-- Создать пользователя (если его нет)
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
VALUES (
    'Айгуль Нурбекова',
    'aigul.nurbekova@kaznu.kz',
    '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',  -- professor123
    'PROFESSOR',
    true,
    CURRENT_TIMESTAMP,
    true,
    'https://i.pravatar.cc/300?img=25',
    'Преподаватель кафедры Информационных технологий'
)
ON CONFLICT (email) DO UPDATE SET
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    role = 'PROFESSOR',
    enabled = true;

-- 5. ПРОВЕРИТЬ РЕЗУЛЬТАТ
SELECT
    '✅ ГОТОВО!' as status,
    p.id as prof_id,
    p.name as professor_name,
    p.email,
    p.department,
    u.id as user_id,
    u.role,
    u.enabled,
    CASE
        WHEN u.enabled THEN '✅ Может войти'
        ELSE '❌ Отключен'
    END as can_login
FROM professors p
LEFT JOIN users u ON p.email = u.email
WHERE p.email = 'aigul.nurbekova@kaznu.kz';

-- ════════════════════════════════════════════════════════════════
-- ДАННЫЕ ДЛЯ ВХОДА:
-- Email:    aigul.nurbekova@kaznu.kz
-- Пароль:   professor123
-- ════════════════════════════════════════════════════════════════

