-- ════════════════════════════════════════════════════════════════
-- АВТОМАТИЧЕСКАЯ РЕГИСТРАЦИЯ ВСЕХ ПРОФЕССОРОВ
-- ════════════════════════════════════════════════════════════════
-- Пароль для всех: "professor123"
-- BCrypt хеш: $2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS
-- ════════════════════════════════════════════════════════════════
--
-- ℹ️ ВАЖНО: Этот скрипт автоматически создает пользователей для ВСЕХ
-- профессоров из таблицы professors. Если пользователь уже существует,
-- скрипт обновит его роль на PROFESSOR и пароль на professor123.
-- ════════════════════════════════════════════════════════════════

-- 1. ПОКАЗАТЬ ВСЕХ ПРОФЕССОРОВ В БАЗЕ
SELECT
    '📊 Всего профессоров в базе:' as info,
    COUNT(*) as count
FROM professors;
SELECT
    id,
    name,
    email,
    department,
    university_id
FROM professors
ORDER BY id;

-- 2. ПОКАЗАТЬ СУЩЕСТВУЮЩИХ ПОЛЬЗОВАТЕЛЕЙ С РОЛЬЮ PROFESSOR
SELECT
    id,
    name,
    email,
    role,
    enabled
FROM users
WHERE role = 'PROFESSOR'
ORDER BY id;

-- 3. СОЗДАТЬ ПОЛЬЗОВАТЕЛЕЙ ДЛЯ ПРОФЕССОРОВ (если их еще нет в таблице users)
-- Создаем пользователя для каждого профессора из таблицы professors
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
SELECT
    p.name,
    p.email,
    '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS', -- professor123
    'PROFESSOR',
    true,
    CURRENT_TIMESTAMP,
    true,
    'https://i.pravatar.cc/300?img=' || (10 + p.id),
    COALESCE(p.bio, 'Преподаватель кафедры ' || p.department)
FROM professors p
WHERE NOT EXISTS (
    SELECT 1 FROM users u WHERE u.email = p.email
);

-- 4. ОБНОВИТЬ СУЩЕСТВУЮЩИХ ПОЛЬЗОВАТЕЛЕЙ-ПРОФЕССОРОВ
-- Убедиться что у них правильная роль и они активны
UPDATE users u
SET
    role = 'PROFESSOR',
    enabled = true,
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS', -- professor123
    email_verified = true
WHERE EXISTS (
    SELECT 1 FROM professors p WHERE p.email = u.email
);

-- 5. ПРОВЕРИТЬ РЕЗУЛЬТАТ - ПОКАЗАТЬ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ-ПРОФЕССОРОВ
SELECT
    u.id,
    u.name,
    u.email,
    u.role,
    u.enabled,
    p.department,
    u.registration_date
FROM users u
LEFT JOIN professors p ON u.email = p.email
WHERE u.role = 'PROFESSOR'
ORDER BY u.id;

-- 6. ПОКАЗАТЬ СОПОСТАВЛЕНИЕ: ПРОФЕССОР -> ПОЛЬЗОВАТЕЛЬ
SELECT
    p.id as professor_id,
    p.name as professor_name,
    p.email,
    p.department,
    u.id as user_id,
    u.role,
    u.enabled,
    CASE
        WHEN u.id IS NULL THEN '❌ НЕТ ПОЛЬЗОВАТЕЛЯ'
        WHEN u.enabled THEN '✅ ГОТОВ'
        ELSE '⚠️ ОТКЛЮЧЕН'
    END as status
FROM professors p
LEFT JOIN users u ON p.email = u.email
ORDER BY p.id;

-- ════════════════════════════════════════════════════════════════
-- ГОТОВО!
-- ════════════════════════════════════════════════════════════════
-- Теперь все профессора могут войти используя:
-- Email:    их email из таблицы professors
-- Пароль:   professor123
-- ════════════════════════════════════════════════════════════════

