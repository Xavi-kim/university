-- ════════════════════════════════════════════════════════════════
-- ИСПРАВЛЕНИЕ СТРУКТУРЫ ТАБЛИЦЫ USERS
-- ════════════════════════════════════════════════════════════════

-- Проверка текущей структуры
\d users

-- Добавление недостающих столбцов
ALTER TABLE users ADD COLUMN IF NOT EXISTS email_verified BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_token VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);
ALTER TABLE users ADD COLUMN IF NOT EXISTS bio VARCHAR(500);
ALTER TABLE users ADD COLUMN IF NOT EXISTS registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Обновление существующих данных
UPDATE users SET email_verified = false WHERE email_verified IS NULL;
UPDATE users SET registration_date = CURRENT_TIMESTAMP WHERE registration_date IS NULL;

-- Проверка результата
\d users

-- Проверка данных
SELECT id, name, email, role, enabled, email_verified, registration_date FROM users;

