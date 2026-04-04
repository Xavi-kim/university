package org.example.university.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Homework Entity - Домашнее задание для курса
 */
@Entity
@Table(name = "homework")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Название задания

    @Column(length = 2000)
    private String description; // Описание задания

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // К какому курсу относится

    @Column(nullable = false)
    private LocalDateTime dueDate; // Срок сдачи

    @Column(nullable = false)
    private Integer maxPoints = 100; // Максимальный балл

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Дата создания

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy; // Кто создал (профессор)

    @OneToMany(mappedBy = "homework", cascade = CascadeType.ALL)
    private List<HomeworkSubmission> submissions = new ArrayList<>(); // Сданные работы

    public Homework() {
    }

    public Homework(String title, String description, Course course, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.course = course;
        this.dueDate = dueDate;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<HomeworkSubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<HomeworkSubmission> submissions) {
        this.submissions = submissions;
    }
}

