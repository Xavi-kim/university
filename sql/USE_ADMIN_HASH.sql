-- ════════════════════════════════════════════════════════════════
-- ИСПОЛЬЗОВАТЬ ПРОВЕРЕННЫЙ ХЕШ ОТ ADMIN
-- ════════════════════════════════════════════════════════════════

-- Пароль admin: admin123
-- Используем тот же хеш что у admin (он работает!)

UPDATE users
SET password = (SELECT password FROM users WHERE email = 'admin@university.kz')
WHERE email = 'teacher@university.kz';

-- Проверка
SELECT email, LEFT(password, 50) as password_hash
FROM users
WHERE email IN ('admin@university.kz', 'teacher@university.kz');

-- ════════════════════════════════════════════════════════════════
-- Теперь пароль преподавателя = admin123 (как у admin)
-- Войдите: teacher@university.kz / admin123
-- ════════════════════════════════════════════════════════════════

