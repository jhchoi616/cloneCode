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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String title;
    private Boolean isNotice;
    private String writer;
    private Integer viewCount;
    // 파일 역정규화 ( 첨부하는 곳이 공지 뿐임 ) 추후 필요시 정규화하여 분리
    private String fileName;
    private String filePath;
    private String fileUuid;
    private String fileSize;
    private String fileExtension;
    private String fileContentType;
    private String fileDownloadUri;
    private String fileUploadDir;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByIp;
    private String modifiedByIp;
    private String modifiedBy;
    private LocalDateTime modifiedAt;

}
