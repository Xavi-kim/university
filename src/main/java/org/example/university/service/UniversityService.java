package org.example.university.service;

import org.example.university.model.University;
import org.example.university.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for University entity
 */
@Service
@Transactional
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    public List<University> getAllActiveUniversities() {
        return universityRepository.findByActiveTrue();
    }

    public Optional<University> getUniversityById(Long id) {
        return universityRepository.findById(id);
    }

    public Optional<University> getUniversityByName(String name) {
        return universityRepository.findByName(name);
    }

    public List<University> getUniversitiesByCity(String city) {
        return universityRepository.findByCity(city);
    }

    public List<University> getUniversitiesByCountry(String country) {
        return universityRepository.findByCountry(country);
    }

    public List<University> searchUniversities(String name) {
        return universityRepository.findByNameContainingIgnoreCase(name);
    }

    public University saveUniversity(University university) {
        return universityRepository.save(university);
    }

    public void deleteUniversity(Long id) {
        universityRepository.deleteById(id);
    }

    public University updateUniversityStatus(Long id, boolean active) {
        Optional<University> universityOpt = universityRepository.findById(id);
        if (universityOpt.isPresent()) {
            University university = universityOpt.get();
            university.setActive(active);
            return universityRepository.save(university);
        }
        return null;
    }
}

