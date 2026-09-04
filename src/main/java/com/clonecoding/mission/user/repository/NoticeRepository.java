package com.clonecoding.mission.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.Post;

public interface NoticeRepository extends JpaRepository<Post, Long> {
    // 최신 공지를 제외한 전체 게시글
    Page<Post> findByIdNot(
            Long id,
            Pageable pageable
    );

    // 최신 공지를 제외한 특정 타입
    Page<Post> findByIdNotAndType(
            Long id,
            Integer type,
            Pageable pageable
    );

    // 최신 공지를 제외한 전체 게시글 + 제목 검색
    Page<Post> findByIdNotAndTitleContaining(
            Long id,
            String keyword,
            Pageable pageable
    );

    // 최신 공지를 제외한 특정 타입 + 제목 검색
    Page<Post> findByIdNotAndTypeAndTitleContaining(
            Long id,
            Integer type,
            String keyword,
            Pageable pageable
    );

    // 최신 공지 1개
    Post findFirstByTypeOrderByCreatedAtDesc(Integer type);

    // 전체 게시글 중 가장 최신 1개
    Post findFirstByOrderByCreatedAtDesc();

    // 탭별 게시글 수
    long countByType(Integer type);

    Page<Post> findByType(
        Integer type,
        Pageable pageable
);

Page<Post> findByTitleContaining(
        String keyword,
        Pageable pageable
);

Page<Post> findByTypeAndTitleContaining(
        Integer type,
        String keyword,
        Pageable pageable
);
}