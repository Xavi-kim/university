package org.example.university.service;

import org.example.university.model.User;

/**
 * Сервис для отправки email-уведомлений пользователям.
 * ЛР №10 — отправка email при регистрации и смене пароля.
 */
public interface EmailService {

    /**
     * Базовый метод отправки простого текстового письма.
     */
    void sendEmail(String to, String subject, String text);

    /**
     * Письмо после успешной регистрации пользователя.
     */
    void sendRegistrationEmail(User user);

    /**
     * Уведомление о смене пароля.
     */
    void sendPasswordChangeEmail(User user);
}

