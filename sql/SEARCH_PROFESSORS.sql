-- ════════════════════════════════════════════════════════════════
-- ПОИСК ПРОФЕССОРОВ ПОХОЖИХ НА "Aigul Nurbekova"
-- ════════════════════════════════════════════════════════════════

-- 1. Найти всех профессоров
SELECT
    id,
    name,
    email,
    department,
    active
FROM professors
ORDER BY name;

-- 2. Найти профессоров с похожим именем
SELECT
    id,
    name,
    email,
    department,
    active
FROM professors
WHERE LOWER(name) LIKE '%aigul%'
   OR LOWER(name) LIKE '%nurbekova%'
   OR LOWER(name) LIKE '%нурбекова%'
   OR LOWER(name) LIKE '%айгуль%';

-- 3. Найти профессоров с похожим email
SELECT
    id,
    name,
    email,
    department,
    active
FROM professors
WHERE LOWER(email) LIKE '%aigul%'
   OR LOWER(email) LIKE '%nurbekova%';

-- 4. Показать всех пользователей с ролью PROFESSOR
SELECT
    id,
    name,
    email,
    role,
    enabled
FROM users
WHERE role = 'PROFESSOR'
ORDER BY name;

-- 5. Проверить конкретный email
SELECT
    'Проверка aigul.nurbekova@kaznu.kz' as info,
    COUNT(*) as count
FROM users
WHERE email = 'aigul.nurbekova@kaznu.kz';

-- 6. Показать профессоров БЕЗ пользователей
SELECT
    p.id,
    p.name as professor_name,
    p.email,
    p.department,
    '❌ НЕТ ПОЛЬЗОВАТЕЛЯ' as status
FROM professors p
LEFT JOIN users u ON p.email = u.email
WHERE u.id IS NULL;

