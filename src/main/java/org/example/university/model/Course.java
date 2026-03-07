package org.example.university.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

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

    // ===== ФАЗА 2: Расширение курсов =====

    @Column(name = "credits")
    private Integer credits; // Количество кредитов (3, 4, 5)

    @Column(name = "max_students")
    private Integer maxStudents; // Максимальное количество студентов

    @Column(name = "start_date")
    private LocalDate startDate; // Дата начала курса

    @Column(name = "end_date")
    private LocalDate endDate; // Дата окончания курса

    @Column(name = "category", length = 100)
    private String category; // Категория (например: "Программирование", "Математика")

    @Column(name = "tags", length = 255)
    private String tags; // Теги через запятую: "Java,Spring,Backend"

    @Column(name = "image_url")
    private String imageUrl; // URL обложки курса

    @Column(name = "level", length = 50)
    private String level = "BEGINNER"; // Уровень: BEGINNER, INTERMEDIATE, ADVANCED

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

    // ===== ФАЗА 2: Геттеры и сеттеры =====

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", department='" + department + '\'' +
                ", semester='" + semester + '\'' +
                ", credits=" + credits +
                ", level='" + level + '\'' +
                ", active=" + active +
                '}';
    }
}

