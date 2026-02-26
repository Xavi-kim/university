package org.example.university.controller;

import org.example.university.model.Course;
import org.example.university.model.Enrollment;
import org.example.university.model.User;
import org.example.university.service.CourseService;
import org.example.university.service.EnrollmentService;
import org.example.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller for Student Dashboard
 */
@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    /**
     * Проверка авторизации студента
     */
    private Long getStudentId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        Object role = session.getAttribute("userRole");

        if (userId != null && role != null && "STUDENT".equals(role.toString())) {
            return Long.parseLong(userId.toString());
        }
        return null;
    }

    /**
     * Панель студента
     */
    @GetMapping("/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("userName", session.getAttribute("userName"));

        // Курсы студента
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        model.addAttribute("enrollments", enrollments);

        // Доступные курсы для записи
        List<Course> availableCourses = courseService.getAllActiveCourses();
        model.addAttribute("availableCourses", availableCourses);

        return "student-dashboard";
    }

    /**
     * Запись на курс
     */
    @PostMapping("/enroll/{courseId}")
    public String enrollCourse(HttpSession session,
                              @PathVariable Long courseId,
                              Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        try {
            User student = userService.getUserById(studentId)
                    .orElseThrow(() -> new RuntimeException("Студент не найден"));
            Course course = courseService.getCourseById(courseId)
                    .orElseThrow(() -> new RuntimeException("Курс не найден"));

            enrollmentService.enrollStudent(student, course);

            return "redirect:/student/dashboard?success=enrolled";
        } catch (Exception e) {
            return "redirect:/student/dashboard?error=" + e.getMessage();
        }
    }

    /**
     * Отписаться от курса
     */
    @PostMapping("/unenroll/{enrollmentId}")
    public String unenrollCourse(HttpSession session, @PathVariable Long enrollmentId) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        // Проверка, что запись принадлежит этому студенту
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Запись не найдена"));

        if (!enrollment.getStudent().getId().equals(studentId)) {
            return "redirect:/student/dashboard?error=unauthorized";
        }

        enrollmentService.deleteEnrollment(enrollmentId);

        return "redirect:/student/dashboard?success=unenrolled";
    }

    /**
     * Мои курсы
     */
    @GetMapping("/my-courses")
    public String myCourses(HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("userName", session.getAttribute("userName"));

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        model.addAttribute("enrollments", enrollments);

        return "student-courses";
    }
}

