package com.clonecoding.mission.admin.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        public String admin_news(
                @RequestParam(name = "category", required = false) Integer category,
                @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,

                @PageableDefault(
                        size = 10,
                        sort = "createdAt",
                        direction = Sort.Direction.DESC
                ) Pageable pageable,

                Model model
        ) {
        model.addAttribute("currentUri", "news");

        Page<News> news = adminNewsService.findAll(category, keyword, pageable);

        model.addAttribute("newsList", news);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);

        return "admin/news";
        }

        @PostMapping("/news")
        @ResponseBody
        public News createNews(@RequestBody News news) {

        return adminNewsService.save(news);
        }
        
        @GetMapping("/news/{id}")
        @ResponseBody
        public News getNews(@PathVariable("id") Long id) {

        return adminNewsService.findById(id);
        }

        @PutMapping("/news/{id}")
        @ResponseBody
        public News updateNews(
                @PathVariable("id") Long id,
                @RequestBody News news
        ) {

        return adminNewsService.update(id, news);
        }

        @DeleteMapping("/news/{id}")
        @ResponseBody
        public void deleteNews(@PathVariable("id") Long id) {

        adminNewsService.delete(id);
        }
}
