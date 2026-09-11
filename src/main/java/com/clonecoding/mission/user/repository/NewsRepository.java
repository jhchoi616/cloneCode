package com.clonecoding.mission.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.News;


public interface NewsRepository extends JpaRepository<News, Long> {
    // List<News> findByView();
    Page<News> findByCategoryOrderByIdDesc(Integer category, Pageable pageable);
    Page<News> findAllByOrderByIdDesc(Pageable pageable);
    long countByCategory(Integer category);
    Optional<News> findFirstByIdLessThanOrderByIdDesc(Long id);
    Optional<News> findFirstByIdGreaterThanOrderByIdAsc(Long id);
    Optional<News> findFirstByIdLessThanAndCategoryOrderByIdDesc(Long id, Integer category);
    Optional<News> findFirstByIdGreaterThanAndCategoryOrderByIdAsc(Long id, Integer category);
    List<News> findFirst3ByOrderByIdDesc();
    List<News> findFirst4ByIsViewOrderByIdDesc(Boolean isView);
    List<News> findFirst5ByOrderByCreatedAtDesc();
}
