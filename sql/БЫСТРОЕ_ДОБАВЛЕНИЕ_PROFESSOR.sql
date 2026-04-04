-- ════════════════════════════════════════════════════════════════
-- БЫСТРОЕ ДОБАВЛЕНИЕ ПРЕПОДАВАТЕЛЯ
-- Скопируйте эти команды в pgAdmin Query Tool и нажмите F5
-- ════════════════════════════════════════════════════════════════

-- 1. ДОБАВИТЬ ПРЕПОДАВАТЕЛЯ (если его нет)
INSERT INTO users (
    name,
    email,
    password,
    role,
    enabled,
    registration_date,
    email_verified,
    avatar_url,
    phone_number,
    bio
)
VALUES (
    'Айгуль Нурбекова',
    'teacher@university.kz',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy',
    'PROFESSOR',
    true,
    CURRENT_TIMESTAMP,
    false,
    'https://i.pravatar.cc/300?img=20',
    '+7 (701) 987-65-43',
    'Преподаватель кафедры Программной инженерии. Опыт работы 10 лет.'
)
ON CONFLICT (email) DO NOTHING;

-- 2. ОБНОВИТЬ ПРЕПОДАВАТЕЛЯ (если он уже существует)
UPDATE users
SET
    password = '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy',
    role = 'PROFESSOR',
    enabled = true,
    name = 'Айгуль Нурбекова',
    avatar_url = 'https://i.pravatar.cc/300?img=20',
    phone_number = '+7 (701) 987-65-43',
    bio = 'Преподаватель кафедры Программной инженерии. Опыт работы 10 лет.'
WHERE email = 'teacher@university.kz';

-- 3. ПРОВЕРИТЬ РЕЗУЛЬТАТ
SELECT id, name, email, role, enabled
FROM users
WHERE email = 'teacher@university.kz';

-- 4. ПОКАЗАТЬ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ
SELECT id, name, email, role, enabled
FROM users
ORDER BY id;

-- ════════════════════════════════════════════════════════════════
-- ГОТОВО! Теперь войдите:
-- Email:    teacher@university.kz
-- Пароль:   teacher123
-- ════════════════════════════════════════════════════════════════

