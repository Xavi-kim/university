package org.example.university.controller;

import org.example.university.model.Course;
import org.example.university.model.Professor;
import org.example.university.model.University;
import org.example.university.service.CourseService;
import org.example.university.service.EnrollmentService;
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

    @Autowired private CourseService courseService;
    @Autowired private ProfessorService professorService;
    @Autowired private UniversityService universityService;
    @Autowired private EnrollmentService enrollmentService;

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
}
