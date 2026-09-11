package com.clonecoding.mission.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.News;

public interface AdminNewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByOrderByCreatedAtDescIdDesc();
    Page<News> findByCategoryAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
        Integer category,
        String keyword,
        Pageable pageable
);

Page<News> findByCategoryOrderByCreatedAtDesc(
        Integer category,
        Pageable pageable
);

Page<News> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(
        String keyword,
        Pageable pageable
);

Page<News> findAllByOrderByCreatedAtDesc(
        Pageable pageable
);
}
