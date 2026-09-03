package com.clonecoding.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.entity.News;
import java.util.List;


public interface NewsRepository extends JpaRepository<News, Long> {
    // List<News> findByView();
}
