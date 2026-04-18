package org.example.university.controller;

import org.example.university.factory.AdvancedCourseFactory;
import org.example.university.factory.CourseFactory;
import org.example.university.factory.StandardCourseFactory;
import org.example.university.model.*;
import org.example.university.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Web Controller для панели преподавателя
 */
@Controller
@RequestMapping("/professor")
public class ProfessorWebController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GradeService gradeService;

    /**
     * Панель преподавателя
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();

        // Получаем профессора по email
        Professor professor = professorService.findByEmail(user.getEmail());

        // Курсы преподавателя
        List<Course> myCourses = courseService.getCoursesByProfessor(professor);

        // Непрочитанные сообщения
        Long unreadCount = messageService.getUnreadCount(user.getId());

        // Домашние задания созданные преподавателем
        List<Homework> myHomeworks = homeworkService.getHomeworksByProfessor(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("professor", professor);
        model.addAttribute("myCourses", myCourses);
        model.addAttribute("unreadCount", unreadCount);
        model.addAttribute("homeworksCount", myHomeworks.size());

        return "professor-dashboard";
    }

    /**
     * Мои курсы
     */
    @GetMapping("/courses")
    public String myCourses(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Professor professor = professorService.findByEmail(user.getEmail());

        List<Course> myCourses = courseService.getCoursesByProfessor(professor);

        model.addAttribute("courses", myCourses);
        return "professor-courses";
    }

    /**
     * Форма создания курса
     */
    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("universities", courseService.getAllUniversities());
        return "professor-create-course";
    }

    /**
     * Создание курса
     */
    @PostMapping("/courses/create")
    public String createCourse(@ModelAttribute Course course, Authentication auth) {
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Professor professor = professorService.findByEmail(user.getEmail());

        if (professor == null) {
            return "redirect:/professor/dashboard?error=no_professor_profile";
        }

        // Factory: продвинутые курсы (level="Advanced") создаём через AdvancedCourseFactory,
        // остальные — через StandardCourseFactory.
        CourseFactory factory = (course.getLevel() != null && "ADVANCED".equalsIgnoreCase(course.getLevel()))
                ? new AdvancedCourseFactory()
                : new StandardCourseFactory();

        Course toSave = factory.createCourse(
                course.getTitle(),
                course.getDescription(),
                course.getDepartment(),
                course.getSemester(),
                professor,
                course.getUniversity()
        );

        // переносим дополнительные поля формы (кроме professor, credits/level могут быть переопределены Advanced фабрикой)
        toSave.setActive(course.isActive());
        if (!(factory instanceof AdvancedCourseFactory)) {
            toSave.setCredits(course.getCredits());
            toSave.setLevel(course.getLevel());
        }
        toSave.setMaxStudents(course.getMaxStudents());
        toSave.setStartDate(course.getStartDate());
        toSave.setEndDate(course.getEndDate());
        toSave.setCategory(course.getCategory());
        toSave.setTags(course.getTags());
        toSave.setImageUrl(course.getImageUrl());

        courseService.saveCourse(toSave);

        return "redirect:/professor/courses?success=created";
    }

    /**
     * Редактирование курса
     */
    @GetMapping("/courses/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Authentication auth, Model model) {
        Course course = courseService.getCourseById(id).orElseThrow();

        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Professor professor = professorService.findByEmail(user.getEmail());

        if (professor == null) {
            return "redirect:/professor/dashboard?error=no_professor_profile";
        }

        if (!course.getProfessor().getId().equals(professor.getId())) {
            return "redirect:/professor/courses?error=access_denied";
        }

        model.addAttribute("course", course);
        model.addAttribute("universities", courseService.getAllUniversities());
        return "professor-edit-course";
    }

    /**
     * Обновление курса
     */
    @PostMapping("/courses/{id}/update")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course updatedCourse, Authentication auth) {
        Course course = courseService.getCourseById(id).orElseThrow();

        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Professor professor = professorService.findByEmail(user.getEmail());

        if (professor == null) {
            return "redirect:/professor/dashboard?error=no_professor_profile";
        }

        if (!course.getProfessor().getId().equals(professor.getId())) {
            return "redirect:/professor/courses?error=access_denied";
        }

        // Обновляем только разрешенные поля
        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());
        course.setDepartment(updatedCourse.getDepartment());
        course.setSemester(updatedCourse.getSemester());
        course.setCredits(updatedCourse.getCredits());
        course.setMaxStudents(updatedCourse.getMaxStudents());
        course.setStartDate(updatedCourse.getStartDate());
        course.setEndDate(updatedCourse.getEndDate());
        course.setCategory(updatedCourse.getCategory());
        course.setLevel(updatedCourse.getLevel());

        courseService.saveCourse(course);

        return "redirect:/professor/courses?success=updated";
    }

    /**
     * Студенты на курсе
     */
    @GetMapping("/courses/{id}/students")
    public String courseStudents(@PathVariable Long id, Authentication auth, Model model) {
        Course course = courseService.getCourseById(id).orElseThrow();

        // Проверка прав
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Professor professor = professorService.findByEmail(user.getEmail());

        if (professor == null) {
            return "redirect:/professor/dashboard?error=no_professor_profile";
        }

        if (!course.getProfessor().getId().equals(professor.getId())) {
            return "redirect:/professor/courses?error=access_denied";
        }

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(course.getId());

        model.addAttribute("course", course);
        model.addAttribute("enrollments", enrollments);
        return "professor-course-students";
    }

    /**
     * Домашние задания курса
     */
    @GetMapping("/courses/{id}/homework")
    public String courseHomework(@PathVariable Long id, Authentication auth, Model model) {
        Course course = courseService.getCourseById(id).orElseThrow();

        // Проверка прав
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Professor professor = professorService.findByEmail(user.getEmail());

        if (professor == null) {
            return "redirect:/professor/dashboard?error=no_professor_profile";
        }

        if (!course.getProfessor().getId().equals(professor.getId())) {
            return "redirect:/professor/courses?error=access_denied";
        }

        List<Homework> homeworks = homeworkService.getHomeworksByCourse(id);

        model.addAttribute("course", course);
        model.addAttribute("homeworks", homeworks);
        return "professor-homework";
    }

    /**
     * Создание ДЗ
     */
    @PostMapping("/courses/{id}/homework/create")
    public String createHomework(@PathVariable Long id,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String dueDate,
                                 @RequestParam(defaultValue = "100") Integer maxPoints,
                                 Authentication auth) {
        Course course = courseService.getCourseById(id).orElseThrow();
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();

        Homework homework = new Homework();
        homework.setTitle(title);
        homework.setDescription(description);
        homework.setCourse(course);
        homework.setDueDate(LocalDateTime.parse(dueDate));
        homework.setMaxPoints(maxPoints);
        homework.setCreatedBy(user);

        homeworkService.createHomework(homework);

        return "redirect:/professor/courses/" + id + "/homework?success=created";
    }

    /**
     * Просмотр сданных работ
     */
    @GetMapping("/homework/{id}/submissions")
    public String homeworkSubmissions(@PathVariable Long id, Authentication auth, Model model) {
        Homework homework = homeworkService.getHomeworkById(id).orElseThrow();

        // Проверка прав
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Professor professor = professorService.findByEmail(user.getEmail());

        if (professor == null) {
            return "redirect:/professor/dashboard?error=no_professor_profile";
        }

        if (!homework.getCourse().getProfessor().getId().equals(professor.getId())) {
            return "redirect:/professor/dashboard?error=access_denied";
        }

        List<HomeworkSubmission> submissions = homeworkService.getSubmissionsForHomework(id);

        model.addAttribute("homework", homework);
        model.addAttribute("submissions", submissions);
        return "professor-grade-homework";
    }

    /**
     * Оценка работы
     */
    @PostMapping("/submissions/{id}/grade")
    public String gradeSubmission(@PathVariable Long id,
                                  @RequestParam Integer points,
                                  @RequestParam String feedback) {
        HomeworkSubmission submission = homeworkService.gradeSubmission(id, points, feedback);

        return "redirect:/professor/homework/" + submission.getHomework().getId() + "/submissions?success=graded";
    }

    /**
     * Выставление оценки студенту
     */
    @PostMapping("/courses/{courseId}/students/{studentId}/grade")
    public String gradeStudent(@PathVariable Long courseId,
                              @PathVariable Long studentId,
                              @RequestParam String letterGrade,
                              @RequestParam Double numericGrade,
                              @RequestParam(required = false) String comments,
                              Authentication auth) {
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();
        Professor professor = professorService.findByEmail(user.getEmail());
        if (professor == null) {
            return "redirect:/professor/dashboard?error=no_professor_profile";
        }

        // Если у текущего пользователя нет профиля профессора — не падаем, а ведём в dashboard с ошибкой.
        // Это защищает редирект /professor/courses/{id}/students от NPE.
        // (Профиль профессора создаётся отдельно и может отсутствовать.)
        //
        // NB: здесь auth не передавался ранее, поэтому используем текущий SecurityContext через параметр не можем.
        // В этой реализации полагаемся на то, что сам endpoint доступен только профессорам,
        // но всё равно проверяем наличие professor-записи по email.

        // Найти enrollment
        Enrollment enrollment = enrollmentService.getEnrollmentsByCourse(courseId).stream()
                .filter(e -> e.getStudent().getId().equals(studentId))
                .findFirst()
                .orElseThrow();

        // Создать Grade
        Grade grade = new Grade();
        grade.setEnrollment(enrollment);
        grade.setLetterGrade(letterGrade);
        grade.setNumericGrade(numericGrade);
        grade.setComments(comments);
        grade.setAssignmentName("Final Grade");
        grade.setAssignmentType("FINAL");

        gradeService.saveGrade(grade);

        return "redirect:/professor/courses/" + courseId + "/students?success=graded";
    }

    /**
     * Сообщения
     */
    @GetMapping("/messages")
    public String messages(Authentication auth, Model model) {
        User user = userService.getUserByEmail(auth.getName()).orElseThrow();

        List<org.example.university.dto.ConversationDto> conversations = messageService.getConversations(user.getId());
        Long unreadCount = messageService.getUnreadCount(user.getId());

        model.addAttribute("conversations", conversations);
        model.addAttribute("unreadCount", unreadCount);
        return "professor-messages";
    }

    /**
     * Отправка нового сообщения (форма "как почта")
     */
    @PostMapping("/messages/send")
    public String sendNewMessage(@RequestParam(required = false) Long recipientId,
                                 @RequestParam(required = false) String recipientEmail,
                                 @RequestParam String content,
                                 Authentication auth) {
        User sender = userService.getUserByEmail(auth.getName()).orElseThrow();

        User recipient = null;
        if (recipientId != null) {
            recipient = userService.getUserById(recipientId).orElse(null);
        }
        if (recipient == null && recipientEmail != null && !recipientEmail.isBlank()) {
            recipient = userService.getUserByEmail(recipientEmail.trim()).orElse(null);
        }
        if (recipient == null) {
            return "redirect:/professor/messages?error=user_not_found";
        }

        Message message = new Message(sender, recipient, content);
        messageService.sendMessage(message);
        return "redirect:/professor/messages/" + recipient.getId();
    }

    /**
     * Диалог со студентом
     */
    @GetMapping("/messages/{userId}")
    public String conversation(@PathVariable Long userId, Authentication auth, Model model) {
        User professor = userService.getUserByEmail(auth.getName()).orElseThrow();
        User other = userService.getUserById(userId).orElseThrow();

        List<Message> conversation = messageService.getConversation(professor.getId(), userId);
        messageService.markConversationAsRead(professor.getId(), userId);

        model.addAttribute("otherUser", other);
        model.addAttribute("messages", conversation);
        model.addAttribute("currentUserId", professor.getId());
        model.addAttribute("backUrl", "/professor/messages");
        model.addAttribute("sendUrl", "/professor/messages/" + userId + "/send");
        return "conversation";
    }

    /**
     * Отправка сообщения в диалоге
     */
    @PostMapping("/messages/{userId}/send")
    public String sendMessage(@PathVariable Long userId,
                             @RequestParam String content,
                             Authentication auth) {
        User professor = userService.getUserByEmail(auth.getName()).orElseThrow();
        User other = userService.getUserById(userId).orElseThrow();
        Message message = new Message(professor, other, content);
        messageService.sendMessage(message);
        return "redirect:/professor/messages/" + userId;
    }
}

