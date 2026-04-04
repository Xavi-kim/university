package org.example.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Исправляет структуру таблицы users, добавляя недостающие столбцы
 */
@Component
@Order(0) // Выполняется перед DataInitializer
public class DatabaseSchemaFixer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔧 [DB FIX] Проверка и исправление структуры таблицы users...");

        try {
            // Добавляем email_verified
            executeIfColumnNotExists("email_verified",
                "ALTER TABLE users ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT false");

            // Добавляем verification_token
            executeIfColumnNotExists("verification_token",
                "ALTER TABLE users ADD COLUMN verification_token VARCHAR(255)");

            // Добавляем avatar_url
            executeIfColumnNotExists("avatar_url",
                "ALTER TABLE users ADD COLUMN avatar_url VARCHAR(255)");

            // Добавляем phone_number
            executeIfColumnNotExists("phone_number",
                "ALTER TABLE users ADD COLUMN phone_number VARCHAR(20)");

            // Добавляем bio
            executeIfColumnNotExists("bio",
                "ALTER TABLE users ADD COLUMN bio VARCHAR(500)");

            // Добавляем registration_date
            executeIfColumnNotExists("registration_date",
                "ALTER TABLE users ADD COLUMN registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");

            System.out.println("✅ [DB FIX] Структура таблицы users проверена и исправлена");

        } catch (Exception e) {
            System.out.println("⚠️ [DB FIX] Ошибка при исправлении структуры: " + e.getMessage());
            // Не прерываем запуск приложения
        }
    }

    private void executeIfColumnNotExists(String columnName, String sql) {
        try {
            // Проверяем существует ли столбец
            String checkSql = "SELECT column_name FROM information_schema.columns " +
                             "WHERE table_name = 'users' AND column_name = ?";

            var result = jdbcTemplate.queryForList(checkSql, String.class, columnName);

            if (result.isEmpty()) {
                System.out.println("   Добавление столбца: " + columnName);
                jdbcTemplate.execute(sql);
                System.out.println("   ✅ Столбец " + columnName + " добавлен");
            } else {
                System.out.println("   ✓ Столбец " + columnName + " уже существует");
            }
        } catch (Exception e) {
            System.out.println("   ⚠️ Ошибка при добавлении столбца " + columnName + ": " + e.getMessage());
        }
    }
}

