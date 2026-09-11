package com.clonecoding.mission.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.clonecoding.mission.global.entity.Contact;

public interface AdminInquiryRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByOrderByCreatedAtDescIdDesc();
    List<Contact> findByTypeOrderByCreatedAtDescIdDesc(Integer type);
    Page<Contact> findAllByOrderByCreatedAtDescIdDesc( Pageable pageable );
    Page<Contact> findByTypeOrderByCreatedAtDescIdDesc( Integer type, Pageable pageable);
}
