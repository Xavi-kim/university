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

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GradeService gradeService;

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

    /**
     * Просмотр информации о профессорах
     */
    @GetMapping("/professors")
    public String professors(HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        List<Professor> professors = professorService.getAllActiveProfessors();
        model.addAttribute("professors", professors);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "student-professors";
    }

    /**
     * Детальная информация о профессоре
     */
    @GetMapping("/professors/{id}")
    public String professorDetails(@PathVariable Long id, HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        Professor professor = professorService.getProfessorById(id)
                .orElseThrow(() -> new RuntimeException("Профессор не найден"));

        List<Course> professorCourses = courseService.getCoursesByProfessor(professor);

        model.addAttribute("professor", professor);
        model.addAttribute("courses", professorCourses);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "student-professor-details";
    }

    /**
     * Домашние задания студента
     */
    @GetMapping("/homework")
    public String homework(HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        User student = userService.getUserById(studentId).orElseThrow();

        // Получаем курсы студента
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);

        // Получаем все сданные работы
        List<HomeworkSubmission> submissions = homeworkService.getStudentSubmissions(studentId);

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("submissions", submissions);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "student-homework";
    }

    /**
     * ДЗ для конкретного курса
     */
    @GetMapping("/courses/{courseId}/homework")
    public String courseHomework(@PathVariable Long courseId, HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        Course course = courseService.getCourseById(courseId).orElseThrow();
        List<Homework> homeworks = homeworkService.getHomeworksByCourse(courseId);

        // Получаем сдачи студента для этого курса
        List<HomeworkSubmission> submissions = homeworkService.getCourseSubmissionsForStudent(courseId, studentId);

        model.addAttribute("course", course);
        model.addAttribute("homeworks", homeworks);
        model.addAttribute("submissions", submissions);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "student-course-homework";
    }

    /**
     * Сдача домашнего задания
     */
    @PostMapping("/homework/{id}/submit")
    public String submitHomework(@PathVariable Long id,
                                @RequestParam String content,
                                @RequestParam(required = false) String fileUrl,
                                HttpSession session) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        User student = userService.getUserById(studentId).orElseThrow();
        Homework homework = homeworkService.getHomeworkById(id).orElseThrow();

        HomeworkSubmission submission = new HomeworkSubmission(homework, student, content);
        if (fileUrl != null && !fileUrl.trim().isEmpty()) {
            submission.setFileUrl(fileUrl);
        }

        homeworkService.submitHomework(submission);

        return "redirect:/student/courses/" + homework.getCourse().getId() + "/homework?success=submitted";
    }

    /**
     * Мои оценки
     */
    @GetMapping("/grades")
    public String grades(HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        User student = userService.getUserById(studentId).orElseThrow();
        List<Grade> grades = gradeService.getGradesByStudent(studentId);
        Double avgGPA = gradeService.calculateStudentGPA(studentId);

        model.addAttribute("grades", grades);
        model.addAttribute("avgGPA", avgGPA);
        model.addAttribute("userName", session.getAttribute("userName"));

        return "student-grades";
    }

    /**
     * Сообщения
     */
    @GetMapping("/messages")
    public String messages(HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) return "redirect:/auth/login";

        List<org.example.university.dto.ConversationDto> conversations = messageService.getConversations(studentId);
        Long unreadCount = messageService.getUnreadCount(studentId);

        model.addAttribute("conversations", conversations);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "student-messages";
    }

    /**
     * Отправка нового сообщения (форма "как почта")
     */
    @PostMapping("/messages/send")
    public String sendNewMessage(@RequestParam(required = false) Long recipientId,
                                 @RequestParam(required = false) String recipientEmail,
                                 @RequestParam String content,
                                 HttpSession session) {
        Long studentId = getStudentId(session);
        if (studentId == null) return "redirect:/auth/login";

        User student = userService.getUserById(studentId).orElseThrow();

        // Найти получателя по ID или по email
        User recipient = null;
        if (recipientId != null) {
            recipient = userService.getUserById(recipientId).orElse(null);
        }
        if (recipient == null && recipientEmail != null && !recipientEmail.isBlank()) {
            recipient = userService.getUserByEmail(recipientEmail.trim()).orElse(null);
        }
        if (recipient == null) {
            return "redirect:/student/messages?error=user_not_found";
        }

        Message message = new Message(student, recipient, content);
        messageService.sendMessage(message);
        return "redirect:/student/messages/" + recipient.getId();
    }

    /**
     * Диалог с пользователем
     */
    @GetMapping("/messages/{userId}")
    public String conversation(@PathVariable Long userId, HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) return "redirect:/auth/login";

        User other = userService.getUserById(userId).orElseThrow();
        List<Message> conversation = messageService.getConversation(studentId, userId);
        messageService.markConversationAsRead(studentId, userId);

        model.addAttribute("otherUser", other);
        model.addAttribute("messages", conversation);
        model.addAttribute("currentUserId", studentId);
        model.addAttribute("backUrl", "/student/messages");
        model.addAttribute("sendUrl", "/student/messages/" + userId + "/send");
        model.addAttribute("userName", session.getAttribute("userName"));
        return "conversation";
    }

    /**
     * Отправка сообщения в диалоге
     */
    @PostMapping("/messages/{userId}/send")
    public String sendMessageInDialog(@PathVariable Long userId,
                                      @RequestParam String content,
                                      HttpSession session) {
        Long studentId = getStudentId(session);
        if (studentId == null) return "redirect:/auth/login";

        User student = userService.getUserById(studentId).orElseThrow();
        User other = userService.getUserById(userId).orElseThrow();
        Message message = new Message(student, other, content);
        messageService.sendMessage(message);
        return "redirect:/student/messages/" + userId;
    }

    /**
     * Подробная информация о курсе
     */
    @GetMapping("/courses/{courseId}")
    public String courseDetails(@PathVariable Long courseId, HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        // Получить курс
        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));

        // Проверить, записан ли студент на этот курс
        Enrollment enrollment = enrollmentService.getEnrollmentsByStudent(studentId).stream()
                .filter(e -> e.getCourse().getId().equals(courseId))
                .findFirst()
                .orElse(null);

        // Получить домашние задания курса
        List<Homework> homeworks = homeworkService.getHomeworksByCourse(courseId);

        // Получить оценку студента (если есть)
        Grade grade = null;
        if (enrollment != null) {
            List<Grade> grades = gradeService.getGradesByEnrollment(enrollment);
            if (!grades.isEmpty()) {
                grade = grades.get(0);
            }
        }

        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("course", course);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("homeworks", homeworks);
        model.addAttribute("grade", grade);
        model.addAttribute("isEnrolled", enrollment != null);

        // ID пользователя-профессора для ссылки "Написать"
        if (course.getProfessor() != null) {
            userService.getUserByEmail(course.getProfessor().getEmail())
                .ifPresent(u -> model.addAttribute("profUserId", u.getId()));
        }

        return "student-course-detail";
    }


    /**
     * Чат - список профессоров для общения
     */
    @GetMapping("/chat")
    public String chat(HttpSession session, Model model) {
        Long studentId = getStudentId(session);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        // Получить всех профессоров, с которыми студент может общаться
        // (профессоров курсов, на которые записан студент)
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        List<Professor> professors = enrollments.stream()
                .map(e -> e.getCourse().getProfessor())
                .distinct()
                .toList();

        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("professors", professors);

        return "student-chat";
    }
}
