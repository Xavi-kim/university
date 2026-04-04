-- ════════════════════════════════════════════════════════════════
-- ИСПРАВЛЕНИЕ ВСЕХ НЕПОЛНЫХ ХЕШЕЙ ПАРОЛЕЙ ДЛЯ ПРОФЕССОРОВ
-- ════════════════════════════════════════════════════════════════
-- Проблема: Некоторые пароли в БД имеют неполный BCrypt хеш
-- Решение: Заменить все неполные хеши на правильный хеш для professor123
-- ════════════════════════════════════════════════════════════════

-- 1. НАЙТИ ПРОБЛЕМНЫЕ ЗАПИСИ
SELECT
    '❌ Пользователи с неполными хешами:' as info,
    id,
    name,
    email,
    role,
    LENGTH(password) as password_length,
    CASE
        WHEN LENGTH(password) = 60 THEN '✅ OK'
        WHEN LENGTH(password) < 60 THEN '❌ Неполный хеш'
        ELSE '⚠️ Слишком длинный'
    END as status
FROM users
WHERE role = 'PROFESSOR'
AND LENGTH(password) != 60
ORDER BY email;

-- 2. ПОКАЗАТЬ ВСЕХ ПРОФЕССОРОВ И ИХ СТАТУС
SELECT
    u.id,
    u.name,
    u.email,
    LENGTH(u.password) as pwd_len,
    CASE
        WHEN LENGTH(u.password) = 60 THEN '✅'
        WHEN LENGTH(u.password) < 60 THEN '❌ НУЖНО ИСПРАВИТЬ'
        ELSE '⚠️'
    END as status
FROM users u
WHERE u.role = 'PROFESSOR'
ORDER BY u.email;

-- 3. ИСПРАВИТЬ ВСЕ НЕПОЛНЫЕ ХЕШИ
-- Установить правильный BCrypt хеш для пароля professor123
UPDATE users
SET
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
    enabled = true,
    email_verified = true
WHERE role = 'PROFESSOR'
AND LENGTH(password) != 60;

-- 4. ПОКАЗАТЬ ИСПРАВЛЕННЫЕ ЗАПИСИ
SELECT
    '✅ Исправлено пользователей:' as info,
    COUNT(*) as count
FROM users
WHERE role = 'PROFESSOR'
AND password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS';

-- 5. ПРОВЕРКА РЕЗУЛЬТАТА - ВСЕ ПРОФЕССОРЫ
SELECT
    '📊 Все профессоры после исправления:' as info,
    u.id,
    u.name,
    u.email,
    LENGTH(u.password) as pwd_len,
    u.enabled,
    CASE
        WHEN LENGTH(u.password) = 60 AND u.enabled THEN '✅ Готов к входу'
        WHEN LENGTH(u.password) != 60 THEN '❌ ПРОБЛЕМА С ПАРОЛЕМ'
        WHEN NOT u.enabled THEN '⚠️ Отключен'
        ELSE '⚠️'
    END as status
FROM users u
WHERE u.role = 'PROFESSOR'
ORDER BY u.email;

-- 6. ПРОВЕРИТЬ КОНКРЕТНЫХ ПОЛЬЗОВАТЕЛЕЙ
SELECT
    '🔍 Проверка конкретных пользователей:' as info,
    email,
    LENGTH(password) as pwd_len,
    enabled,
    CASE
        WHEN LENGTH(password) = 60 AND enabled THEN '✅ OK'
        ELSE '❌ ПРОБЛЕМА'
    END as status
FROM users
WHERE email IN (
    'marat.tokayev@kaznu.kz',
    'aigul.nurbekova@kaznu.kz',
    'teacher@university.kz'
)
ORDER BY email;

-- ════════════════════════════════════════════════════════════════
-- ✅ ГОТОВО!
-- ════════════════════════════════════════════════════════════════
-- Все профессоры теперь имеют правильный хеш пароля
-- Пароль для всех: professor123
-- Длина BCrypt хеша: 60 символов
-- ════════════════════════════════════════════════════════════════

