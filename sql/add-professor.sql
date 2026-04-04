-- ════════════════════════════════════════════════════════════════
-- ДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ PROFESSOR В POSTGRESQL
-- ════════════════════════════════════════════════════════════════

-- Проверка существующих пользователей
SELECT id, name, email, role, enabled FROM users;

-- Добавление преподавателя с BCrypt хешем для пароля "teacher123"
-- BCrypt хеш: $2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy
INSERT INTO users (name, email, password, role, enabled, registration_date, email_verified)
VALUES (
    'Айгуль Нурбекова',
    'teacher@university.kz',
    '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy',
    'PROFESSOR',
    true,
    CURRENT_TIMESTAMP,
    false
);

-- Проверка что пользователь добавлен
SELECT id, name, email, role, enabled FROM users WHERE email = 'teacher@university.kz';

-- Проверка всех пользователей
SELECT id, name, email, role, enabled FROM users ORDER BY id;

