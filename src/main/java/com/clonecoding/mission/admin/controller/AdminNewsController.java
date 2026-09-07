package com.clonecoding.mission.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clonecoding.mission.admin.service.AdminNewsService;
import com.clonecoding.mission.global.entity.News;

import lombok.RequiredArgsConstructor;

@Controller 
@RequestMapping("/admin")
@RequiredArgsConstructor 
public class AdminNewsController {
    //admin 소식 서비스
    private final AdminNewsService adminNewsService;

     @GetMapping("/news")
    public String admin_news(Model model) {
        model.addAttribute("currentUri","news");
                List<News> news = adminNewsService.findAll();

        model.addAttribute("newsList", news);
        return "admin/news";
    }

        @PostMapping("/news")
        @ResponseBody
        public News createNews(@RequestBody News news) {

        return adminNewsService.save(news);
        }
        
        @GetMapping("/news/{id}")
        @ResponseBody
        public News getNews(@PathVariable Long id) {

        return adminNewsService.findById(id);
        }

        @PutMapping("/news/{id}")
        @ResponseBody
        public News updateNews(
                @PathVariable Long id,
                @RequestBody News news
        ) {

        return adminNewsService.update(id, news);
        }

        @DeleteMapping("/news/{id}")
        @ResponseBody
        public void deleteNews(@PathVariable Long id) {

        adminNewsService.delete(id);
        }
}
