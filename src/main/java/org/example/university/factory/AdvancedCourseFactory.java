package org.example.university.factory;

import org.example.university.model.Course;
import org.example.university.model.Professor;
import org.example.university.model.University;

/**
 * Продвинутый курс (credits=6, level="Advanced").
 */
public class AdvancedCourseFactory implements CourseFactory {

    @Override
    public Course createCourse(String title,
                               String description,
                               String department,
                               String semester,
                               Professor professor,
                               University university) {
        Course course = new Course(title, description, department, semester, professor, university);
        course.setCredits(6);
        course.setLevel("Advanced");
        return course;
    }
}

