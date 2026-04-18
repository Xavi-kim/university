package org.example.university.repository;

import org.example.university.model.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    List<CourseReview> findByCourseId(Long courseId);

    Optional<CourseReview> findByCourseIdAndStudentId(Long courseId, Long studentId);

    @Query("select avg(cr.rating) from CourseReview cr where cr.course.id = :courseId")
    Double findAverageRatingByCourseId(@Param("courseId") Long courseId);

    @Query("select count(cr) > 0 from CourseReview cr where cr.course.id = :courseId and cr.student.id = :studentId")
    boolean existsByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    @Query("select cr from CourseReview cr where cr.course.id = :courseId order by cr.createdAt desc")
    List<CourseReview> findLatestByCourseId(@Param("courseId") Long courseId);
}

