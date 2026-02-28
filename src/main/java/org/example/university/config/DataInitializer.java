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
        // Если данные уже есть — не трогаем (PostgreSQL сохраняет данные между запусками)
        if (userRepository.count() > 0) {
            System.out.println("✅ База данных уже содержит данные, инициализация пропущена.");
            System.out.println("   👤 Пользователей: " + userRepository.count());
            System.out.println("   📚 Университетов: " + universityRepository.count());
            System.out.println("   👨‍🏫 Преподавателей: " + professorRepository.count());
            System.out.println("   📖 Курсов: " + courseRepository.count());
            return;
        }

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
        admin.setEmail("admin@university.kz");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");
        admin.setEnabled(true);
        userRepository.save(admin);

        // Тестовые студенты
        User student1 = new User();
        student1.setName("Асель Токарева");
        student1.setEmail("asel@student.kz");
        student1.setPassword(passwordEncoder.encode("123456"));
        student1.setRole("STUDENT");
        student1.setEnabled(true);
        userRepository.save(student1);

        User student2 = new User();
        student2.setName("Ерлан Сатпаев");
        student2.setEmail("erlan@student.kz");
        student2.setPassword(passwordEncoder.encode("123456"));
        student2.setRole("STUDENT");
        student2.setEnabled(true);
        userRepository.save(student2);

        // Создание университетов
        University knu = new University(
            "Казахский Национальный Университет им. аль-Фараби",
            "пр. аль-Фараби, 71",
            "Алматы",
            "Казахстан"
        );
        knu.setWebsite("https://www.kaznu.kz");
        knu.setDescription("Ведущий университет Казахстана с богатой историей и традициями");
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
            "Информатика",
            knu
        );
        prof1.setBio("Доктор технических наук, специалист по искусственному интеллекту");
        professorRepository.save(prof1);

        Professor prof2 = new Professor(
            "Ерлан Сагинов",
            "erlan.saginov@enu.kz",
            "Математика",
            enu
        );
        prof2.setBio("Кандидат физико-математических наук, эксперт по прикладной математике");
        professorRepository.save(prof2);

        Professor prof3 = new Professor(
            "Дина Абдуллаева",
            "dina.abdullayeva@kbtu.kz",
            "Программирование",
            kbtu
        );
        prof3.setBio("Магистр компьютерных наук, специалист по разработке программного обеспечения");
        professorRepository.save(prof3);

        Professor prof4 = new Professor(
            "Марат Токаев",
            "marat.tokayev@kaznu.kz",
            "Физика",
            knu
        );
        prof4.setBio("Доктор физико-математических наук, исследователь в области квантовой физики");
        professorRepository.save(prof4);

        Professor prof5 = new Professor(
            "Сауле Жанузакова",
            "saule.zhanuzakova@enu.kz",
            "Экономика",
            enu
        );
        prof5.setBio("Кандидат экономических наук, эксперт по макроэкономике");
        professorRepository.save(prof5);

        // Создание курсов
        Course course1 = new Course(
            "Искусственный интеллект",
            "Изучение основ машинного обучения, нейронных сетей и глубокого обучения. " +
            "Курс включает теоретические основы и практические задания по разработке AI-моделей.",
            "Информатика",
            "Осень 2024",
            prof1,
            knu
        );
        courseRepository.save(course1);

        Course course2 = new Course(
            "Дискретная математика",
            "Фундаментальный курс по теории множеств, комбинаторике, теории графов и математической логике. " +
            "Необходим для понимания алгоритмов и структур данных.",
            "Математика",
            "Весна 2025",
            prof2,
            enu
        );
        courseRepository.save(course2);

        Course course3 = new Course(
            "Разработка веб-приложений",
            "Практический курс по созданию современных веб-приложений с использованием Spring Boot, " +
            "React, и RESTful API. Включает проектную работу.",
            "Программирование",
            "Осень 2024",
            prof3,
            kbtu
        );
        courseRepository.save(course3);

        Course course4 = new Course(
            "Квантовая механика",
            "Углубленное изучение квантовой теории, принципа неопределенности, волновой функции. " +
            "Курс для студентов физических специальностей.",
            "Физика",
            "Весна 2025",
            prof4,
            knu
        );
        courseRepository.save(course4);

        Course course5 = new Course(
            "Макроэкономика",
            "Изучение национальной экономики в целом: ВВП, инфляция, безработица, денежно-кредитная политика. " +
            "Анализ современных экономических моделей.",
            "Экономика",
            "Осень 2024",
            prof5,
            enu
        );
        courseRepository.save(course5);

        Course course6 = new Course(
            "Структуры данных и алгоритмы",
            "Изучение основных структур данных (массивы, списки, деревья, графы) и алгоритмов их обработки. " +
            "Анализ сложности алгоритмов.",
            "Информатика",
            "Весна 2025",
            prof1,
            knu
        );
        courseRepository.save(course6);

        Course course7 = new Course(
            "Базы данных",
            "Проектирование и разработка реляционных баз данных. SQL, нормализация, транзакции, индексы. " +
            "Практика работы с PostgreSQL и MySQL.",
            "Программирование",
            "Осень 2024",
            prof3,
            kbtu
        );
        courseRepository.save(course7);

        System.out.println("✅ База данных инициализирована тестовыми данными:");
        System.out.println("   👤 Пользователей: " + userRepository.count());
        System.out.println("   📚 Университетов: " + universityRepository.count());
        System.out.println("   👨‍🏫 Преподавателей: " + professorRepository.count());
        System.out.println("   📖 Курсов: " + courseRepository.count());
        System.out.println("\n🔑 Тестовые аккаунты:");
        System.out.println("   АДМИН: admin@university.kz / admin123");
        System.out.println("   СТУДЕНТ: asel@student.kz / 123456");
        System.out.println("   СТУДЕНТ: erlan@student.kz / 123456");
    }
}

