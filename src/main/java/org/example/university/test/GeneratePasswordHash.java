package org.example.university.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswordHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "teacher123";
        String hash = encoder.encode(password);

        System.out.println("========================================");
        System.out.println("Генерация BCrypt хеша для пароля");
        System.out.println("========================================");
        System.out.println("Пароль: " + password);
        System.out.println("BCrypt хеш: " + hash);
        System.out.println("========================================");
        System.out.println();
        System.out.println("SQL для обновления:");
        System.out.println("UPDATE users SET password = '" + hash + "' WHERE email = 'teacher@university.kz';");
        System.out.println("========================================");

        // Проверка что хеш работает
        boolean matches = encoder.matches(password, hash);
        System.out.println();
        System.out.println("Проверка: " + (matches ? "✅ Хеш корректный" : "❌ Ошибка"));

        // Проверка существующего хеша
        String existingHash = "$2a$10$N9qo8uLOickgx2ZMRZoMye7I4grqeyNnTu4lW5TUyqYN7TXeGJXOy";
        boolean existingMatches = encoder.matches(password, existingHash);
        System.out.println("Проверка существующего хеша: " + (existingMatches ? "✅ Совпадает" : "❌ НЕ совпадает"));
    }
}

