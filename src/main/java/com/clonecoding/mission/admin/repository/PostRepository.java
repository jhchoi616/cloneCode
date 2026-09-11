package com.clonecoding.mission.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDescTypeDesc();
    Page<Post> findByTypeAndTitleContainingIgnoreCaseOrderByCreatedAtDescTypeDesc(Integer type, String keyword, Pageable pageable);
    Page<Post> findByTypeOrderByCreatedAtDescTypeDesc(Integer type, Pageable pageable);
    Page<Post> findByTitleContainingIgnoreCaseOrderByCreatedAtDescTypeDesc(String keyword, Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtDescTypeDesc(Pageable pageable);
}
