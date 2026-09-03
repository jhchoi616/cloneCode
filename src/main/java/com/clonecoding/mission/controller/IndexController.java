package com.clonecoding.mission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
    
    @GetMapping("/")
    public String home( Model model) {
        model.addAttribute("currentUri", "/");
        // model.addAttribute(
        //     "news",
        //     newsRepository.findByTypeOrderByIdDesc("notice")
        // );

        return "index";
    }
}
