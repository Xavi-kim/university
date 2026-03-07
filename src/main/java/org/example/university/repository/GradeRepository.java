package org.example.university.repository;

import org.example.university.model.Enrollment;
import org.example.university.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Grade entity
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * Найти все оценки по регистрации (enrollment)
     */
    List<Grade> findByEnrollment(Enrollment enrollment);

    /**
     * Найти все оценки студента
     */
    @Query("SELECT g FROM Grade g WHERE g.enrollment.student.id = :studentId")
    List<Grade> findByStudentId(Long studentId);

    /**
     * Найти все оценки по курсу
     */
    @Query("SELECT g FROM Grade g WHERE g.enrollment.course.id = :courseId")
    List<Grade> findByCourseId(Long courseId);

    /**
     * Рассчитать средний GPA студента
     */
    @Query("SELECT AVG(g.gpaValue) FROM Grade g WHERE g.enrollment.student.id = :studentId")
    Double calculateAverageGPA(Long studentId);

    /**
     * Найти оценки по типу задания
     */
    List<Grade> findByAssignmentType(String assignmentType);
}

