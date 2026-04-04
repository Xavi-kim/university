package org.example.university.controller;

import org.example.university.model.*;
import org.example.university.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller for Admin Dashboard
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private CourseService courseService;
    @Autowired private ProfessorService professorService;
    @Autowired private UniversityService universityService;
    @Autowired private EnrollmentService enrollmentService;
    @Autowired private UserService userService;
    @Autowired private HomeworkService homeworkService;
    @Autowired private MessageService messageService;

    /**
     * Проверка авторизации админа
     */
    private boolean isAdmin(HttpSession session) {
        Object role = session.getAttribute("userRole");
        return role != null && "ADMIN".equals(role.toString());
    }

    /**
     * Админ панель
     */
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("universities", universityService.getAllUniversities());
        return "admin-dashboard";
    }

    // ──────────────── КУРСЫ ────────────────

    /**
     * Страница добавления курса
     */
    @GetMapping("/courses/new")
    public String newCoursePage(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("universities", universityService.getAllUniversities());
        return "admin-course-form";
    }

    /**
     * Создание курса
     */
    @PostMapping("/courses")
    public String createCourse(HttpSession session,
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam String department,
                               @RequestParam String semester,
                               @RequestParam(required = false) Long professorId,
                               @RequestParam(required = false) Long universityId,
                               Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        try {
            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setDepartment(department);
            course.setSemester(semester);
            course.setActive(true);
            if (professorId != null) professorService.getProfessorById(professorId).ifPresent(course::setProfessor);
            if (universityId != null) universityService.getUniversityById(universityId).ifPresent(course::setUniversity);
            courseService.saveCourse(course);
            return "redirect:/admin/dashboard?success=course_created";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("professors", professorService.getAllProfessors());
            model.addAttribute("universities", universityService.getAllUniversities());
            return "admin-course-form";
        }
    }

    /**
     * Страница редактирования курса
     */
    @GetMapping("/courses/edit/{id}")
    public String editCoursePage(HttpSession session, @PathVariable Long id, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        courseService.getCourseById(id).ifPresent(c -> model.addAttribute("course", c));
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("universities", universityService.getAllUniversities());
        return "admin-course-form";
    }

    /**
     * Обновление курса
     */
    @PostMapping("/courses/{id}")
    public String updateCourse(HttpSession session, @PathVariable Long id,
                               @RequestParam String title, @RequestParam String description,
                               @RequestParam String department, @RequestParam String semester,
                               @RequestParam(required = false) Long professorId,
                               @RequestParam(required = false) Long universityId) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        courseService.getCourseById(id).ifPresent(course -> {
            course.setTitle(title);
            course.setDescription(description);
            course.setDepartment(department);
            course.setSemester(semester);
            if (professorId != null) professorService.getProfessorById(professorId).ifPresent(course::setProfessor);
            if (universityId != null) universityService.getUniversityById(universityId).ifPresent(course::setUniversity);
            courseService.saveCourse(course);
        });
        return "redirect:/admin/dashboard?success=course_updated";
    }

    /**
     * Удаление курса
     */
    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(HttpSession session, @PathVariable Long id) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        courseService.deleteCourse(id);
        return "redirect:/admin/dashboard?success=course_deleted";
    }

    // ──────────────── ПРЕПОДАВАТЕЛИ ────────────────

    /**
     * Страница добавления преподавателя
     */
    @GetMapping("/professors/new")
    public String newProfessorPage(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        model.addAttribute("universities", universityService.getAllUniversities());
        return "admin-professor-form";
    }

    /**
     * Создание преподавателя
     */
    @PostMapping("/professors")
    public String createProfessor(HttpSession session,
                                  @RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String department,
                                  @RequestParam(required = false) String bio,
                                  @RequestParam(required = false) Long universityId,
                                  Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        try {
            Professor professor = new Professor();
            professor.setName(name);
            professor.setEmail(email);
            professor.setDepartment(department);
            professor.setBio(bio);
            professor.setActive(true);
            if (universityId != null) universityService.getUniversityById(universityId).ifPresent(professor::setUniversity);
            professorService.saveProfessor(professor);
            return "redirect:/admin/dashboard?success=professor_created";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("universities", universityService.getAllUniversities());
            return "admin-professor-form";
        }
    }

    /**
     * Страница редактирования преподавателя
     */
    @GetMapping("/professors/edit/{id}")
    public String editProfessorPage(HttpSession session, @PathVariable Long id, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        professorService.getProfessorById(id).ifPresent(p -> model.addAttribute("professor", p));
        model.addAttribute("universities", universityService.getAllUniversities());
        return "admin-professor-form";
    }

    /**
     * Обновление преподавателя
     */
    @PostMapping("/professors/{id}")
    public String updateProfessor(HttpSession session, @PathVariable Long id,
                                  @RequestParam String name, @RequestParam String email,
                                  @RequestParam String department,
                                  @RequestParam(required = false) String bio,
                                  @RequestParam(required = false) Long universityId) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        professorService.getProfessorById(id).ifPresent(professor -> {
            professor.setName(name);
            professor.setEmail(email);
            professor.setDepartment(department);
            professor.setBio(bio);
            if (universityId != null) universityService.getUniversityById(universityId).ifPresent(professor::setUniversity);
            professorService.saveProfessor(professor);
        });
        return "redirect:/admin/dashboard?success=professor_updated";
    }

    /**
     * Удаление преподавателя
     */
    @PostMapping("/professors/delete/{id}")
    public String deleteProfessor(HttpSession session, @PathVariable Long id) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        professorService.deleteProfessor(id);
        return "redirect:/admin/dashboard?success=professor_deleted";
    }

    // ──────────────── УНИВЕРСИТЕТЫ ────────────────

    /**
     * Страница добавления университета
     */
    @GetMapping("/universities/new")
    public String newUniversityPage(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        return "admin-university-form";
    }

    /**
     * Создание университета
     */
    @PostMapping("/universities")
    public String createUniversity(HttpSession session,
                                   @RequestParam String name,
                                   @RequestParam(required = false) String address,
                                   @RequestParam(required = false) String city,
                                   @RequestParam(required = false) String country,
                                   @RequestParam(required = false) String website,
                                   @RequestParam(required = false) String description,
                                   Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        try {
            University university = new University();
            university.setName(name);
            university.setAddress(address != null ? address : "");
            university.setCity(city != null ? city : "");
            university.setCountry(country != null ? country : "");
            university.setWebsite(website);
            university.setDescription(description);
            university.setActive(true);
            universityService.saveUniversity(university);
            return "redirect:/admin/dashboard?success=university_created";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "admin-university-form";
        }
    }

    /**
     * Страница редактирования университета
     */
    @GetMapping("/universities/edit/{id}")
    public String editUniversityPage(HttpSession session, @PathVariable Long id, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        universityService.getUniversityById(id).ifPresent(u -> model.addAttribute("university", u));
        return "admin-university-form";
    }

    /**
     * Обновление университета
     */
    @PostMapping("/universities/{id}")
    public String updateUniversity(HttpSession session, @PathVariable Long id,
                                   @RequestParam String name,
                                   @RequestParam(required = false) String address,
                                   @RequestParam(required = false) String city,
                                   @RequestParam(required = false) String country,
                                   @RequestParam(required = false) String website,
                                   @RequestParam(required = false) String description) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        universityService.getUniversityById(id).ifPresent(university -> {
            university.setName(name);
            university.setAddress(address != null ? address : "");
            university.setCity(city != null ? city : "");
            university.setCountry(country != null ? country : "");
            university.setWebsite(website);
            university.setDescription(description);
            universityService.saveUniversity(university);
        });
        return "redirect:/admin/dashboard?success=university_updated";
    }

    /**
     * Удаление университета
     */
    @PostMapping("/universities/delete/{id}")
    public String deleteUniversity(HttpSession session, @PathVariable Long id) {
        if (!isAdmin(session)) return "redirect:/auth/login";
        universityService.deleteUniversity(id);
        return "redirect:/admin/dashboard?success=university_deleted";
    }

    /**
     * Управление пользователями
     */
    @GetMapping("/users")
    public String users(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "admin-users";
    }

    /**
     * Назначение роли пользователю
     */
    @PostMapping("/users/{id}/assign-role")
    public String assignRole(HttpSession session,
                            @PathVariable Long id,
                            @RequestParam String role) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        User user = userService.getUserById(id).orElseThrow();
        user.setRole(role);
        userService.saveUser(user);

        // Если назначили роль PROFESSOR, создаём запись Professor
        if ("PROFESSOR".equals(role)) {
            Professor existingProf = professorService.findByEmail(user.getEmail());
            if (existingProf == null) {
                Professor professor = new Professor();
                professor.setName(user.getName());
                professor.setEmail(user.getEmail());
                professor.setDepartment("General");
                professor.setActive(true);
                professorService.saveProfessor(professor);
            }
        }

        return "redirect:/admin/users?success=role_assigned";
    }

    /**
     * Редактирование пользователя
     */
    @GetMapping("/users/{id}/edit")
    public String editUserForm(HttpSession session, @PathVariable Long id, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        User user = userService.getUserById(id).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "admin-edit-user";
    }

    /**
     * Обновление пользователя
     */
    @PostMapping("/users/{id}/update")
    public String updateUser(HttpSession session,
                            @PathVariable Long id,
                            @RequestParam String name,
                            @RequestParam String email,
                            @RequestParam String role,
                            @RequestParam(required = false) String phoneNumber,
                            @RequestParam(required = false) String bio) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        User user = userService.getUserById(id).orElseThrow();
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        user.setPhoneNumber(phoneNumber);
        user.setBio(bio);

        userService.saveUser(user);

        return "redirect:/admin/users?success=updated";
    }

    /**
     * Активация/деактивация пользователя
     */
    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(HttpSession session, @PathVariable Long id) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        User user = userService.getUserById(id).orElseThrow();
        user.setEnabled(!user.getEnabled());
        userService.saveUser(user);

        return "redirect:/admin/users?success=status_updated";
    }

    /**
     * Мониторинг профессоров
     */
    @GetMapping("/professors/monitor")
    public String monitorProfessors(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        List<Professor> professors = professorService.getAllProfessors();

        // Для каждого профессора собираем статистику
        for (Professor prof : professors) {
            List<Course> courses = courseService.getCoursesByProfessor(prof);
            prof.setCoursesCount(courses.size());

            // Подсчёт студентов
            int totalStudents = 0;
            for (Course course : courses) {
                totalStudents += enrollmentService.getEnrollmentsByCourse(course.getId()).size();
            }
            prof.setStudentsCount(totalStudents);
        }

        model.addAttribute("professors", professors);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "admin-monitor-professors";
    }

    /**
     * Детальная статистика профессора
     */
    @GetMapping("/professors/{id}/stats")
    public String professorStats(HttpSession session, @PathVariable Long id, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        Professor professor = professorService.getProfessorById(id).orElseThrow();
        List<Course> courses = courseService.getCoursesByProfessor(professor);

        // Собираем статистику
        int totalStudents = 0;
        int totalHomeworks = 0;

        for (Course course : courses) {
            totalStudents += enrollmentService.getEnrollmentsByCourse(course.getId()).size();
            totalHomeworks += homeworkService.getHomeworksByCourse(course.getId()).size();
        }

        model.addAttribute("professor", professor);
        model.addAttribute("courses", courses);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalHomeworks", totalHomeworks);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "admin-professor-stats";
    }

    /**
     * Общая статистика системы
     */
    @GetMapping("/statistics")
    public String systemStatistics(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        long totalUsers = userService.getAllUsers().size();
        long totalCourses = courseService.getAllCourses().size();
        long totalProfessors = professorService.getAllProfessors().size();
        long totalUniversities = universityService.getAllUniversities().size();
        long totalEnrollments = enrollmentService.getAllEnrollments().size();
        long totalHomeworks = homeworkService.getAllHomeworks().size();
        long totalMessages = messageService.getAllMessages().size();

        // Студенты
        long totalStudents = userService.getAllUsers().stream()
                .filter(u -> "STUDENT".equals(u.getRole()))
                .count();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("totalProfessors", totalProfessors);
        model.addAttribute("totalUniversities", totalUniversities);
        model.addAttribute("totalEnrollments", totalEnrollments);
        model.addAttribute("totalHomeworks", totalHomeworks);
        model.addAttribute("totalMessages", totalMessages);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "admin-statistics";
    }

    /**
     * Удаление пользователя
     */
    @PostMapping("/users/{id}/delete")
    public String deleteUser(HttpSession session, @PathVariable Long id) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        // Проверка что не удаляем самого себя
        Long currentUserId = (Long) session.getAttribute("userId");
        if (currentUserId.equals(id)) {
            return "redirect:/admin/users?error=cannot_delete_self";
        }

        userService.deleteUser(id);

        return "redirect:/admin/users?success=deleted";
    }

    /**
     * Автоматическая регистрация всех существующих профессоров как пользователей
     * Создает пользователей для всех профессоров, у которых их еще нет
     * Пароль по умолчанию: professor123
     */
    @PostMapping("/professors/register-all")
    public String registerAllProfessors(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/auth/login";

        try {
            int createdCount = professorService.registerAllExistingProfessors();

            System.out.println("✅ [ADMIN] Администратор зарегистрировал всех профессоров");
            System.out.println("   Создано пользователей: " + createdCount);

            return "redirect:/admin/dashboard?success=professors_registered&count=" + createdCount;
        } catch (Exception e) {
            System.err.println("❌ [ADMIN] Ошибка при регистрации профессоров: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/dashboard?error=registration_failed";
        }
    }
}
