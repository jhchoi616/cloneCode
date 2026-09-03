package com.clonecoding.mission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AboutController {
    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("currentUri", "/about");
        return "about/about";
    }

    @GetMapping("/story")
    public String storyPage(Model model) {
        model.addAttribute("currentUri", "/about");
        return "about/story";
    }
       
}
