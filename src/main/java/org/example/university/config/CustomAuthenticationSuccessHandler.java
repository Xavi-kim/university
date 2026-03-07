package org.example.university.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.university.model.User;
import org.example.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom Authentication Success Handler
 * Перенаправляет пользователя в зависимости от его роли после успешного входа
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {

        // Получаем email пользователя
        String email = authentication.getName();

        // Загружаем пользователя из БД
        User user = userService.getUserByEmail(email).orElse(null);

        if (user != null) {
            // Сохраняем данные пользователя в HTTP сессию
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole());

            // Определяем URL для редиректа в зависимости от роли
            String redirectUrl = "/";

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                redirectUrl = "/admin/dashboard";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
                redirectUrl = "/student/dashboard";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROFESSOR"))) {
                redirectUrl = "/professor/dashboard";
            }

            // Перенаправляем пользователя
            response.sendRedirect(redirectUrl);
        } else {
            // Если пользователь не найден, перенаправляем на главную
            response.sendRedirect("/");
        }
    }
}

