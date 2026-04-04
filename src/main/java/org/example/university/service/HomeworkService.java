package org.example.university.service;

import org.example.university.model.Homework;
import org.example.university.model.HomeworkSubmission;
import org.example.university.model.Course;
import org.example.university.model.User;
import org.example.university.repository.HomeworkRepository;
import org.example.university.repository.HomeworkSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HomeworkService {

    @Autowired
    private HomeworkRepository homeworkRepository;

    @Autowired
    private HomeworkSubmissionRepository submissionRepository;

    // Создать домашнее задание
    public Homework createHomework(Homework homework) {
        return homeworkRepository.save(homework);
    }

    // Получить все ДЗ для курса
    public List<Homework> getHomeworksByCourse(Long courseId) {
        return homeworkRepository.findByCourseId(courseId);
    }

    // Получить все ДЗ созданные профессором
    public List<Homework> getHomeworksByProfessor(Long professorId) {
        return homeworkRepository.findByCreatedBy_Id(professorId);
    }

    // Получить ДЗ по ID
    public Optional<Homework> getHomeworkById(Long id) {
        return homeworkRepository.findById(id);
    }

    // Обновить ДЗ
    public Homework updateHomework(Homework homework) {
        return homeworkRepository.save(homework);
    }

    // Удалить ДЗ
    public void deleteHomework(Long id) {
        homeworkRepository.deleteById(id);
    }

    // Сдать работу
    public HomeworkSubmission submitHomework(HomeworkSubmission submission) {
        // Проверка на опоздание
        if (LocalDateTime.now().isAfter(submission.getHomework().getDueDate())) {
            submission.setStatus(HomeworkSubmission.SubmissionStatus.LATE);
        }
        return submissionRepository.save(submission);
    }

    // Получить сдачи для ДЗ
    public List<HomeworkSubmission> getSubmissionsForHomework(Long homeworkId) {
        return submissionRepository.findByHomework_Id(homeworkId);
    }

    // Получить все сдачи студента
    public List<HomeworkSubmission> getStudentSubmissions(Long studentId) {
        return submissionRepository.findByStudent_Id(studentId);
    }

    // Получить сдачу студента для конкретного ДЗ
    public Optional<HomeworkSubmission> getStudentSubmission(Long homeworkId, Long studentId) {
        return submissionRepository.findByHomework_IdAndStudent_Id(homeworkId, studentId);
    }

    // Оценить работу
    public HomeworkSubmission gradeSubmission(Long submissionId, Integer points, String feedback) {
        HomeworkSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Сдача не найдена"));

        submission.setPoints(points);
        submission.setFeedback(feedback);
        submission.setGradedAt(LocalDateTime.now());
        submission.setStatus(HomeworkSubmission.SubmissionStatus.GRADED);

        return submissionRepository.save(submission);
    }

    // Получить все ДЗ студента для курса
    public List<HomeworkSubmission> getCourseSubmissionsForStudent(Long courseId, Long studentId) {
        return submissionRepository.findByHomework_Course_IdAndStudent_Id(courseId, studentId);
    }

    // Получить все ДЗ в системе
    public List<Homework> getAllHomeworks() {
        return homeworkRepository.findAll();
    }
}
