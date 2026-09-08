package com.clonecoding.mission.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.clonecoding.mission.user.repository.NewsRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final NewsRepository newsRepository;
    @GetMapping("/")
    public String home( Model model) {
        model.addAttribute("currentUri", "/");
        model.addAttribute(
            "news",
            newsRepository.findFirst3ByOrderByIdDesc()
        );

        return "index";
    }
}
