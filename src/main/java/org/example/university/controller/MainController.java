package org.example.university.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.university.dto.StudentDTO;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/main")
public class MainController {

    /**
     * GET эндпоинт: /api/main
     * Возвращает простое приветствие
     *
     * Это базовый индикатор работоспособности сервиса
     */
    @GetMapping
    public String getHello() {
        return "Hello World! Система управления университетом работает.";
    }

    /**
     * GET эндпоинт: /api/main/special
     * Возвращает JSON объект студента с фиксированными данными
     *
     * Демонстрирует сериализацию Java объекта в JSON
     */
    @GetMapping("/special")
    public StudentDTO getStudentData() {
        return new StudentDTO(1L, "Камила Танырбергенова", 30);
    }

    /**
     * POST эндпоинт: /api/main/special
     * Принимает параметр 'name' из сұраныста и возвращает StudentDTO с этим именем
     *
     * Демонстрирует обработку динамических параметров и JSON сериализацию
     *
     * Пример использования в Postman:
     * POST http://localhost:8080/api/main/special?name=Асхан+Сатпаев
     */
    @PostMapping("/special")
    public StudentDTO postStudentData(@RequestParam(value = "name", defaultValue = "Аноним") String name) {
        // Создаем объект с переданным именем
        return new StudentDTO(1L, name, 25);
    }

    /**
     * POST эндпоинт: /api/main/serialize
     * Демонстрирует ручную JSON сериализацию с помощью ObjectMapper
     *
     * Это важно знать для больших систем, где требуется контроль над процессом сериализации
     */
    @PostMapping("/serialize")
    public String serializeStudent(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "age", defaultValue = "20") Integer age) {
        try {
            StudentDTO student = new StudentDTO(1L, name, age);
            ObjectMapper mapper = new ObjectMapper();

            // Ручное преобразование объекта в JSON строку
            String jsonString = mapper.writeValueAsString(student);

            return "Сериализованные данные: " + jsonString;
        } catch (Exception e) {
            return "Ошибка при сериализации: " + e.getMessage();
        }
    }

    /**
     * GET эндпоинт: /api/main/info
     * Возвращает информацию о структуре API
     */
    @GetMapping("/info")
    public String getApiInfo() {
        return "REST API для системы управления университетом\n\n" +
               "=== Курсы (Courses) ===\n" +
               "GET /api/courses - Получить все активные курсы\n" +
               "GET /api/courses/all - Получить все курсы\n" +
               "GET /api/courses/{id} - Получить курс по ID\n" +
               "GET /api/courses/department/{department} - Получить курсы по отделению\n" +
               "GET /api/courses/professor/{professorId} - Получить курсы преподавателя\n" +
               "GET /api/courses/university/{universityId} - Получить курсы университета\n" +
               "POST /api/courses - Создать новый курс\n" +
               "PUT /api/courses/{id} - Обновить курс\n" +
               "DELETE /api/courses/{id} - Удалить курс\n" +
               "PATCH /api/courses/{id}/status?active=true/false - Обновить статус курса\n\n" +

               "=== Преподаватели (Professors) ===\n" +
               "GET /api/professors - Получить всех активных преподавателей\n" +
               "GET /api/professors/all - Получить всех преподавателей\n" +
               "GET /api/professors/{id} - Получить преподавателя по ID\n" +
               "GET /api/professors/email/{email} - Получить преподавателя по email\n" +
               "GET /api/professors/department/{department} - Получить преподавателей по отделению\n" +
               "GET /api/professors/university/{universityId} - Получить преподавателей университета\n" +
               "GET /api/professors/search?name=NAME - Поиск преподавателей по имени\n" +
               "POST /api/professors - Создать нового преподавателя\n" +
               "PUT /api/professors/{id} - Обновить преподавателя\n" +
               "DELETE /api/professors/{id} - Удалить преподавателя\n" +
               "PATCH /api/professors/{id}/status?active=true/false - Обновить статус преподавателя\n\n" +

               "=== Университеты (Universities) ===\n" +
               "GET /api/universities - Получить все активные университеты\n" +
               "GET /api/universities/all - Получить все университеты\n" +
               "GET /api/universities/{id} - Получить университет по ID\n" +
               "GET /api/universities/name/{name} - Получить университет по названию\n" +
               "GET /api/universities/city/{city} - Получить университеты по городу\n" +
               "GET /api/universities/country/{country} - Получить университеты по стране\n" +
               "GET /api/universities/search?name=NAME - Поиск университетов по названию\n" +
               "POST /api/universities - Создать новый университет\n" +
               "PUT /api/universities/{id} - Обновить университет\n" +
               "DELETE /api/universities/{id} - Удалить университет\n" +
               "PATCH /api/universities/{id}/status?active=true/false - Обновить статус университета\n\n" +

               "=== Дополнительно ===\n" +
               "GET /api/main - Получить приветствие\n" +
               "GET /api/main/special - Получить данные студента (JSON)\n" +
               "POST /api/main/special?name=ИМЯ - Создать студента с заданным именем\n" +
               "POST /api/main/serialize?name=ИМЯ&age=ВОЗРАСТ - Сериализовать студента";
    }
}

