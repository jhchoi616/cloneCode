package com.clonecoding.mission.admin.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.clonecoding.mission.admin.service.PostService;
import com.clonecoding.mission.global.entity.Contact;
import com.clonecoding.mission.global.entity.Post;
import com.clonecoding.mission.user.repository.ContactRepository;
import com.clonecoding.mission.user.repository.NewsRepository;
import com.clonecoding.mission.user.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.net.URLEncoder;
import java.nio.file.Path;




@Controller 
@RequestMapping("/admin")
@RequiredArgsConstructor 
public class AdminController {
    // 유저의 레포지토리 기본기능용!
    private final NoticeRepository noticeRepository;
    private final ContactRepository contactRepository;
    private final NewsRepository newsRepository;
    // admin 공지 서비스
    private final PostService postService;


    @GetMapping
    public String getMethodName(Model model) {
        model.addAttribute("currentUri","admin");
        // noticeCount, newsCount, inquiryCount
        model.addAttribute("inquiryCount",contactRepository.count());
        model.addAttribute("newsCount",newsRepository.count());
        model.addAttribute("noticeCount",noticeRepository.count());
        // recentInquiries 문의
        model.addAttribute("recentInquiries",contactRepository.findFirst5ByOrderByCreatedAtDesc());
        // recentNotices 공지
        model.addAttribute("recentNotices",noticeRepository.findFirst5ByOrderByCreatedAtDesc());
        // recentNews 소식
        model.addAttribute("recentNews",newsRepository.findFirst5ByOrderByCreatedAtDesc());
        return "admin/dashboard";
    }
    

    @GetMapping("/login")
    public String loginForm() {
        return "admin/login";
    }

    @GetMapping("/notices")
    public String admin_notice(@RequestParam(name="type", required = false) Integer type, @RequestParam(required = false, defaultValue = "", name="keyword") String keyword, @PageableDefault( size = 10, sort = "createdAt", direction = Sort.Direction.DESC ) Pageable pageable, Model model ) {
        
        model.addAttribute("currentUri","notice");
        // notices에 데이터 넣어가기
        Page<Post> notices = postService.findAll(type, keyword, pageable);

        model.addAttribute("notices", notices);


        // 검색/필터 조건 유지용
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/notice";
    }

    // 공지 저장 [ 새글, 수정 ]
    @PostMapping("/notices/save")
    public String saveNotice( @ModelAttribute Post post, @RequestParam( name="uploadFile", value = "uploadFile", required = false ) MultipartFile uploadFile ) throws IOException {
        /*
        * id가 없으면 새 글
        */
        if (post.getId() == null) {
            postService.save( post, uploadFile );
        }
        /*
        * id가 있으면 수정
        */
        else {
            postService.update(
                    post.getId(),
                    post,
                    uploadFile
            );

        }
        return "redirect:/admin/notices";
    }

    // 공지 삭제
    @PostMapping("/notices/delete/{id}")
    public String deleteNotice( @PathVariable("id") Long id ) throws IOException {
    postService.delete(id);
    return "redirect:/admin/notices";
    }

    
   

    // 파일 다운로드
    @GetMapping("/notices/file/{id}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("id") Long id
    ) throws IOException {

        Post post =
                postService.findById(id);


        if (post.getFilePath() == null) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "첨부파일이 없습니다."
            );
        }


        Path path =
                Paths.get(post.getFilePath());


        Resource resource =
                new UrlResource(
                        path.toUri()
                );


        if (!resource.exists()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "파일을 찾을 수 없습니다."
            );
        }


        String fileName =
                URLEncoder.encode(
                        post.getFileName(),
                        StandardCharsets.UTF_8
                ).replaceAll("\\+", "%20");


        return ResponseEntity.ok()

                .contentType(
                        MediaType.parseMediaType(
                                post.getFileContentType()
                                        != null
                                        ? post.getFileContentType()
                                        : MediaType.APPLICATION_OCTET_STREAM_VALUE
                        )
                )

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + fileName
                )

                .body(resource);
    }
}
