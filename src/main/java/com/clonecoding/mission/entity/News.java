package com.clonecoding.mission.entity;

import java.time.LocalDate;
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
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean view;
    private String title;
    private String category;
    private String description;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String ImageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    // 공통 컬럼
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByIp;
    private String modifiedByIp;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
}
