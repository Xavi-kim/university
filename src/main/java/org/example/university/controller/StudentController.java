package org.example.university.controller;

import org.example.university.exception.NotFoundException;
import org.example.university.model.*;
import org.example.university.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

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

    @Autowired
    private CourseReviewService courseReviewService;

    /**
     * Получить studentId через Spring Security Authentication.
     */
    private Long getStudentId(Authentication authentication) {
        if (authentication == null) return null;
        String email = authentication.getName();
        return userService.getUserByEmail(email)
                .map(User::getId)
                .orElse(null);
    }

    /**
     * Панель студента
     */
    @GetMapping("/dashboard")
    public String studentDashboard(Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        // имя для приветствия: сначала пробуем сессию (если выставляли), иначе берём из БД
        Object userName = session.getAttribute("userName");
        if (userName == null) {
            userName = userService.getUserById(studentId).map(User::getName).orElse("Student");
        }
        model.addAttribute("userName", userName);

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        model.addAttribute("enrollments", enrollments);

        List<Course> availableCourses = courseService.getAllActiveCourses();
        model.addAttribute("availableCourses", availableCourses);

        return "student-dashboard";
    }

    /**
     * Запись на курс
     */
    @PostMapping("/enroll/{courseId}")
    public String enrollCourse(Authentication authentication,
                              @PathVariable Long courseId,
                              Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        try {
            User student = userService.getUserById(studentId)
                    .orElseThrow(() -> new NotFoundException("Студент не найден"));
            Course course = courseService.getCourseById(courseId)
                    .orElseThrow(() -> new NotFoundException("Курс не найден"));

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
    public String unenrollCourse(Authentication authentication, @PathVariable Long enrollmentId) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId)
                .orElseThrow(() -> new NotFoundException("Запись не найдена"));

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
    public String myCourses(Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        Object userName = session.getAttribute("userName");
        if (userName == null) {
            userName = userService.getUserById(studentId).map(User::getName).orElse("Student");
        }
        model.addAttribute("userName", userName);

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        model.addAttribute("enrollments", enrollments);

        return "student-courses";
    }

    /**
     * Просмотр информации о профессорах
     */
    @GetMapping("/professors")
    public String professors(Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
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
    public String professorDetails(@PathVariable Long id, Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        Professor professor = professorService.getProfessorById(id)
                .orElseThrow(() -> new NotFoundException("Профессор не найден"));

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
    public String homework(Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
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
    public String courseHomework(@PathVariable Long courseId, Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        Course course = courseService.getCourseById(courseId).orElseThrow(() -> new NotFoundException("Курс не найден"));
        List<Homework> homeworks = homeworkService.getHomeworksByCourse(courseId);
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
                                 Authentication authentication) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        User student = userService.getUserById(studentId).orElseThrow(() -> new NotFoundException("Студент не найден"));
        Homework homework = homeworkService.getHomeworkById(id).orElseThrow(() -> new NotFoundException("ДЗ не найдено"));

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
    public String grades(Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

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
    public String messages(Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
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
                                 Authentication authentication) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) return "redirect:/auth/login";

        User student = userService.getUserById(studentId).orElseThrow(() -> new NotFoundException("Студент не найден"));

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
    public String conversation(@PathVariable Long userId, Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) return "redirect:/auth/login";

        User other = userService.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
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
                                      Authentication authentication) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) return "redirect:/auth/login";

        User student = userService.getUserById(studentId).orElseThrow(() -> new NotFoundException("Студент не найден"));
        User other = userService.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Message message = new Message(student, other, content);
        messageService.sendMessage(message);
        return "redirect:/student/messages/" + userId;
    }

    /**
     * Подробная информация о курсе
     */
    @GetMapping("/courses/{courseId}")
    public String courseDetails(@PathVariable Long courseId, Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс не найден"));

        Enrollment enrollment = enrollmentService.getEnrollmentsByStudent(studentId).stream()
                .filter(e -> e.getCourse().getId().equals(courseId))
                .findFirst()
                .orElse(null);

        List<Homework> homeworks = homeworkService.getHomeworksByCourse(courseId);

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

        // 1) Правильный поиск userId профессора
        Long profUserId = null;
        if (course.getProfessor() != null && course.getProfessor().getEmail() != null) {
            profUserId = userService.getUserByEmail(course.getProfessor().getEmail())
                    .map(User::getId)
                    .orElse(null);
        }
        model.addAttribute("profUserId", profUserId);

        // 4) Отзывы
        Double averageRating = courseReviewService.getAverageRating(courseId);
        boolean hasReviewed = courseReviewService.hasStudentReviewed(courseId, studentId);
        List<CourseReview> reviews = courseReviewService.getReviewsForCourse(courseId);
        List<CourseReview> latest3 = reviews.stream().limit(3).toList();

        model.addAttribute("averageRating", averageRating);
        model.addAttribute("latestReviews", latest3);
        model.addAttribute("hasReviewed", hasReviewed);

        return "student-course-detail";
    }

    /**
     * Форма отзыва на курс (только если студент записан и ещё не оставлял отзыв)
     */
    @GetMapping("/courses/{courseId}/review")
    public String reviewForm(@PathVariable Long courseId,
                             Authentication authentication,
                             HttpSession session,
                             Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) return "redirect:/auth/login";

        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс не найден"));

        boolean isEnrolled = enrollmentService.getEnrollmentsByStudent(studentId).stream()
                .anyMatch(e -> e.getCourse().getId().equals(courseId));
        if (!isEnrolled) {
            return "redirect:/student/courses/" + courseId + "?error=not_enrolled";
        }

        if (courseReviewService.hasStudentReviewed(courseId, studentId)) {
            return "redirect:/student/courses/" + courseId + "?reviewed=true";
        }

        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("course", course);
        return "student-course-review";
    }

    /**
     * Сохранение отзыва
     */
    @PostMapping("/courses/{courseId}/review")
    public String submitReview(@PathVariable Long courseId,
                               @RequestParam int rating,
                               @RequestParam(required = false) String comment,
                               Authentication authentication) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) return "redirect:/auth/login";

        courseReviewService.addReview(courseId, studentId, rating, comment);
        return "redirect:/student/courses/" + courseId + "?reviewed=true";
    }

    /**
     * Чат - список профессоров для общения
     */
    @GetMapping("/chat")
    public String chat(Authentication authentication, HttpSession session, Model model) {
        Long studentId = getStudentId(authentication);
        if (studentId == null) {
            return "redirect:/auth/login";
        }

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
