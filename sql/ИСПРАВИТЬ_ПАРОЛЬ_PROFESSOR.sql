-- ════════════════════════════════════════════════════════════════
-- ИСПРАВЛЕНИЕ ПАРОЛЯ ПРЕПОДАВАТЕЛЯ
-- Скопируйте в pgAdmin Query Tool и выполните (F5)
-- ════════════════════════════════════════════════════════════════

-- Обновление пароля для teacher@university.kz
-- Пароль: teacher123
-- BCrypt хеш: $2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy

UPDATE users
SET
    password = '$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy',
    role = 'PROFESSOR',
    enabled = true
WHERE email = 'teacher@university.kz';

-- Проверка результата
SELECT id, name, email, role, enabled,
       LEFT(password, 30) || '...' as password_preview
FROM users
WHERE email = 'teacher@university.kz';

-- ════════════════════════════════════════════════════════════════
-- После выполнения войдите:
-- Email:    teacher@university.kz
-- Пароль:   teacher123
-- ════════════════════════════════════════════════════════════════

