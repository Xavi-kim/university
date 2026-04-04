package org.example.university.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User Entity for University Management System
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не должно быть пусто")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email не должен быть пусто")
    @Email(message = "Email должен быть корректным")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Пароль не должен быть пусто")
    @Column(nullable = false, length = 255)  // BCrypt хеш = 60 символов, устанавливаем с запасом
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String role = "STUDENT"; // STUDENT, ADMIN, PROFESSOR

    @Column(nullable = false)
    private boolean enabled = true;

    // ===== ФАЗА 2: Расширение профиля =====

    @Column(name = "avatar_url")
    private String avatarUrl; // URL аватара пользователя

    @Column(name = "phone_number", length = 20)
    private String phoneNumber; // Телефон

    @Column(name = "bio", length = 500)
    private String bio; // Краткая биография

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate; // Дата регистрации (устанавливается в конструкторе)

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false; // Верифицирован ли email

    @Column(name = "verification_token")
    private String verificationToken; // Токен для верификации email

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Enrollment> enrollments = new ArrayList<>();

    public User() {
        this.registrationDate = LocalDateTime.now();
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "STUDENT";
        this.enabled = true;
        this.registrationDate = LocalDateTime.now();
    }

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = true;
        this.registrationDate = LocalDateTime.now();
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

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    // Альтернативный getter для совместимости
    public Boolean getEnabled() {
        return enabled;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    // ===== ФАЗА 2: Геттеры и сеттеры для профиля =====

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }
}

