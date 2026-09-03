package com.clonecoding.mission.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer type;
    private String tel;
    @Column(columnDefinition = "TEXT")
    private String contact;
    private String description;
    
        // 공통 컬럼
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByIp;
    private String modifiedByIp;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
}
