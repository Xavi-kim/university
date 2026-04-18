package org.example.university.config;

import org.example.university.builder.UserBuilder;
import org.example.university.model.Course;
import org.example.university.model.Professor;
import org.example.university.model.University;
import org.example.university.model.User;
import org.example.university.repository.CourseRepository;
import org.example.university.repository.EnrollmentRepository;
import org.example.university.repository.ProfessorRepository;
import org.example.university.repository.UniversityRepository;
import org.example.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes the database with sample data
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Если в базе уже есть пользователи — считаем, что БД не пустая, инициализацию пропускаем.
        if (userRepository.count() > 0) {
            System.out.println("ℹ️ База уже содержит данные, инициализация пропущена");
            return;
        }

        System.out.println("🚀 Начинаем инициализацию базы данных...");

        final String adminEmail = "admin@kaznu.kz";

        // Админ
        User admin = new UserBuilder()
                .name("Администратор")
                .email(adminEmail)
                .password(passwordEncoder.encode("admin123"))
                .role("ADMIN")
                .enabled(true)
                .build();
        userRepository.save(admin);

        // ===== Пользователи =====
        // Тестовые студенты
        seedUserIfNotExists(
                "Асылбек Касымов",
                "asylbek.kasymov@student.kaznu.kz",
                "student123",
                "STUDENT",
                "Студент 3 курса, специальность ИТ"
        );

        seedUserIfNotExists(
                "Динара Сагиева",
                "dinara.sagieva@student.kaznu.kz",
                "student123",
                "STUDENT",
                "Студентка 2 курса, специальность ПИ"
        );

        seedUserIfNotExists(
                "Ерлан Нурланов",
                "erlan.nurlanov@student.kaznu.kz",
                "student123",
                "STUDENT",
                "Студент 4 курса, специальность КН"
        );

        // Тестовые преподаватели (пользователи)
        seedUserIfNotExists(
                "Айгуль Нурбекова",
                "aigul.nurbekova@kaznu.kz",
                "professor123",
                "PROFESSOR",
                "Профессор кафедры ИТ"
        );

        seedUserIfNotExists(
                "Марат Токаев",
                "marat.tokayev@kaznu.kz",
                "professor123",
                "PROFESSOR",
                "Доцент кафедры ПИ"
        );

        seedUserIfNotExists(
                "Алия Жакупова",
                "aliya.zhakupova@kaznu.kz",
                "professor123",
                "PROFESSOR",
                "Старший преподаватель"
        );

        // ===== Университеты/преподаватели/курсы =====
        // Важно: ниже оставляем текущую логику создания, но делаем её безопасной (не плодим дубликаты при ddl-auto=update).
        // Для полноценной прод-схемы лучше перенести это в Flyway миграции.

        University knu = universityRepository.findAll().stream()
                .filter(u -> "Казахский Национальный Университет".equalsIgnoreCase(u.getName()))
                .findFirst()
                .orElseGet(() -> {
                    University u = new University(
                            "Казахский Национальный Университет",
                            "пр. Аль-Фараби, 71",
                            "Алматы",
                            "Казахстан"
                    );
                    u.setWebsite("https://www.kaznu.kz");
                    u.setDescription("Ведущий университет Казахстана");
                    return universityRepository.save(u);
                });

        University enu = universityRepository.findAll().stream()
                .filter(u -> "Евразийский Национальный Университет".equalsIgnoreCase(u.getName()))
                .findFirst()
                .orElseGet(() -> {
                    University u = new University(
                            "Евразийский Национальный Университет",
                            "ул. Сатпаева, 2",
                            "Астана",
                            "Казахстан"
                    );
                    u.setWebsite("https://www.enu.kz");
                    u.setDescription("Крупнейший университет столицы с современной инфраструктурой");
                    return universityRepository.save(u);
                });

        University kbtu = universityRepository.findAll().stream()
                .filter(u -> "Казахстанско-Британский Технический Университет".equalsIgnoreCase(u.getName()))
                .findFirst()
                .orElseGet(() -> {
                    University u = new University(
                            "Казахстанско-Британский Технический Университет",
                            "ул. Толе би, 59",
                            "Алматы",
                            "Казахстан"
                    );
                    u.setWebsite("https://www.kbtu.kz");
                    u.setDescription("Инновационный технический университет с британскими стандартами образования");
                    return universityRepository.save(u);
                });

        Professor prof1 = professorRepository.findByEmail("aigul.nurbekova@kaznu.kz").orElseGet(() -> {
            Professor p = new Professor(
                    "Айгуль Нурбекова",
                    "aigul.nurbekova@kaznu.kz",
                    "Информационные технологии",
                    knu
            );
            p.setBio("Профессор кафедры ИТ. Специализация: базы данных, веб-разработка");
            return professorRepository.save(p);
        });

        Professor prof2 = professorRepository.findByEmail("marat.tokayev@kaznu.kz").orElseGet(() -> {
            Professor p = new Professor(
                    "Марат Токаев",
                    "marat.tokayev@kaznu.kz",
                    "Программная инженерия",
                    knu
            );
            p.setBio("Доцент кафедры ПИ. Специализация: Java, Spring Framework");
            return professorRepository.save(p);
        });

        Professor prof3 = professorRepository.findByEmail("aliya.zhakupova@kaznu.kz").orElseGet(() -> {
            Professor p = new Professor(
                    "Алия Жакупова",
                    "aliya.zhakupova@kaznu.kz",
                    "Компьютерные науки",
                    knu
            );
            p.setBio("Старший преподаватель. Специализация: алгоритмы, структуры данных");
            return professorRepository.save(p);
        });

        seedCourseIfNotExists(
                "Базы данных",
                prof1,
                knu,
                () -> {
                    Course c = new Course(
                            "Базы данных",
                            "Изучение реляционных БД, SQL, проектирование схем. Практика: PostgreSQL, индексы, транзакции, оптимизация запросов.",
                            "Информационные технологии",
                            "Осень 2025",
                            prof1,
                            knu
                    );
                    c.setCredits(5);
                    c.setMaxStudents(30);
                    return c;
                }
        );

        seedCourseIfNotExists(
                "Веб-разработка на Spring",
                prof2,
                knu,
                () -> {
                    Course c = new Course(
                            "Веб-разработка на Spring",
                            "Разработка веб-приложений на Java Spring Boot. REST API, Spring Security, JPA/Hibernate, Thymeleaf.",
                            "Программная инженерия",
                            "Весна 2026",
                            prof2,
                            knu
                    );
                    c.setCredits(6);
                    c.setMaxStudents(25);
                    return c;
                }
        );

        seedCourseIfNotExists(
                "Алгоритмы и структуры данных",
                prof3,
                knu,
                () -> {
                    Course c = new Course(
                            "Алгоритмы и структуры данных",
                            "Основные алгоритмы сортировки, поиска. Структуры данных: списки, деревья, графы. Анализ сложности.",
                            "Компьютерные науки",
                            "Осень 2025",
                            prof3,
                            knu
                    );
                    c.setCredits(4);
                    c.setMaxStudents(40);
                    return c;
                }
        );

        seedCourseIfNotExists(
                "Объектно-ориентированное программирование",
                prof2,
                knu,
                () -> {
                    Course c = new Course(
                            "Объектно-ориентированное программирование",
                            "ООП на Java: классы, наследование, полиморфизм, интерфейсы. Паттерны проектирования.",
                            "Программная инженерия",
                            "Весна 2026",
                            prof2,
                            knu
                    );
                    c.setCredits(5);
                    c.setMaxStudents(35);
                    return c;
                }
        );

        System.out.println("✅ Инициализация завершена (идемпотентно).");
        System.out.println("   👤 Пользователей: " + userRepository.count());
        System.out.println("   📚 Университетов: " + universityRepository.count());
        System.out.println("   👨‍🏫 Преподавателей: " + professorRepository.count());
        System.out.println("   📖 Курсов: " + courseRepository.count());
        System.out.println("\n🔑 Тестовые аккаунты:");
        System.out.println("   АДМИН:        admin@kaznu.kz / admin123");
        System.out.println("   ПРОФЕССОР:    aigul.nurbekova@kaznu.kz / professor123");
        System.out.println("   СТУДЕНТ:      asylbek.kasymov@student.kaznu.kz / student123");
    }

    private void seedUserIfNotExists(String name,
                                    String email,
                                    String rawPassword,
                                    String role,
                                    String bio) {
        if (userRepository.existsByEmail(email)) {
            return;
        }
        User u = new UserBuilder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .enabled(true)
                .bio(bio)
                .build();
        userRepository.save(u);
        System.out.println("✅ [SEED] Создан пользователь: " + email + " (" + role + ")");
    }

    private void seedCourseIfNotExists(String title,
                                      Professor professor,
                                      University university,
                                      java.util.function.Supplier<Course> factory) {
        boolean exists = courseRepository.findAll().stream()
                .anyMatch(c -> title.equalsIgnoreCase(c.getTitle())
                        && c.getProfessor() != null && c.getProfessor().getId() != null && c.getProfessor().getId().equals(professor.getId())
                        && c.getUniversity() != null && c.getUniversity().getId() != null && c.getUniversity().getId().equals(university.getId()));
        if (exists) {
            return;
        }
        courseRepository.save(factory.get());
        System.out.println("✅ [SEED] Создан курс: " + title);
    }
}
