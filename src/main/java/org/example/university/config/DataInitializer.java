package org.example.university.config;

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
        System.out.println("🚀 Начинаем инициализацию базы данных...");

        // Очистка в правильном порядке (сначала зависимые таблицы)
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        professorRepository.deleteAll();
        universityRepository.deleteAll();
        userRepository.deleteAll();

        // Создание пользователей
        // Админ
        User admin = new User();
        admin.setName("Администратор");
        admin.setEmail("admin@kaznu.kz");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");
        admin.setEnabled(true);
        userRepository.save(admin);

        // Тестовые студенты
        User student1 = new User();
        student1.setName("Асылбек Касымов");
        student1.setEmail("asylbek.kasymov@student.kaznu.kz");
        student1.setPassword(passwordEncoder.encode("student123"));
        student1.setRole("STUDENT");
        student1.setEnabled(true);
        student1.setBio("Студент 3 курса, специальность ИТ");
        userRepository.save(student1);

        User student2 = new User();
        student2.setName("Динара Сагиева");
        student2.setEmail("dinara.sagieva@student.kaznu.kz");
        student2.setPassword(passwordEncoder.encode("student123"));
        student2.setRole("STUDENT");
        student2.setEnabled(true);
        student2.setBio("Студентка 2 курса, специальность ПИ");
        userRepository.save(student2);

        User student3 = new User();
        student3.setName("Ерлан Нурланов");
        student3.setEmail("erlan.nurlanov@student.kaznu.kz");
        student3.setPassword(passwordEncoder.encode("student123"));
        student3.setRole("STUDENT");
        student3.setEnabled(true);
        student3.setBio("Студент 4 курса, специальность КН");
        userRepository.save(student3);

        // Тестовые преподаватели (пользователи)
        User teacher1 = new User();
        teacher1.setName("Айгуль Нурбекова");
        teacher1.setEmail("aigul.nurbekova@kaznu.kz");
        teacher1.setPassword(passwordEncoder.encode("professor123"));
        teacher1.setRole("PROFESSOR");
        teacher1.setEnabled(true);
        teacher1.setBio("Профессор кафедры ИТ");
        userRepository.save(teacher1);

        User teacher2 = new User();
        teacher2.setName("Марат Токаев");
        teacher2.setEmail("marat.tokayev@kaznu.kz");
        teacher2.setPassword(passwordEncoder.encode("professor123"));
        teacher2.setRole("PROFESSOR");
        teacher2.setEnabled(true);
        teacher2.setBio("Доцент кафедры ПИ");
        userRepository.save(teacher2);

        User teacher3 = new User();
        teacher3.setName("Алия Жакупова");
        teacher3.setEmail("aliya.zhakupova@kaznu.kz");
        teacher3.setPassword(passwordEncoder.encode("professor123"));
        teacher3.setRole("PROFESSOR");
        teacher3.setEnabled(true);
        teacher3.setBio("Старший преподаватель");
        userRepository.save(teacher3);

        // Создание университетов
        University knu = new University(
            "Казахский Национальный Университет",
            "пр. Аль-Фараби, 71",
            "Алматы",
            "Казахстан"
        );
        knu.setWebsite("https://www.kaznu.kz");
        knu.setDescription("Ведущий университет Казахстана");
        universityRepository.save(knu);

        University enu = new University(
            "Евразийский Национальный Университет",
            "ул. Сатпаева, 2",
            "Астана",
            "Казахстан"
        );
        enu.setWebsite("https://www.enu.kz");
        enu.setDescription("Крупнейший университет столицы с современной инфраструктурой");
        universityRepository.save(enu);

        University kbtu = new University(
            "Казахстанско-Британский Технический Университет",
            "ул. Толе би, 59",
            "Алматы",
            "Казахстан"
        );
        kbtu.setWebsite("https://www.kbtu.kz");
        kbtu.setDescription("Инновационный технический университет с британскими стандартами образования");
        universityRepository.save(kbtu);

        // Создание преподавателей
        Professor prof1 = new Professor(
            "Айгуль Нурбекова",
            "aigul.nurbekova@kaznu.kz",
            "Информационные технологии",
            knu
        );
        prof1.setBio("Профессор кафедры ИТ. Специализация: базы данных, веб-разработка");
        professorRepository.save(prof1);

        Professor prof2 = new Professor(
            "Марат Токаев",
            "marat.tokayev@kaznu.kz",
            "Программная инженерия",
            knu
        );
        prof2.setBio("Доцент кафедры ПИ. Специализация: Java, Spring Framework");
        professorRepository.save(prof2);

        Professor prof3 = new Professor(
            "Алия Жакупова",
            "aliya.zhakupova@kaznu.kz",
            "Компьютерные науки",
            knu
        );
        prof3.setBio("Старший преподаватель. Специализация: алгоритмы, структуры данных");
        professorRepository.save(prof3);


        // Создание курсов
        Course course1 = new Course(
            "Базы данных",
            "Изучение реляционных БД, SQL, проектирование схем. Практика: PostgreSQL, индексы, транзакции, оптимизация запросов.",
            "Информационные технологии",
            "Осень 2025",
            prof1,
            knu
        );
        course1.setCredits(5);
        course1.setMaxStudents(30);
        courseRepository.save(course1);

        Course course2 = new Course(
            "Веб-разработка на Spring",
            "Разработка веб-приложений на Java Spring Boot. REST API, Spring Security, JPA/Hibernate, Thymeleaf.",
            "Программная инженерия",
            "Весна 2026",
            prof2,
            knu
        );
        course2.setCredits(6);
        course2.setMaxStudents(25);
        courseRepository.save(course2);

        Course course3 = new Course(
            "Алгоритмы и структуры данных",
            "Основные алгоритмы сортировки, поиска. Структуры данных: списки, деревья, графы. Анализ сложности.",
            "Компьютерные науки",
            "Осень 2025",
            prof3,
            knu
        );
        course3.setCredits(4);
        course3.setMaxStudents(40);
        courseRepository.save(course3);

        Course course4 = new Course(
            "Объектно-ориентированное программирование",
            "ООП на Java: классы, наследование, полиморфизм, интерфейсы. Паттерны проектирования.",
            "Программная инженерия",
            "Весна 2026",
            prof2,
            knu
        );
        course4.setCredits(5);
        course4.setMaxStudents(35);
        courseRepository.save(course4);


        System.out.println("✅ База данных инициализирована тестовыми данными:");
        System.out.println("   👤 Пользователей: " + userRepository.count());
        System.out.println("   📚 Университетов: " + universityRepository.count());
        System.out.println("   👨‍🏫 Преподавателей: " + professorRepository.count());
        System.out.println("   📖 Курсов: " + courseRepository.count());
        System.out.println("\n🔑 Тестовые аккаунты:");
        System.out.println("   ═══════════════════════════════════════════════════════");
        System.out.println("   АДМИН:        admin@kaznu.kz / admin123");
        System.out.println("   ПРОФЕССОР:    aigul.nurbekova@kaznu.kz / professor123");
        System.out.println("   ПРОФЕССОР:    marat.tokayev@kaznu.kz / professor123");
        System.out.println("   ПРОФЕССОР:    aliya.zhakupova@kaznu.kz / professor123");
        System.out.println("   СТУДЕНТ:      asylbek.kasymov@student.kaznu.kz / student123");
        System.out.println("   СТУДЕНТ:      dinara.sagieva@student.kaznu.kz / student123");
        System.out.println("   СТУДЕНТ:      erlan.nurlanov@student.kaznu.kz / student123");
        System.out.println("   ═══════════════════════════════════════════════════════");
    }
}

