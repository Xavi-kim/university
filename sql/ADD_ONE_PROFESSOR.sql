-- ════════════════════════════════════════════════════════════════
-- ДОБАВЛЕНИЕ ОДНОГО ПРОФЕССОРА ВРУЧНУЮ
-- ════════════════════════════════════════════════════════════════
-- Используйте этот скрипт, если нужно добавить конкретного профессора
-- ════════════════════════════════════════════════════════════════

-- ШАГ 1: ИЗМЕНИТЕ ЭТИ ДАННЫЕ
-- Замените на данные вашего профессора:
\set prof_name 'Айгуль Нурбекова'
\set prof_email 'teacher@university.kz'
\set prof_department 'Информационные технологии'
\set prof_bio 'Преподаватель кафедры Программной инженерии. Опыт работы 10 лет.'
\set prof_password '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS'  -- professor123

-- ШАГ 2: ПРОВЕРЬТЕ, ЕСТЬ ЛИ УЖЕ ТАКОЙ ПРОФЕССОР
SELECT
    'Профессор существует в таблице professors' as info,
    id, name, email, department
FROM professors
WHERE email = 'teacher@university.kz';

-- ШАГ 3: ПРОВЕРЬТЕ, ЕСТЬ ЛИ УЖЕ ПОЛЬЗОВАТЕЛЬ
SELECT
    'Пользователь существует в таблице users' as info,
    id, name, email, role, enabled
FROM users
WHERE email = 'teacher@university.kz';

-- ШАГ 4: СОЗДАТЬ/ОБНОВИТЬ ПОЛЬЗОВАТЕЛЯ
-- Создать нового пользователя
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
    'teacher@university.kz',
    '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',  -- professor123
    'PROFESSOR',
    true,
    CURRENT_TIMESTAMP,
    true,
    'https://i.pravatar.cc/300?img=20',
    'Преподаватель кафедры Программной инженерии. Опыт работы 10 лет.'
)
ON CONFLICT (email) DO UPDATE SET
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    role = 'PROFESSOR',
    enabled = true,
    name = EXCLUDED.name,
    bio = EXCLUDED.bio,
    avatar_url = EXCLUDED.avatar_url;

-- ШАГ 5: ПРОВЕРИТЬ РЕЗУЛЬТАТ
SELECT
    '✅ ГОТОВО! Профессор может войти' as status,
    u.id,
    u.name,
    u.email,
    u.role,
    u.enabled,
    p.department
FROM users u
LEFT JOIN professors p ON u.email = p.email
WHERE u.email = 'teacher@university.kz';

-- ════════════════════════════════════════════════════════════════
-- ДАННЫЕ ДЛЯ ВХОДА:
-- Email:    teacher@university.kz
-- Пароль:   professor123
-- ════════════════════════════════════════════════════════════════

