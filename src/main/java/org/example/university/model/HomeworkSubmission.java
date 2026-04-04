package org.example.university.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * HomeworkSubmission Entity - Сданная работа студента
 */
@Entity
@Table(name = "homework_submissions")
public class HomeworkSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "homework_id", nullable = false)
    private Homework homework; // Какое задание

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student; // Кто сдал

    @Column(length = 2000)
    private String content; // Текст ответа

    @Column
    private String fileUrl; // Ссылка на файл (если есть)

    @Column(nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now(); // Когда сдано

    @Column
    private Integer points; // Баллы (null если не проверено)

    @Column(length = 1000)
    private String feedback; // Комментарий преподавателя

    @Column
    private LocalDateTime gradedAt; // Когда проверено

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status = SubmissionStatus.SUBMITTED; // Статус

    public enum SubmissionStatus {
        SUBMITTED,  // Сдано, ожидает проверки
        GRADED,     // Проверено
        LATE        // Сдано с опозданием
    }

    public HomeworkSubmission() {
    }

    public HomeworkSubmission(Homework homework, User student, String content) {
        this.homework = homework;
        this.student = student;
        this.content = content;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
}

