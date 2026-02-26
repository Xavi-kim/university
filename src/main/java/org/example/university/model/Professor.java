package org.example.university.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Professor Entity for University Management System
 */
@Entity
@Table(name = "professors")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя преподавателя не должно быть пусто")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email не должен быть пусто")
    @Email(message = "Email должен быть корректным")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id")
    @JsonIgnoreProperties({"professors", "courses", "hibernate_lazy_initializer", "handler"})
    private University university;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"professor", "university", "hibernate_lazy_initializer", "handler"})
    private List<Course> courses = new ArrayList<>();

    @Column(nullable = false)
    private boolean active = true;

    public Professor() {
    }

    public Professor(String name, String email, String department, University university) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.university = university;
        this.active = true;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getBio() {
        return bio;
    }

    public University getUniversity() {
        return university;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public boolean isActive() {
        return active;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", active=" + active +
                '}';
    }
}

