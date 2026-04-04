package org.example.university.repository;

import org.example.university.model.HomeworkSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkSubmissionRepository extends JpaRepository<HomeworkSubmission, Long> {
    List<HomeworkSubmission> findByHomework_Id(Long homeworkId);
    List<HomeworkSubmission> findByStudent_Id(Long studentId);
    Optional<HomeworkSubmission> findByHomework_IdAndStudent_Id(Long homeworkId, Long studentId);
    List<HomeworkSubmission> findByHomework_Course_IdAndStudent_Id(Long courseId, Long studentId);
}

