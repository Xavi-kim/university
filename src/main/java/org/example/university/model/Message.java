package org.example.university.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Message Entity - Личное сообщение между пользователями
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // Отправитель

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // Получатель

    @Column(nullable = false, length = 2000)
    private String content; // Текст сообщения

    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now(); // Время отправки

    @Column(nullable = false)
    private Boolean isRead = false; // Прочитано ли

    @Column
    private LocalDateTime readAt; // Когда прочитано

    // Для группировки сообщений по теме/курсу
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course relatedCourse; // Связанный курс (опционально)

    public Message() {
    }

    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public Course getRelatedCourse() {
        return relatedCourse;
    }

    public void setRelatedCourse(Course relatedCourse) {
        this.relatedCourse = relatedCourse;
    }
}

