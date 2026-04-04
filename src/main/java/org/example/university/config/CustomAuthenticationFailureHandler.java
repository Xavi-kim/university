package org.example.university.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Custom Authentication Failure Handler
 * Обрабатывает ошибки аутентификации с детальным логированием
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                       HttpServletResponse response,
                                       AuthenticationException exception) throws IOException, ServletException {

        System.out.println("❌ [AUTH FAILURE] Ошибка аутентификации:");
        System.out.println("   Email: " + request.getParameter("email"));
        System.out.println("   Exception: " + exception.getClass().getSimpleName());
        System.out.println("   Message: " + exception.getMessage());

        String errorMessage = "Неверный email или пароль";

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Пользователь не найден";
            System.out.println("   Причина: Пользователь не существует в базе данных");
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Неверный email или пароль";
            System.out.println("   Причина: Неверный пароль или email");
        } else if (exception instanceof DisabledException) {
            errorMessage = "Аккаунт отключен";
            System.out.println("   Причина: Аккаунт деактивирован (enabled=false)");
        }

        System.out.println("   Redirect: /auth/login?error=true");
        System.out.println();

        // Перенаправляем на страницу входа с параметром error
        response.sendRedirect("/auth/login?error=true&message=" +
            URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
    }
}

