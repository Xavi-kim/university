package org.example.university.controller;

import org.example.university.model.University;
import org.example.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST API Controller for University management
 */
@RestController
@RequestMapping("/api/universities")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    /**
     * GET: Получить все активные университеты
     */
    @GetMapping
    public ResponseEntity<List<University>> getAllActiveUniversities() {
        return ResponseEntity.ok(universityService.getAllActiveUniversities());
    }

    /**
     * GET: Получить все университеты (включая неактивные)
     */
    @GetMapping("/all")
    public ResponseEntity<List<University>> getAllUniversities() {
        return ResponseEntity.ok(universityService.getAllUniversities());
    }

    /**
     * GET: Получить университет по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable Long id) {
        Optional<University> university = universityService.getUniversityById(id);
        return university.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET: Получить университет по названию
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<University> getUniversityByName(@PathVariable String name) {
        Optional<University> university = universityService.getUniversityByName(name);
        return university.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET: Получить университеты по городу
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<University>> getUniversitiesByCity(@PathVariable String city) {
        return ResponseEntity.ok(universityService.getUniversitiesByCity(city));
    }

    /**
     * GET: Получить университеты по стране
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<University>> getUniversitiesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(universityService.getUniversitiesByCountry(country));
    }

    /**
     * GET: Поиск университетов по названию
     */
    @GetMapping("/search")
    public ResponseEntity<List<University>> searchUniversities(@RequestParam String name) {
        return ResponseEntity.ok(universityService.searchUniversities(name));
    }

    /**
     * POST: Создать новый университет
     */
    @PostMapping
    public ResponseEntity<University> createUniversity(@Valid @RequestBody University university) {
        University savedUniversity = universityService.saveUniversity(university);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUniversity);
    }

    /**
     * PUT: Обновить университет
     */
    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable Long id, @Valid @RequestBody University universityDetails) {
        Optional<University> universityOpt = universityService.getUniversityById(id);
        if (universityOpt.isPresent()) {
            University university = universityOpt.get();
            university.setName(universityDetails.getName());
            university.setAddress(universityDetails.getAddress());
            university.setCity(universityDetails.getCity());
            university.setCountry(universityDetails.getCountry());
            university.setWebsite(universityDetails.getWebsite());
            university.setDescription(universityDetails.getDescription());
            University updatedUniversity = universityService.saveUniversity(university);
            return ResponseEntity.ok(updatedUniversity);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE: Удалить университет
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        if (universityService.getUniversityById(id).isPresent()) {
            universityService.deleteUniversity(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PATCH: Обновить статус университета
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<University> updateUniversityStatus(@PathVariable Long id, @RequestParam boolean active) {
        University updatedUniversity = universityService.updateUniversityStatus(id, active);
        if (updatedUniversity != null) {
            return ResponseEntity.ok(updatedUniversity);
        }
        return ResponseEntity.notFound().build();
    }
}

