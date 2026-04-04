package org.example.university.service;

import org.example.university.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Dev-реализация: не отправляет реальные письма, только логирует.
 * Удобно для разработки и тестирования без SMTP.
 */
@Service
@Profile("dev")
public class DevEmailService implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(DevEmailService.class);

    @Override
    public void sendEmail(String to, String subject, String text) {
        log.info("[DEV MOCK EMAIL] To: {} | Subject: {} | Text: {}", to, subject, text);
    }

    @Override
    public void sendRegistrationEmail(User user) {
        log.info("[DEV MOCK EMAIL] Registration email for user {} <{}>", user.getName(), user.getEmail());
    }

    @Override
    public void sendPasswordChangeEmail(User user) {
        log.info("[DEV MOCK EMAIL] Password change email for user {} <{}>", user.getName(), user.getEmail());
    }
}

