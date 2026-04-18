package org.example.university.factory;

import org.example.university.model.Course;
import org.example.university.model.Professor;
import org.example.university.model.University;

/**
 * Обычный курс без специальных настроек.
 */
public class StandardCourseFactory implements CourseFactory {

    @Override
    public Course createCourse(String title,
                               String description,
                               String department,
                               String semester,
                               Professor professor,
                               University university) {
        return new Course(title, description, department, semester, professor, university);
    }
}

