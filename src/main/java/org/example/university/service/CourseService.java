package org.example.university.service;

import org.example.university.model.Course;
import org.example.university.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for Course operations
 */
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Получить все активные курсы
     */
    public List<Course> getAllActiveCourses() {
        return courseRepository.findByActiveTrue();
    }

    /**
     * Получить все курсы
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Получить курс по ID
     */
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    /**
     * Получить курсы по отделению
     */
    public List<Course> getCoursesByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }

    /**
     * Получить курсы по ID преподавателя
     */
    public List<Course> getCoursesByProfessorId(Long professorId) {
        return courseRepository.findByProfessorId(professorId);
    }

    /**
     * Получить курсы по ID университета
     */
    public List<Course> getCoursesByUniversityId(Long universityId) {
        return courseRepository.findByUniversityId(universityId);
    }

    /**
     * Сохранить курс
     */
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Удалить курс
     */
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * Обновить статус курса
     */
    public Course updateCourseStatus(Long id, boolean active) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setActive(active);
            return courseRepository.save(course);
        }
        return null;
    }
}

