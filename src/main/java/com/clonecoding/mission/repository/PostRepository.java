package com.clonecoding.mission.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTypeOrderByIdDesc(String type);
    List<Post> findAllByOrderByIdDesc();
}