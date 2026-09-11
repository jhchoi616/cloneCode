package com.clonecoding.mission.user.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.clonecoding.mission.global.entity.Post;
import com.clonecoding.mission.user.service.NoticeService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;


@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public String noticePage(@RequestParam(name="type", required = false) Integer type, @RequestParam(required = false, defaultValue = "", name="keyword") String keyword, @PageableDefault( size = 9, sort = "createdAt", direction = Sort.Direction.DESC ) Pageable pageable, Model model ) {

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
        model.addAttribute( "listUri", buildListQuery(type, keyword, pageable));
    return "user/notice/notice";
}

        private String buildListQuery(Integer type, String keyword, Pageable pageable) {
        UriComponentsBuilder b = UriComponentsBuilder.newInstance();
        if (type != null) b.queryParam("type", type);
        if (StringUtils.hasText(keyword)) b.queryParam("keyword", keyword);
        b.queryParam("page", pageable.getPageNumber());
        b.queryParam("size", pageable.getPageSize());
        pageable.getSort().forEach(o ->
                b.queryParam("sort", o.getProperty() + "," + o.getDirection().name()));
        return b.encode(StandardCharsets.UTF_8).toUriString();
        }
    @GetMapping("/{id}")
    public String getNotices( @PathVariable("id") Long id,@RequestParam(name="type", required = false) Integer type, @RequestParam(required = false, defaultValue = "", name="keyword") String keyword,@PageableDefault( size = 9, sort = "createdAt", direction = Sort.Direction.DESC ) Pageable pageable, Model model) {
        Post post = noticeService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("currentUri", "/notice");
        model.addAttribute("previousPost",noticeService.findPreviousPost(id, type, keyword, pageable));
        model.addAttribute("nextPost",noticeService.findNextPost(id, type, keyword, pageable));
        model.addAttribute( "listUri", buildListQuery(type, keyword, pageable));
        // previousPost
        // nextPost
        // model.addAttribute("posts",postRepository.findByTypeOrderByIdDesc(1));
        System.out.println("넘어온 값 : "+pageable.getSort());
        return "user/notice/detail";
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("id") Long id
    ) throws IOException {

        Post post = noticeService.findById(id);


        if (post.getFilePath() == null) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "첨부파일이 없습니다."
            );
        }


        Path path = Paths.get(post.getFilePath());


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
