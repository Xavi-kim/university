-- ════════════════════════════════════════════════════════════════
-- УДАЛЕНИЕ И ПЕРЕСОЗДАНИЕ ПРЕПОДАВАТЕЛЯ
-- Если UPDATE не помог - используйте это
-- ════════════════════════════════════════════════════════════════

-- Шаг 1: Удалить старого пользователя
DELETE FROM users WHERE email = 'teacher@university.kz';

-- Шаг 2: Создать нового с правильным паролем
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
    'Преподаватель кафедры Программной инженерии'
);

-- Шаг 3: Проверка
SELECT id, name, email, role, enabled, LEFT(password, 30) as password_hash
FROM users
WHERE email = 'teacher@university.kz';

-- ════════════════════════════════════════════════════════════════
-- После выполнения войдите:
-- Email:    teacher@university.kz
-- Пароль:   teacher123
-- ════════════════════════════════════════════════════════════════

