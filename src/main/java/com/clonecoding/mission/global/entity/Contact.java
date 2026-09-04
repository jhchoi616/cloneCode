package com.clonecoding.mission.global.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    // 1 : 협력 , 2 : 단체 수업 요청 , 3 : 질문 , 4 : 기타
    private Integer type;
    // 연락처 : 휴대폰번호 또는 이메일 : 구분 없이 받는 형태
    private String contact;
    @Column(columnDefinition = "TEXT")
    private String message;
    // 인원이나 
    private String schedule;
    
        // 공통 컬럼
    @CreatedDate
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByIp;
    private String modifiedByIp;
    private String modifiedBy;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
