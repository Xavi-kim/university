-- ════════════════════════════════════════════════════════════════
-- ИСПРАВЛЕНИЕ ПАРОЛЯ ДЛЯ marat.tokayev@kaznu.kz
-- ════════════════════════════════════════════════════════════════

-- Проверить текущее состояние
SELECT
    id,
    name,
    email,
    role,
    enabled,
    LENGTH(password) as password_length,
    SUBSTRING(password, 1, 30) as password_preview
FROM users
WHERE email = 'marat.tokayev@kaznu.kz';

-- Установить правильный пароль (professor123)
UPDATE users
SET
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    role = 'PROFESSOR',
    enabled = true,
    email_verified = true
WHERE email = 'marat.tokayev@kaznu.kz';

-- Проверить результат
SELECT
    id,
    name,
    email,
    role,
    enabled,
    LENGTH(password) as password_length,
    '✅ Пароль исправлен!' as status
FROM users
WHERE email = 'marat.tokayev@kaznu.kz';

-- ════════════════════════════════════════════════════════════════
-- ДАННЫЕ ДЛЯ ВХОДА:
-- Email:    marat.tokayev@kaznu.kz
-- Пароль:   professor123
-- ════════════════════════════════════════════════════════════════

