package com.clonecoding.mission.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findFirst5ByOrderByCreatedAtDesc();

} 