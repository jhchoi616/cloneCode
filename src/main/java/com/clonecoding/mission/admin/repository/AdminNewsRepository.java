package com.clonecoding.mission.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.News;

public interface AdminNewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByOrderByCreatedAtDescIdDesc();
}
