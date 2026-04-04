package org.example.university.controller;

import org.example.university.model.User;
import org.example.university.service.GradeService;
import org.example.university.service.UserService;
import org.example.university.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Profile Controller - Управление профилем пользователя (Фаза 2)
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private EmailService emailService;

    /**
     * Страница профиля
     */
    @GetMapping
    public String profilePage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);

        // Статистика для студента
        if ("STUDENT".equals(user.getRole())) {
            int coursesCount = user.getEnrollments().size();
            Double avgGPA = gradeService.calculateStudentGPA(user.getId());

            model.addAttribute("coursesCount", coursesCount);
            model.addAttribute("avgGPA", avgGPA);
        }

        return "profile";
    }

    /**
     * Страница редактирования профиля
     */
    @GetMapping("/edit")
    public String editProfilePage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        return "profile-edit";
    }

    /**
     * Обновление профиля
     */
    @PostMapping("/update")
    public String updateProfile(Authentication authentication,
                               @RequestParam String name,
                               @RequestParam(required = false) String phoneNumber,
                               @RequestParam(required = false) String bio,
                               @RequestParam(required = false) String avatarUrl,
                               Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/auth/login";
        }

        // Обновляем данные
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setBio(bio);
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            user.setAvatarUrl(avatarUrl);
        }

        userService.saveUser(user);

        model.addAttribute("success", "Профиль успешно обновлён!");
        return "redirect:/profile";
    }

    /**
     * Страница смены пароля
     */
    @GetMapping("/change-password")
    public String changePasswordPage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }
        return "profile-change-password";
    }

    /**
     * Смена пароля
     */
    @PostMapping("/change-password")
    public String changePassword(Authentication authentication,
                                @RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        if (authentication == null) {
            return "redirect:/auth/login";
        }

        String email = authentication.getName();
        User user = userService.getUserByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/auth/login";
        }

        // Проверка текущего пароля
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Неверный текущий пароль");
            return "profile-change-password";
        }

        // Проверка совпадения новых паролей
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Новые пароли не совпадают");
            return "profile-change-password";
        }

        // Проверка длины пароля
        if (newPassword.length() < 6) {
            model.addAttribute("error", "Пароль должен содержать минимум 6 символов");
            return "profile-change-password";
        }

        // Обновляем пароль
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        // Отправляем уведомление на email (если сервис доступен)
        if (emailService != null) {
            emailService.sendPasswordChangeEmail(user);
        }

        model.addAttribute("success", "Пароль успешно изменён!");
        return "redirect:/profile";
    }
}
