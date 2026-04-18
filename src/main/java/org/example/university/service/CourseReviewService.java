package org.example.university.service;

import org.example.university.model.CourseReview;

import java.util.List;

public interface CourseReviewService {

    CourseReview addReview(Long courseId, Long studentId, int rating, String comment);

    List<CourseReview> getReviewsForCourse(Long courseId);

    Double getAverageRating(Long courseId);

    boolean hasStudentReviewed(Long courseId, Long studentId);
}

