package com.clonecoding.mission.user.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.clonecoding.mission.global.entity.Post;
import com.clonecoding.mission.user.service.NoticeService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public String noticePage(@RequestParam(required = false) Integer type, @RequestParam(required = false, defaultValue = "") String keyword, @PageableDefault( size = 9, sort = "createdAt", direction = Sort.Direction.DESC ) Pageable pageable, Model model ) {

        Page<Post> postPage = noticeService.getPosts( type,keyword,pageable); 
        Post latestNotice = noticeService.getLatestNotice(); 
        Post latestPost = noticeService.getLatestPost();
        System.out.println("지금 조회하는 정렬 조건 postPage : ");
        System.out.println(postPage.getSort());
        System.out.println(postPage.getSort().getClass().getSimpleName());


        model.addAttribute("currentUri", "/notice");
        
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("page", postPage);
        String convertSort = postPage.getSort().toString();
        convertSort = convertSort.replace(": ",",");
        System.out.println("수정된 SORT 값");
        System.out.println(convertSort);
        model.addAttribute("sort",convertSort);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        model.addAttribute( "latestNotice", latestNotice );

        model.addAttribute( "latestPostId", latestPost != null ? latestPost.getId() : null );

        model.addAttribute( "totalCount", noticeService.getTotalCount() );

        model.addAttribute( "noticeCount", noticeService.getNoticeCount() );

        model.addAttribute( "dataCount", noticeService.getDataCount() );

        return "user/notice/notice";
    }

    // @GetMapping
    // public String getNotices(Model model) {
    //     model.addAttribute("currentUri", "/notice");
    //     // model.addAttribute("posts",postRepository.findByTypeOrderByIdDesc(1));
    //     return "user/notice/notice";
    // }
}
