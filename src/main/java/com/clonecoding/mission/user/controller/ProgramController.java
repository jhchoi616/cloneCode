package com.clonecoding.mission.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class ProgramController {
    // TODO NEWS 데이터 가져오기
    // private final NewsRepository newsRepository;

    @GetMapping("/programs")
    public String programPage(Model model) {
        model.addAttribute("currentUri", "/program");
        return "user/programs/program";
    }

    @GetMapping("/class")
    public String classPage(Model model) {
        model.addAttribute("currentUri", "/program");
        return "user/programs/class";
    }
    
    @GetMapping("/event")
    public String eventPage(Model model) {
        model.addAttribute("currentUri", "/program");
        // newsRepository
        // model.addAttribute("newsList", newsRepository.find)

        return "user/programs/event";
    }
    
    
}
