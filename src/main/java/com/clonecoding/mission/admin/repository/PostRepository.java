package com.clonecoding.mission.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDescTypeDesc();
}
