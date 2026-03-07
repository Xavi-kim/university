package org.example.university.service;

import org.example.university.model.Enrollment;
import org.example.university.model.Grade;
import org.example.university.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for Grade operations (Фаза 2)
 */
@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    /**
     * Получить все оценки
     */
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    /**
     * Получить оценку по ID
     */
    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }

    /**
     * Сохранить оценку
     */
    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    /**
     * Удалить оценку
     */
    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }

    /**
     * Получить все оценки студента
     */
    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    /**
     * Получить все оценки по курсу
     */
    public List<Grade> getGradesByCourse(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }

    /**
     * Получить оценки по регистрации
     */
    public List<Grade> getGradesByEnrollment(Enrollment enrollment) {
        return gradeRepository.findByEnrollment(enrollment);
    }

    /**
     * Рассчитать средний GPA студента
     */
    public Double calculateStudentGPA(Long studentId) {
        Double avgGPA = gradeRepository.calculateAverageGPA(studentId);
        return avgGPA != null ? Math.round(avgGPA * 100.0) / 100.0 : 0.0;
    }

    /**
     * Получить оценки по типу задания
     */
    public List<Grade> getGradesByType(String assignmentType) {
        return gradeRepository.findByAssignmentType(assignmentType);
    }

    /**
     * Рассчитать итоговую оценку студента по курсу
     * (с учётом весов заданий)
     */
    public Double calculateFinalGrade(Long enrollmentId) {
        // TODO: Реализовать расчёт с учётом весов
        return 0.0;
    }
}

