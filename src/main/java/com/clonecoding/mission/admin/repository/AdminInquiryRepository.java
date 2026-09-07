package com.clonecoding.mission.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.Contact;

public interface AdminInquiryRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByOrderByCreatedAtDescIdDesc();
}
