-- ════════════════════════════════════════════════════════════════
-- ПОЛНАЯ ОЧИСТКА БАЗЫ ДАННЫХ (ОДИН РАЗ)
-- ════════════════════════════════════════════════════════════════
-- Выполните этот скрипт ОДИН РАЗ в pgAdmin
-- Затем запустите приложение - оно пересоздаст все данные
-- ════════════════════════════════════════════════════════════════

-- Отключить проверку внешних ключей
SET session_replication_role = 'replica';

-- Удалить все данные и таблицы
DROP TABLE IF EXISTS homework_submissions CASCADE;
DROP TABLE IF EXISTS homeworks CASCADE;
DROP TABLE IF EXISTS grades CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS professors CASCADE;
DROP TABLE IF EXISTS universities CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ВАЖНО: Удалить историю Flyway чтобы миграции применились заново
DROP TABLE IF EXISTS flyway_schema_history CASCADE;

-- Включить проверку
SET session_replication_role = 'origin';

-- Готово!
SELECT '✅ База данных полностью очищена (включая Flyway)!' as status;
SELECT 'Теперь запустите приложение: mvnw spring-boot:run' as next_step;

