package org.example.university.repository;

import org.example.university.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Course entity
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDepartment(String department);
    List<Course> findByProfessorId(Long professorId);
    List<Course> findByUniversityId(Long universityId);
    List<Course> findByActiveTrue();
}

