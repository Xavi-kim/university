package org.example.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.awt.Desktop;
import java.net.URI;

/**
 * Spring Boot Application for University Management System
 *
 * This is the main entry point for the Spring Boot application.
 * It configures and starts the embedded Tomcat server.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        Environment env = ctx.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        String url = "http://localhost:" + port;

        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║        🎓 UNIVERSITY MANAGEMENT SYSTEM               ║");
        System.out.println("║                                                      ║");
        System.out.println("║   ✅ Приложение запущено успешно!                    ║");
        System.out.println("║                                                      ║");
        System.out.println("║   🌐 Сайт:     " + url + "                    ║");
        System.out.println("║   👑 Админ:    " + url + "/auth/login          ║");
        System.out.println("║   🗄  База:     PostgreSQL (university_db)           ║");
        System.out.println("║                                                      ║");
        System.out.println("║   🔑 Аккаунты:                                       ║");
        System.out.println("║      admin@university.kz  / admin123                 ║");
        System.out.println("║      asel@student.kz      / 123456                   ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("\n");

        // Автоматически открываем браузер
        openBrowser(url);
    }

    private static void openBrowser(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", url});
            } else if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", url});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
            }
            System.out.println("🚀 Браузер открывается автоматически: " + url);
        } catch (Exception e) {
            System.out.println("⚠️  Не удалось открыть браузер автоматически.");
            System.out.println("   Откройте вручную: " + url);
        }
    }
}
