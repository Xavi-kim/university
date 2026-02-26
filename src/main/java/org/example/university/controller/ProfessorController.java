package org.example.university.controller;

import org.example.university.model.Professor;
import org.example.university.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST API Controller for Professor management
 */
@RestController
@RequestMapping("/api/professors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    /**
     * GET: Получить всех активных преподавателей
     */
    @GetMapping
    public ResponseEntity<List<Professor>> getAllActiveProfessors() {
        return ResponseEntity.ok(professorService.getAllActiveProfessors());
    }

    /**
     * GET: Получить всех преподавателей (включая неактивных)
     */
    @GetMapping("/all")
    public ResponseEntity<List<Professor>> getAllProfessors() {
        return ResponseEntity.ok(professorService.getAllProfessors());
    }

    /**
     * GET: Получить преподавателя по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        Optional<Professor> professor = professorService.getProfessorById(id);
        return professor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET: Получить преподавателя по email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Professor> getProfessorByEmail(@PathVariable String email) {
        Optional<Professor> professor = professorService.getProfessorByEmail(email);
        return professor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET: Получить преподавателей по отделению
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Professor>> getProfessorsByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(professorService.getProfessorsByDepartment(department));
    }

    /**
     * GET: Получить преподавателей по университету
     */
    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<Professor>> getProfessorsByUniversity(@PathVariable Long universityId) {
        return ResponseEntity.ok(professorService.getProfessorsByUniversity(universityId));
    }

    /**
     * GET: Поиск преподавателей по имени
     */
    @GetMapping("/search")
    public ResponseEntity<List<Professor>> searchProfessors(@RequestParam String name) {
        return ResponseEntity.ok(professorService.searchProfessors(name));
    }

    /**
     * POST: Создать нового преподавателя
     */
    @PostMapping
    public ResponseEntity<Professor> createProfessor(@Valid @RequestBody Professor professor) {
        Professor savedProfessor = professorService.saveProfessor(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfessor);
    }

    /**
     * PUT: Обновить преподавателя
     */
    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Long id, @Valid @RequestBody Professor professorDetails) {
        Optional<Professor> professorOpt = professorService.getProfessorById(id);
        if (professorOpt.isPresent()) {
            Professor professor = professorOpt.get();
            professor.setName(professorDetails.getName());
            professor.setEmail(professorDetails.getEmail());
            professor.setDepartment(professorDetails.getDepartment());
            professor.setBio(professorDetails.getBio());
            professor.setUniversity(professorDetails.getUniversity());
            Professor updatedProfessor = professorService.saveProfessor(professor);
            return ResponseEntity.ok(updatedProfessor);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE: Удалить преподавателя
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        if (professorService.getProfessorById(id).isPresent()) {
            professorService.deleteProfessor(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * PATCH: Обновить статус преподавателя
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Professor> updateProfessorStatus(@PathVariable Long id, @RequestParam boolean active) {
        Professor updatedProfessor = professorService.updateProfessorStatus(id, active);
        if (updatedProfessor != null) {
            return ResponseEntity.ok(updatedProfessor);
        }
        return ResponseEntity.notFound().build();
    }
}

