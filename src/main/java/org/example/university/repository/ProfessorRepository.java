package org.example.university.repository;

import org.example.university.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Professor entity
 */
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findByActiveTrue();

    List<Professor> findByDepartment(String department);

    List<Professor> findByUniversityId(Long universityId);

    Optional<Professor> findByEmail(String email);

    List<Professor> findByNameContainingIgnoreCase(String name);
}

