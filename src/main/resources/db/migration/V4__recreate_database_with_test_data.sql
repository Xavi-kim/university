-- ════════════════════════════════════════════════════════════════
-- Flyway Migration V4: Добавление тестовых данных
-- ════════════════════════════════════════════════════════════════
-- Дата: 2026-03-07
-- Описание: Добавление тестовых данных для разработки и тестирования
-- Использует ON CONFLICT DO NOTHING чтобы не создавать дубликаты
-- ════════════════════════════════════════════════════════════════

-- 1. Создать университет
INSERT INTO universities (id, name, address, city, country, website, description, active) VALUES
(1, 'Казахский Национальный Университет', 'пр. Аль-Фараби, 71', 'Алматы', 'Казахстан',
 'https://www.kaznu.kz', 'Ведущий университет Казахстана', true)
ON CONFLICT (id) DO NOTHING;

-- 2. Создать администратора
INSERT INTO users (id, name, email, password, role, enabled, registration_date, email_verified, bio) VALUES
(1, 'Администратор', 'admin@kaznu.kz',
 '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
 'ADMIN', true, NOW(), true, 'Системный администратор')
ON CONFLICT (email) DO NOTHING;

-- 3. Создать профессоров
INSERT INTO professors (id, name, email, department, bio, university_id, active) VALUES
(1, 'Айгуль Нурбекова', 'aigul.nurbekova@kaznu.kz', 'Информационные технологии',
 'Профессор кафедры ИТ. Специализация: базы данных, веб-разработка', 1, true),
(2, 'Марат Токаев', 'marat.tokayev@kaznu.kz', 'Программная инженерия',
 'Доцент кафедры ПИ. Специализация: Java, Spring Framework', 1, true),
(3, 'Алия Жакупова', 'aliya.zhakupova@kaznu.kz', 'Компьютерные науки',
 'Старший преподаватель. Специализация: алгоритмы, структуры данных', 1, true)
ON CONFLICT (email) DO NOTHING;

-- 4. Создать пользователей для профессоров
INSERT INTO users (id, name, email, password, role, enabled, registration_date, email_verified, bio) VALUES
(2, 'Айгуль Нурбекова', 'aigul.nurbekova@kaznu.kz',
 '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
 'PROFESSOR', true, NOW(), true, 'Профессор кафедры ИТ'),
(3, 'Марат Токаев', 'marat.tokayev@kaznu.kz',
 '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
 'PROFESSOR', true, NOW(), true, 'Доцент кафедры ПИ'),
(4, 'Алия Жакупова', 'aliya.zhakupova@kaznu.kz',
 '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
 'PROFESSOR', true, NOW(), true, 'Старший преподаватель')
ON CONFLICT (email) DO NOTHING;

-- 5. Создать студентов
INSERT INTO users (id, name, email, password, role, enabled, registration_date, email_verified, bio) VALUES
(5, 'Асылбек Касымов', 'asylbek.kasymov@student.kaznu.kz',
 '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
 'STUDENT', true, NOW(), true, 'Студент 3 курса, специальность ИТ'),
(6, 'Динара Сагиева', 'dinara.sagieva@student.kaznu.kz',
 '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
 'STUDENT', true, NOW(), true, 'Студентка 2 курса, специальность ПИ'),
(7, 'Ерлан Нурланов', 'erlan.nurlanov@student.kaznu.kz',
 '$2a$10$dXJ3SW6G7P3R1U5rn9HlPuFVMqEKfW9UnWjcMQ.3YLZnLMQ8rFfpS',
 'STUDENT', true, NOW(), true, 'Студент 4 курса, специальность КН')
ON CONFLICT (email) DO NOTHING;

-- 6. Создать курсы
INSERT INTO courses (id, title, description, department, semester, credits, max_students, professor_id, university_id, active) VALUES
(1, 'Базы данных',
 'Изучение реляционных БД, SQL, проектирование схем. Практика: PostgreSQL, индексы, транзакции, оптимизация запросов.',
 'Информационные технологии', 'Осень 2025', 5, 30, 1, 1, true),
(2, 'Веб-разработка на Spring',
 'Разработка веб-приложений на Java Spring Boot. REST API, Spring Security, JPA/Hibernate, Thymeleaf.',
 'Программная инженерия', 'Весна 2026', 6, 25, 2, 1, true),
(3, 'Алгоритмы и структуры данных',
 'Основные алгоритмы сортировки, поиска. Структуры данных: списки, деревья, графы. Анализ сложности.',
 'Компьютерные науки', 'Осень 2025', 4, 40, 3, 1, true),
