-- ════════════════════════════════════════════════════════════════
-- ПРОВЕРКА ПРОФЕССОРОВ В БАЗЕ ДАННЫХ
-- ════════════════════════════════════════════════════════════════

-- 1. ПОКАЗАТЬ ВСЕХ ПРОФЕССОРОВ
SELECT
    id,
    name,
    email,
    department,
    university_id,
    active
FROM professors
ORDER BY id;

-- 2. ПОКАЗАТЬ ПОЛЬЗОВАТЕЛЕЙ С РОЛЬЮ PROFESSOR
SELECT
    id,
    name,
    email,
    role,
    enabled,
    registration_date
FROM users
WHERE role = 'PROFESSOR'
ORDER BY id;

-- 3. СОПОСТАВЛЕНИЕ: ПРОФЕССОР <-> ПОЛЬЗОВАТЕЛЬ
SELECT
    p.id as prof_id,
    p.name as prof_name,
    p.email,
    p.department,
    u.id as user_id,
    u.role,
    u.enabled,
    CASE
        WHEN u.id IS NULL THEN '❌ НЕТ ПОЛЬЗОВАТЕЛЯ'
        WHEN u.enabled THEN '✅ АКТИВЕН'
        ELSE '⚠️ ОТКЛЮЧЕН'
    END as status
FROM professors p
LEFT JOIN users u ON p.email = u.email
ORDER BY p.id;

