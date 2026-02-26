package org.example.university.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Course Entity for University Management System
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название курса не должно быть пусто")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Описание не должно быть пусто")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Отделение не должно быть пусто")
    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String semester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id")
    @JsonIgnoreProperties({"courses", "hibernate_lazy_initializer", "handler"})
    private Professor professor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id")
    @JsonIgnoreProperties({"courses", "professors", "hibernate_lazy_initializer", "handler"})
    private University university;

    @Column(nullable = false)
    private boolean active = true;

    public Course() {
    }

    public Course(String title, String description, String department, String semester, Professor professor, University university) {
        this.title = title;
        this.description = description;
        this.department = department;
        this.semester = semester;
        this.professor = professor;
        this.university = university;
        this.active = true;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDepartment() {
        return department;
    }

    public String getSemester() {
        return semester;
    }

    public Professor getProfessor() {
        return professor;
    }

    public University getUniversity() {
        return university;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", department='" + department + '\'' +
                ", semester='" + semester + '\'' +
                ", active=" + active +
                '}';
    }
}

