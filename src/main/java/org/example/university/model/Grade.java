package org.example.university.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Grade Entity - Система оценок (Фаза 2)
 * Поддерживает буквенные (A, B, C, D, F) и числовые оценки
 */
@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @JsonIgnoreProperties({"grades", "student", "course"})
    private Enrollment enrollment;

    @Column(name = "letter_grade", length = 2)
    private String letterGrade; // A, A-, B+, B, B-, C+, C, C-, D, F

    @Column(name = "numeric_grade")
    private Double numericGrade; // 0.0 - 100.0

    @Column(name = "gpa_value")
    private Double gpaValue; // 4.0, 3.7, 3.3, 3.0, 2.7, 2.3, 2.0, 1.7, 1.3, 1.0, 0.0

    @Column(name = "assignment_name", length = 255)
    private String assignmentName; // Название задания/экзамена

    @Column(name = "assignment_type", length = 50)
    private String assignmentType; // HOMEWORK, QUIZ, MIDTERM, FINAL, PROJECT

    @Column(name = "weight")
    private Double weight; // Вес оценки (например, 0.3 для 30%)

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments; // Комментарии преподавателя

    @Column(name = "graded_at", nullable = false)
    private LocalDateTime gradedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "graded_by")
    @JsonIgnoreProperties({"courses"})
    private Professor gradedBy; // Кто поставил оценку

    public Grade() {
        this.gradedAt = LocalDateTime.now();
    }

    public Grade(Enrollment enrollment, String letterGrade, Double numericGrade) {
        this.enrollment = enrollment;
        this.letterGrade = letterGrade;
        this.numericGrade = numericGrade;
        this.gpaValue = calculateGPA(letterGrade);
        this.gradedAt = LocalDateTime.now();
    }

    /**
     * Рассчитать GPA по буквенной оценке
     */
    private Double calculateGPA(String letterGrade) {
        return switch (letterGrade) {
            case "A" -> 4.0;
            case "A-" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "B-" -> 2.7;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "C-" -> 1.7;
            case "D+" -> 1.3;
            case "D" -> 1.0;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
        this.gpaValue = calculateGPA(letterGrade);
    }

    public Double getNumericGrade() {
        return numericGrade;
    }

    public void setNumericGrade(Double numericGrade) {
        this.numericGrade = numericGrade;
    }

    public Double getGpaValue() {
        return gpaValue;
    }

    public void setGpaValue(Double gpaValue) {
        this.gpaValue = gpaValue;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(String assignmentType) {
        this.assignmentType = assignmentType;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public Professor getGradedBy() {
        return gradedBy;
    }

    public void setGradedBy(Professor gradedBy) {
        this.gradedBy = gradedBy;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", letterGrade='" + letterGrade + '\'' +
                ", numericGrade=" + numericGrade +
                ", gpaValue=" + gpaValue +
                ", assignmentName='" + assignmentName + '\'' +
                '}';
    }
}

