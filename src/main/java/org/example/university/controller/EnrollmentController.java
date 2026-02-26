package org.example.university.controller;

import org.example.university.model.Course;
import org.example.university.model.Enrollment;
import org.example.university.model.User;
import org.example.university.service.CourseService;
import org.example.university.service.EnrollmentService;
import org.example.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST API Controller for Enrollment management
 */
@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    /**
     * GET: Получить все записи
     */
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    /**
     * GET: Получить записи студента
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    /**
     * GET: Получить записи на курс
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    /**
     * POST: Записать студента на курс
     */
    @PostMapping
    public ResponseEntity<?> enrollStudent(@RequestParam Long studentId, @RequestParam Long courseId) {
        Optional<User> studentOpt = userService.getUserById(studentId);
        Optional<Course> courseOpt = courseService.getCourseById(courseId);

        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Студент не найден");
        }

        if (courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Курс не найден");
        }

        User student = studentOpt.get();

        // Проверка роли
        if (!"STUDENT".equals(student.getRole())) {
            return ResponseEntity.badRequest().body("Только студенты могут записываться на курсы");
        }

        Enrollment enrollment = enrollmentService.enrollStudent(student, courseOpt.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    /**
     * DELETE: Отписать студента от курса
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        if (enrollmentService.getEnrollmentById(id).isPresent()) {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PATCH: Обновить статус записи
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Enrollment> updateStatus(@PathVariable Long id, @RequestParam String status) {
        Enrollment updated = enrollmentService.updateEnrollmentStatus(id, status);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PATCH: Поставить оценку
     */
    @PatchMapping("/{id}/grade")
    public ResponseEntity<Enrollment> updateGrade(@PathVariable Long id, @RequestParam Double grade) {
        Enrollment updated = enrollmentService.updateGrade(id, grade);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }
}

