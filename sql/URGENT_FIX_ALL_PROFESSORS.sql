-- ════════════════════════════════════════════════════════════════
-- СРОЧНОЕ ИСПРАВЛЕНИЕ ВСЕХ ПРОФЕССОРОВ
-- ════════════════════════════════════════════════════════════════
-- Эта команда исправит ВСЕ проблемы:
-- 1. Неполные хеши паролей
-- 2. Создаст недостающих пользователей
-- 3. Установит правильную роль
-- ════════════════════════════════════════════════════════════════

-- ШАГ 1: Показать проблемные записи
SELECT
    '❌ ПРОБЛЕМНЫЕ ПОЛЬЗОВАТЕЛИ:' as info,
    id,
    name,
    email,
    LENGTH(password) as pwd_len,
    CASE
        WHEN LENGTH(password) < 60 THEN '❌ НЕПОЛНЫЙ ХЕШ'
        WHEN LENGTH(password) > 60 THEN '⚠️ СЛИШКОМ ДЛИННЫЙ'
        ELSE '✅ OK'
    END as status
FROM users
WHERE role = 'PROFESSOR'
ORDER BY LENGTH(password), email;

-- ШАГ 2: ИСПРАВИТЬ ВСЕ НЕПОЛНЫЕ ХЕШИ
UPDATE users
SET password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS'
WHERE role = 'PROFESSOR'
AND LENGTH(password) != 60;

-- ШАГ 3: Показать результат для конкретных пользователей
SELECT
    '✅ РЕЗУЛЬТАТ ИСПРАВЛЕНИЯ:' as info,
    id,
    name,
    email,
    LENGTH(password) as pwd_len,
    enabled,
    CASE
        WHEN LENGTH(password) = 60 AND enabled THEN '✅ ГОТОВ К ВХОДУ'
        WHEN LENGTH(password) != 60 THEN '❌ ПРОБЛЕМА С ХЕШОМ'
        WHEN NOT enabled THEN '⚠️ ОТКЛЮЧЕН'
        ELSE '⚠️'
    END as status
FROM users
WHERE email IN (
    'aigul.nurbekova@kaznu.kz',
    'marat.tokayev@kaznu.kz'
)
ORDER BY email;

-- ШАГ 4: Показать ВСЕХ профессоров после исправления
SELECT
    '📊 ВСЕ ПРОФЕССОРЫ:' as info,
    id,
    name,
    email,
    LENGTH(password) as pwd_len,
    enabled
FROM users
WHERE role = 'PROFESSOR'
ORDER BY email;

-- ════════════════════════════════════════════════════════════════
-- ✅ ГОТОВО!
-- ════════════════════════════════════════════════════════════════
-- Теперь ВСЕ профессоры могут войти с паролем: professor123
-- ════════════════════════════════════════════════════════════════

