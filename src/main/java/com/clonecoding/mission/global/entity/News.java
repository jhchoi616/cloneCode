package com.clonecoding.mission.global.entity;

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
    // 노출 여부
    private Boolean isView;
    // 제목
    private String title;
    // 1 : CLASS , 2 : EVENT , 3 : STORY
    private Integer category;
    // 요약 내용
    private String excerpt;
    // 본문
    @Column(columnDefinition = "TEXT")
    private String content;
    // 첨부 이미지 주소
    private String ImageUrl;
    // 시작일
    private LocalDate startDate;
    // 마감일
    private LocalDate endDate;
    // 공통 컬럼
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByIp;
    private String modifiedByIp;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
}
