package ru.fa.internetshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about")
    public String about(Model model) {
        // Значения ФИО, группы и контактов нужно поменять под себя
        model.addAttribute("authorName", "ФИО Студента");
        model.addAttribute("group", "ДПИ23-3");
        model.addAttribute("university", "Финансовый университет при Правительстве РФ");
        model.addAttribute("email", "student@example.com");
        model.addAttribute("technologies",
                "Java, Spring Boot, Spring Data JPA, Spring Security, Thymeleaf, PostgreSQL");
        model.addAttribute("projectStart", "сентябрь 2025");
        model.addAttribute("projectEnd", "декабрь 2025");
        return "about";
    }
}
