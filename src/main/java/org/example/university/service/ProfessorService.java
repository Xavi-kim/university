package org.example.university.service;

import org.example.university.model.Professor;
import org.example.university.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Professor entity
 */
@Service
@Transactional
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public List<Professor> getAllActiveProfessors() {
        return professorRepository.findByActiveTrue();
    }

    public Optional<Professor> getProfessorById(Long id) {
        return professorRepository.findById(id);
    }

    public Optional<Professor> getProfessorByEmail(String email) {
        return professorRepository.findByEmail(email);
    }

    public List<Professor> getProfessorsByDepartment(String department) {
        return professorRepository.findByDepartment(department);
    }

    public List<Professor> getProfessorsByUniversity(Long universityId) {
        return professorRepository.findByUniversityId(universityId);
    }

    public List<Professor> searchProfessors(String name) {
        return professorRepository.findByNameContainingIgnoreCase(name);
    }

    public Professor saveProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    public Professor updateProfessorStatus(Long id, boolean active) {
        Optional<Professor> professorOpt = professorRepository.findById(id);
        if (professorOpt.isPresent()) {
            Professor professor = professorOpt.get();
            professor.setActive(active);
            return professorRepository.save(professor);
        }
        return null;
    }
}

