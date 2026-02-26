package org.example.university.controller;

import org.example.university.model.Course;
import org.example.university.model.Professor;
import org.example.university.model.University;
import org.example.university.service.CourseService;
import org.example.university.service.ProfessorService;
import org.example.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

/**
 * Controller for Admin Dashboard
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private UniversityService universityService;

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
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("universities", universityService.getAllUniversities());

        return "admin-dashboard";
    }

    /**
     * Страница добавления курса
     */
    @GetMapping("/courses/new")
    public String newCoursePage(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("professors", professorService.getAllActiveProfessors());
        model.addAttribute("universities", universityService.getAllActiveUniversities());

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
                              @RequestParam Long professorId,
                              @RequestParam Long universityId,
                              Model model) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        try {
            Professor professor = professorService.getProfessorById(professorId)
                    .orElseThrow(() -> new RuntimeException("Преподаватель не найден"));
            University university = universityService.getUniversityById(universityId)
                    .orElseThrow(() -> new RuntimeException("Университет не найден"));

            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setDepartment(department);
            course.setSemester(semester);
            course.setProfessor(professor);
            course.setUniversity(university);
            course.setActive(true);

            courseService.saveCourse(course);

            return "redirect:/admin/dashboard?success=course_created";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("professors", professorService.getAllActiveProfessors());
            model.addAttribute("universities", universityService.getAllActiveUniversities());
            return "admin-course-form";
        }
    }

    /**
     * Страница редактирования курса
     */
    @GetMapping("/courses/edit/{id}")
    public String editCoursePage(HttpSession session, @PathVariable Long id, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));

        model.addAttribute("course", course);
        model.addAttribute("professors", professorService.getAllActiveProfessors());
        model.addAttribute("universities", universityService.getAllActiveUniversities());

        return "admin-course-edit";
    }

    /**
     * Обновление курса
     */
    @PostMapping("/courses/{id}")
    public String updateCourse(HttpSession session,
                              @PathVariable Long id,
                              @RequestParam String title,
                              @RequestParam String description,
                              @RequestParam String department,
                              @RequestParam String semester,
                              @RequestParam Long professorId,
                              @RequestParam Long universityId) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));

        Professor professor = professorService.getProfessorById(professorId)
                .orElseThrow(() -> new RuntimeException("Преподаватель не найден"));
        University university = universityService.getUniversityById(universityId)
                .orElseThrow(() -> new RuntimeException("Университет не найден"));

        course.setTitle(title);
        course.setDescription(description);
        course.setDepartment(department);
        course.setSemester(semester);
        course.setProfessor(professor);
        course.setUniversity(university);

        courseService.saveCourse(course);

        return "redirect:/admin/dashboard?success=course_updated";
    }

    /**
     * Удаление курса
     */
    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(HttpSession session, @PathVariable Long id) {
        if (!isAdmin(session)) {
            return "redirect:/auth/login";
        }

        courseService.deleteCourse(id);
        return "redirect:/admin/dashboard?success=course_deleted";
    }
}

