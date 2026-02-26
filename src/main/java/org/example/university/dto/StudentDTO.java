package org.example.university.dto;

/**
 * Student DTO (Data Transfer Object)
 * Используется для передачи данных о студентах между фронтенд и бэкенд
 * Это не Entity (модель БД), а контейнер для информации
 */
public class StudentDTO {
    private Long id;
    private String fullname;
    private Integer age;

    // Конструкторы
    public StudentDTO() {
    }

    public StudentDTO(Long id, String fullname, Integer age) {
        this.id = id;
        this.fullname = fullname;
        this.age = age;
    }

    // Getters и Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", age=" + age +
                '}';
    }
}

