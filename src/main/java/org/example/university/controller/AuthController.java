package org.example.university.controller;

import org.example.university.model.User;
import org.example.university.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Controller for Authentication (Login/Register)
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Страница входа
     */
    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("error", "Неверный email или пароль");
        }
        return "login";
    }

    /**
     * Обработка входа
     */
    @PostMapping("/login")
    public String login(@RequestParam String email,
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        Optional<User> userOpt = userService.getUserByEmail(email);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Пользователь не найден");
            model.addAttribute("email", email);
            return "login";
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Неверный пароль");
            model.addAttribute("email", email);
            return "login";
        }

        // Сохранение пользователя в сессии
        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userRole", user.getRole());

        // Редирект в зависимости от роли
        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/student/dashboard";
        }
    }

    /**
     * Страница регистрации
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    /**
     * Обработка регистрации
     */
    @PostMapping("/register")
    public String register(@RequestParam String name,
                          @RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          HttpSession session,
                          Model model) {
        // Валидация
        if (name == null || name.trim().isEmpty()) {
            model.addAttribute("error", "Имя обязательно");
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            return "register";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "Пароль должен содержать минимум 6 символов");
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            return "register";
        }

        // Проверка существования пользователя
        if (userService.getUserByEmail(email).isPresent()) {
            model.addAttribute("error", "Пользователь с таким email уже существует");
            model.addAttribute("name", name);
            return "register";
        }

        // Создание пользователя
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("STUDENT"); // По умолчанию роль студента
        user.setEnabled(true);

        user = userService.saveUser(user);

        // Автоматический вход
        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userRole", user.getRole());

        return "redirect:/student/dashboard";
    }

    /**
     * Выход
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}

