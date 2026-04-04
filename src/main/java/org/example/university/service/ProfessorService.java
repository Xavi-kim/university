package org.example.university.service;

import org.example.university.model.Professor;
import org.example.university.model.User;
import org.example.university.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Пароль по умолчанию для новых профессоров
    private static final String DEFAULT_PASSWORD = "professor123";

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
        // Сохраняем профессора
        Professor savedProfessor = professorRepository.save(professor);

        // Автоматически создаем пользователя для профессора, если его еще нет
        createUserForProfessor(savedProfessor);

        return savedProfessor;
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

    // Найти профессора по email (без Optional для совместимости)
    public Professor findByEmail(String email) {
        return professorRepository.findByEmail(email).orElse(null);
    }

    /**
     * Автоматически создает пользователя для профессора, если его еще нет
     * @param professor - профессор, для которого нужно создать пользователя
     */
    private void createUserForProfessor(Professor professor) {
        if (professor == null || professor.getEmail() == null) {
            return;
        }

        // Проверяем, существует ли уже пользователь с таким email
        Optional<User> existingUser = userService.getUserByEmail(professor.getEmail());

        if (existingUser.isEmpty()) {
            // Создаем нового пользователя для профессора
            User newUser = new User();
            newUser.setName(professor.getName());
            newUser.setEmail(professor.getEmail());
            newUser.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
            newUser.setRole("PROFESSOR");
            newUser.setEnabled(true);
            newUser.setRegistrationDate(LocalDateTime.now());
            newUser.setEmailVerified(true);
            newUser.setBio(professor.getBio() != null ? professor.getBio() : "Преподаватель кафедры " + professor.getDepartment());

            userService.saveUser(newUser);

            System.out.println("✅ [PROFESSOR SERVICE] Автоматически создан пользователь для профессора: " + professor.getEmail());
            System.out.println("   Имя: " + professor.getName());
            System.out.println("   Пароль по умолчанию: " + DEFAULT_PASSWORD);
        } else {
            // Если пользователь существует, проверяем его роль
            User user = existingUser.get();
            if (!"PROFESSOR".equals(user.getRole())) {
                user.setRole("PROFESSOR");
                userService.saveUser(user);
                System.out.println("✅ [PROFESSOR SERVICE] Обновлена роль пользователя на PROFESSOR: " + professor.getEmail());
            }
        }
    }

    /**
     * Создает пользователей для всех существующих профессоров, у которых их еще нет
     * @return количество созданных пользователей
     */
    public int registerAllExistingProfessors() {
        List<Professor> allProfessors = professorRepository.findAll();
        int createdCount = 0;

        System.out.println("📊 [PROFESSOR SERVICE] Регистрация существующих профессоров...");
        System.out.println("   Всего профессоров в базе: " + allProfessors.size());

        for (Professor professor : allProfessors) {
            Optional<User> existingUser = userService.getUserByEmail(professor.getEmail());

            if (existingUser.isEmpty()) {
                createUserForProfessor(professor);
                createdCount++;
            } else {
                // Проверяем роль
                User user = existingUser.get();
                if (!"PROFESSOR".equals(user.getRole())) {
                    user.setRole("PROFESSOR");
                    userService.saveUser(user);
                    System.out.println("✅ [PROFESSOR SERVICE] Обновлена роль: " + professor.getEmail());
                }
            }
        }

        System.out.println("✅ [PROFESSOR SERVICE] Регистрация завершена!");
        System.out.println("   Создано новых пользователей: " + createdCount);

        return createdCount;
    }
}
