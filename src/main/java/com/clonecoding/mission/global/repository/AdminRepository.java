package com.clonecoding.mission.global.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUserId(String userId);
}