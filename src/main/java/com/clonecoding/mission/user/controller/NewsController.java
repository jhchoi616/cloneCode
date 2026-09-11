package com.clonecoding.mission.user.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clonecoding.mission.global.entity.News;
import com.clonecoding.mission.user.service.NewsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    @GetMapping
    public String getMethodName(@RequestParam(name="category", required = false) Integer category,@PageableDefault( size = 9, sort = "createdAt", direction = Sort.Direction.DESC ) Pageable pageable,Model model) {
        model.addAttribute("currentUri", "/news");
        Page<News> page = newsService.findNews(category, pageable);
        // System.out.println("지금 선택한 카테고리는?");
        // System.out.println(category);
        model.addAttribute("newsList", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("totalCount", newsService.countAll());
        model.addAttribute("classCount", newsService.countByCategory(1));
        model.addAttribute("eventCount", newsService.countByCategory(2));
        model.addAttribute("storyCount", newsService.countByCategory(3));

        return "user/news/news";
    }
    
    
    @GetMapping("/{id}")
    public String detail(
            @PathVariable("id") Long id,
            @RequestParam(name="category", required = false) Integer category,
            Model model) {
        if(category!=null){
            News news = newsService.findById(id);
            News previousNews = newsService.findPreviousWithCategory(id, category);
            News nextNews = newsService.findNextWithCategory(id,category);
            System.out.println("지금 선택한 카테고리는?");
            System.out.println(category);
            model.addAttribute("selectedCategory", category);
            model.addAttribute("news", news);
            model.addAttribute("previousNews", previousNews);
            model.addAttribute("nextNews", nextNews);
        }else{
            News news = newsService.findById(id);
            News previousNews = newsService.findPrevious(id);
            News nextNews = newsService.findNext(id);
            model.addAttribute("news", news);
            model.addAttribute("previousNews", previousNews);
            model.addAttribute("nextNews", nextNews);
        }
        return "user/news/detail";
    }
}
