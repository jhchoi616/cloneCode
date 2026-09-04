package com.clonecoding.mission.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.News;


public interface NewsRepository extends JpaRepository<News, Long> {
    // List<News> findByView();
     Page<News> findByCategory(Integer category, Pageable pageable);
     long countByCategory(Integer category);
     Optional<News> findFirstByIdLessThanOrderByIdDesc(Long id);

    Optional<News> findFirstByIdGreaterThanOrderByIdAsc(Long id);
}
