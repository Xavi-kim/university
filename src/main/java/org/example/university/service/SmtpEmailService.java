package org.example.university.service;

import org.example.university.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Реальная отправка email через SMTP (prod / default профиль).
 */
@Service
@Profile("!dev")
public class SmtpEmailService implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailService.class);

    private final JavaMailSender mailSender;

    @Autowired
    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email sent to {} with subject '{}'", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Override
    public void sendRegistrationEmail(User user) {
        String subject = "Добро пожаловать в University Management System";
        String text = "Здравствуйте, " + (user.getName() != null ? user.getName() : "пользователь") + "!\n\n" +
                "Вы успешно зарегистрировались в системе University Management System.\n" +
                "Теперь вы можете войти, используя свой email: " + user.getEmail() + "\n\n" +
                "Если это были не вы, немедленно свяжитесь с администратором.";
        sendEmail(user.getEmail(), subject, text);
    }

    @Override
    public void sendPasswordChangeEmail(User user) {
        String subject = "Изменение пароля в University Management System";
        String text = "Здравствуйте, " + (user.getName() != null ? user.getName() : "пользователь") + "!\n\n" +
                "Ваш пароль в системе University Management System был успешно изменён.\n" +
                "Если вы не выполняли это действие, немедленно свяжитесь с поддержкой или администратором.";
        sendEmail(user.getEmail(), subject, text);
    }
}

