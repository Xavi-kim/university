package org.example.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 * Home Controller for main pages
 */
@Controller
public class HomeController {

    @GetMapping("/")
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

    @GetMapping("/browse")
    public String apiBrowser(Model model) {
        model.addAttribute("title", "API Browser");
        return "api-browser";
    }
}

