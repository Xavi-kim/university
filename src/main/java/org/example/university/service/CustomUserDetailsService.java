package org.example.university.service;

import org.example.university.model.User;
import org.example.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation
 * Загружает пользователя из базы данных для Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загрузить пользователя по email (используется как username)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 [AUTH] Попытка входа с email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ [AUTH] Пользователь не найден: " + email);
                    return new UsernameNotFoundException(
                            "Пользователь с email " + email + " не найден"
                    );
                });

        System.out.println("✅ [AUTH] Пользователь найден:");
        System.out.println("   ID: " + user.getId());
        System.out.println("   Имя: " + user.getName());
        System.out.println("   Email: " + user.getEmail());
        System.out.println("   Роль: " + user.getRole());
        System.out.println("   Enabled: " + user.isEnabled());
        System.out.println("   Password hash: " + user.getPassword().substring(0, Math.min(30, user.getPassword().length())) + "...");

        // Создаем Spring Security UserDetails из нашей сущности User
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .disabled(!user.isEnabled())
                .build();

        System.out.println("   Authorities: " + userDetails.getAuthorities());
        System.out.println("   Is Enabled: " + userDetails.isEnabled());

        return userDetails;
    }
}

