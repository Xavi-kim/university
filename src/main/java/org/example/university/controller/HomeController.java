package org.example.university.controller;

import org.example.university.service.CourseService;
import org.example.university.service.ProfessorService;
import org.example.university.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 * Home Controller for main pages
 */
@Controller
public class HomeController {

    @Autowired private CourseService courseService;
    @Autowired private ProfessorService professorService;
    @Autowired private UniversityService universityService;

    @GetMapping({"/", "/home"})
    public String index(Model model) {
        model.addAttribute("title", "Главная страница");
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Панель управления");
        return "dashboard";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "О системе");
        return "about";
    }

    @GetMapping("/api-docs")
    public String apiDocs(Model model) {
        model.addAttribute("title", "REST API Документация");
        return "api-docs";
    }

    // /browse редиректим на /catalog
    @GetMapping("/browse")
    public String browse() {
        return "redirect:/catalog";
    }

    // Страница каталога: курсы, преподаватели, университеты
    @GetMapping("/catalog")
    public String catalog(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("professors", professorService.getAllProfessors());
        model.addAttribute("universities", universityService.getAllUniversities());
        return "catalog";
    }
}
