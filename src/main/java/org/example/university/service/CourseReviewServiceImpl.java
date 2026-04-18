package org.example.university.service;

import org.example.university.exception.BadRequestException;
import org.example.university.exception.NotFoundException;
import org.example.university.model.Course;
import org.example.university.model.CourseReview;
import org.example.university.model.Enrollment;
import org.example.university.model.User;
import org.example.university.repository.CourseReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CourseReviewServiceImpl implements CourseReviewService {

    @Autowired
    private CourseReviewRepository courseReviewRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Override
    public CourseReview addReview(Long courseId, Long studentId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Рейтинг должен быть от 1 до 5");
        }

        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new NotFoundException("Курс не найден"));

        User student = userService.getUserById(studentId)
                .orElseThrow(() -> new NotFoundException("Студент не найден"));

        boolean enrolled = enrollmentService.getEnrollmentsByStudent(studentId).stream()
                .map(Enrollment::getCourse)
                .anyMatch(c -> c.getId().equals(courseId));
        if (!enrolled) {
            throw new BadRequestException("Можно оставлять отзыв только на курс, на который вы записаны");
        }

        if (courseReviewRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new BadRequestException("Вы уже оставили отзыв на этот курс");
        }

        CourseReview review = new CourseReview(course, student, rating, normalize(comment));
        return courseReviewRepository.save(review);
    }

    @Override
    public List<CourseReview> getReviewsForCourse(Long courseId) {
        List<CourseReview> list = courseReviewRepository.findByCourseId(courseId);
        list.sort(Comparator.comparing(CourseReview::getCreatedAt).reversed());
        return list;
    }

    @Override
    public Double getAverageRating(Long courseId) {
        Double avg = courseReviewRepository.findAverageRatingByCourseId(courseId);
        return avg;
    }

    @Override
    public boolean hasStudentReviewed(Long courseId, Long studentId) {
        if (studentId == null) return false;
        return courseReviewRepository.existsByCourseIdAndStudentId(courseId, studentId);
    }

    private String normalize(String comment) {
        if (comment == null) return null;
        String trimmed = comment.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

