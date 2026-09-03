package com.clonecoding.mission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.clonecoding.mission.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;
    
    @GetMapping
    public String getNotices(Model model) {
        model.addAttribute("currentUri", "/notice");
        model.addAttribute("posts",postRepository.findByTypeOrderByIdDesc("notice"));
        return "notice/list";
    }
}
