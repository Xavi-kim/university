package org.example.university.repository;

import org.example.university.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for University entity
 */
@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    List<University> findByActiveTrue();

    Optional<University> findByName(String name);

    List<University> findByCity(String city);

    List<University> findByCountry(String country);

    List<University> findByNameContainingIgnoreCase(String name);
}

