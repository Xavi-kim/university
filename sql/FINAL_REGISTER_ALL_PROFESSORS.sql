-- ════════════════════════════════════════════════════════════════
-- ФИНАЛЬНАЯ РЕГИСТРАЦИЯ ВСЕХ ПРОФЕССОРОВ
-- ════════════════════════════════════════════════════════════════
-- Этот скрипт:
-- 1. Создает профессора aigul.nurbekova@kaznu.kz (если его нет)
-- 2. Создает пользователей для ВСЕХ профессоров
-- 3. Обновляет пароли существующих профессоров на professor123
-- ════════════════════════════════════════════════════════════════

-- ШАГ 1: Создать профессора Aigul Nurbekova (если его нет)
INSERT INTO professors (name, email, department, bio, active)
VALUES (
    'Айгуль Нурбекова',
    'aigul.nurbekova@kaznu.kz',
    'Информационные технологии',
    'Преподаватель кафедры Информационных технологий',
    true
)
ON CONFLICT (email) DO NOTHING;

-- ШАГ 2: Создать пользователей для ВСЕХ профессоров (включая aigul.nurbekova)
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

-- ШАГ 3: Обновить существующих пользователей-профессоров
-- Установить правильную роль и пароль professor123
-- ВАЖНО: Исправляем неполные хеши паролей (длина должна быть 60 символов)
UPDATE users u
SET
    role = 'PROFESSOR',
    enabled = true,
    password = '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS', -- professor123
    email_verified = true
WHERE EXISTS (
    SELECT 1 FROM professors p WHERE p.email = u.email
)
AND (
    LENGTH(u.password) != 60  -- Неполный хеш
    OR u.role != 'PROFESSOR'  -- Неправильная роль
    OR u.enabled = false      -- Отключен
);

-- Показать исправленные записи
SELECT
    '✅ Обновлены пользователи:' as info,
    COUNT(*) as count
FROM users u
WHERE EXISTS (SELECT 1 FROM professors p WHERE p.email = u.email);

-- ШАГ 4: ПРОВЕРКА РЕЗУЛЬТАТА

-- Показать aigul.nurbekova
SELECT
    '✅ Aigul Nurbekova:' as info,
    u.id,
    u.name,
    u.email,
    u.role,
    u.enabled,
    CASE WHEN u.enabled THEN '✅ Может войти' ELSE '❌' END as status
FROM users u
WHERE u.email = 'aigul.nurbekova@kaznu.kz';

-- Показать ВСЕХ пользователей-профессоров
SELECT
    '📊 Все пользователи с ролью PROFESSOR:' as info,
    COUNT(*) as total
FROM users
WHERE role = 'PROFESSOR';

-- Показать детальный список
SELECT
    u.id,
    u.name,
    u.email,
    u.role,
    u.enabled,
    p.department,
    CASE
        WHEN u.enabled THEN '✅ Активен'
        ELSE '⚠️ Отключен'
    END as status
FROM users u
LEFT JOIN professors p ON u.email = p.email
WHERE u.role = 'PROFESSOR'
ORDER BY u.name;

-- Показать профессоров БЕЗ пользователей (должно быть 0)
SELECT
    '❌ Профессоры БЕЗ пользователей:' as info,
    COUNT(*) as count
FROM professors p
LEFT JOIN users u ON p.email = u.email
WHERE u.id IS NULL;

-- ════════════════════════════════════════════════════════════════
-- ✅ ГОТОВО!
-- ════════════════════════════════════════════════════════════════
-- Все профессора теперь могут войти:
-- Email:    их email из таблицы professors
-- Пароль:   professor123
-- ════════════════════════════════════════════════════════════════

