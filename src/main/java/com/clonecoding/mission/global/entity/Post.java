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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 1 : 공지사항 , 2 : 자료실
    private Integer type;
    private String title;
    private Boolean isNotice;
    private String writer;
    private Integer viewCount;
    // 파일 역정규화 ( 첨부하는 곳이 공지 뿐임 ) 추후 필요시 정규화하여 분리
    private String fileName;
    private String filePath;
    private String fileUuid;
    private String fileSize;
    private String fileContentType;
    private String fileDownloadUri;
    private String fileUploadDir;
    private String fileExtension;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDate date;
    private LocalDateTime createdAt;
    private String createdBy;
    private String createdByIp;
    private String modifiedByIp;
    private String modifiedBy;
    private LocalDateTime modifiedAt;

    public String getFormattedFileSize() {
    if (fileSize == null) {
        return "";
    }

    if (Integer.parseInt(fileSize) < 1024) {
        return fileSize + " bytes";
    }

    double kb = Double.parseDouble(fileSize) / 1024.0;

    if (kb < 1024) {
        return String.format("%.1f KB", kb);
    }

    double mb = kb / 1024.0;

    if (mb < 1024) {
        return String.format("%.1f MB", mb);
    }

    double gb = mb / 1024.0;

    return String.format("%.1f GB", gb);
    }

}
