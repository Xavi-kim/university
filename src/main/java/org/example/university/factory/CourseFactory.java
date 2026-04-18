package org.example.university.factory;

import org.example.university.model.Course;
import org.example.university.model.Professor;
import org.example.university.model.University;

/**
 * Factory для создания курсов.
 */
public interface CourseFactory {

    Course createCourse(String title,
                        String description,
                        String department,
                        String semester,
                        Professor professor,
                        University university);
}

