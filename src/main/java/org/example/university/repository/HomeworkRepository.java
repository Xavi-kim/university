package org.example.university.repository;

import org.example.university.model.Homework;
import org.example.university.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByCourse(Course course);
    List<Homework> findByCourseId(Long courseId);
    List<Homework> findByCreatedBy_Id(Long professorId);
}