(4, 'Объектно-ориентированное программирование',
 'ООП на Java: классы, наследование, полиморфизм, интерфейсы. Паттерны проектирования.',
 'Программная инженерия', 'Весна 2026', 5, 35, 2, 1, true)
ON CONFLICT (id) DO NOTHING;

-- 7. Записать студентов на курсы
INSERT INTO enrollments (id, user_id, course_id, enrollment_date, status) VALUES
(1, 5, 1, NOW() - INTERVAL '30 days', 'ACTIVE'),
(2, 5, 2, NOW() - INTERVAL '25 days', 'ACTIVE'),
(3, 6, 1, NOW() - INTERVAL '28 days', 'ACTIVE'),
(4, 6, 3, NOW() - INTERVAL '27 days', 'ACTIVE'),
(5, 7, 3, NOW() - INTERVAL '29 days', 'ACTIVE'),
(6, 7, 4, NOW() - INTERVAL '26 days', 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

-- 8. Создать оценки для некоторых студентов
INSERT INTO grades (id, enrollment_id, letter_grade, numeric_grade, gpa_value, comments, graded_at) VALUES
(1, 1, 'A', 95.0, 4.0, 'Отличное понимание материала, все задания выполнены на высоком уровне', NOW() - INTERVAL '5 days'),
(2, 3, 'B+', 88.0, 3.3, 'Хорошая работа, есть небольшие недочеты в проектной работе', NOW() - INTERVAL '3 days'),
(3, 5, 'A-', 92.0, 3.7, 'Очень хорошее знание алгоритмов, отличная практика', NOW() - INTERVAL '2 days')
ON CONFLICT (id) DO NOTHING;

-- 9. Создать домашние задания
INSERT INTO homeworks (id, course_id, title, description, due_date, max_score) VALUES
(1, 1, 'Проектирование базы данных',
 'Создать ER-диаграмму для системы управления библиотекой. Включить минимум 7 сущностей с связями.',
 NOW() + INTERVAL '14 days', 100),
(2, 2, 'REST API для блога',
 'Разработать REST API для блога на Spring Boot. CRUD операции для постов, комментариев, пользователей.',
 NOW() + INTERVAL '21 days', 100),
(3, 3, 'Реализация графа',
 'Реализовать структуру данных граф и алгоритмы: BFS, DFS, поиск кратчайшего пути (Дейкстра).',
 NOW() + INTERVAL '10 days', 100),
(4, 4, 'Паттерны проектирования',
 'Реализовать 5 паттернов проектирования: Singleton, Factory, Observer, Strategy, Decorator.',
 NOW() + INTERVAL '18 days', 100)
ON CONFLICT (id) DO NOTHING;

-- 10. Создать сданные работы студентов
INSERT INTO homework_submissions (id, homework_id, student_id, submission_text, submission_url, submitted_at, score, feedback) VALUES
(1, 1, 5, 'ER-диаграмма для системы библиотеки с 8 сущностями',
 'https://github.com/student/db-homework', NOW() - INTERVAL '2 days', 95,
 'Отличная работа! Все связи правильно спроектированы. Небольшое замечание по нормализации.'),
(2, 3, 6, 'Реализация графа на Java с BFS и DFS',
 'https://github.com/student/graph-homework', NOW() - INTERVAL '1 day', 88,
 'Хорошая реализация. Алгоритм Дейкстры работает корректно. Можно улучшить документацию кода.')
ON CONFLICT (id) DO NOTHING;

-- Вывести статистику
DO $$
DECLARE
    users_count INTEGER;
    professors_count INTEGER;
    students_count INTEGER;
    courses_count INTEGER;
    enrollments_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO users_count FROM users;
    SELECT COUNT(*) INTO professors_count FROM professors;
    SELECT COUNT(*) INTO students_count FROM users WHERE role = 'STUDENT';
    SELECT COUNT(*) INTO courses_count FROM courses;
    SELECT COUNT(*) INTO enrollments_count FROM enrollments;

    RAISE NOTICE '✅ Migration V4 completed: Test data created';
    RAISE NOTICE '   Total users: %', users_count;
    RAISE NOTICE '   Professors: %', professors_count;
    RAISE NOTICE '   Students: %', students_count;
    RAISE NOTICE '   Courses: %', courses_count;
    RAISE NOTICE '   Enrollments: %', enrollments_count;
    RAISE NOTICE '';
    RAISE NOTICE '📝 Login credentials:';
    RAISE NOTICE '   Admin: admin@kaznu.kz / admin123';
    RAISE NOTICE '   Professor: aigul.nurbekova@kaznu.kz / professor123';
    RAISE NOTICE '   Student: asylbek.kasymov@student.kaznu.kz / student123';
END $$;



