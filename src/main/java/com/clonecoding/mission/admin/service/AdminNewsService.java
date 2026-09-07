package com.clonecoding.mission.admin.service;

import com.clonecoding.mission.admin.repository.AdminNewsRepository;
import com.clonecoding.mission.global.entity.News;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminNewsService {

private final AdminNewsRepository newsRepository;


/*
 * ============================================================
 * 뉴스 전체 조회
 * ============================================================
 */
@Transactional(readOnly = true)
public List<News> findAll() {

    return newsRepository.findAllByOrderByCreatedAtDescIdDesc();

}


/*
 * ============================================================
 * 뉴스 단건 조회
 * ============================================================
 *
 * GET /admin/news/{id}
 *
 * 수정 모달에 기존 데이터를 넣기 위해 사용
 */
@Transactional(readOnly = true)
public News findById(Long id) {

    return newsRepository.findById(id)
            .orElseThrow(() ->
                    new EntityNotFoundException(
                            "해당 소식을 찾을 수 없습니다. id=" + id
                    )
            );

}


/*
 * ============================================================
 * 뉴스 등록
 * ============================================================
 *
 * POST /admin/news
 */
@Transactional
public News save(News news) {
    /*
     * 신규 작성 기본값
     */
    if (news.getIsView() == null) {
        news.setIsView(false);
    }
    /*
     * 작성일
     */
    if (news.getCreatedAt() == null) {
        news.setCreatedAt(
                java.time.LocalDateTime.now()
        );
    }


    return newsRepository.save(news);

}


/*
 * ============================================================
 * 뉴스 수정
 * ============================================================
 *
 * PUT /admin/news/{id}
 */
@Transactional
public News update( Long id, News request) {
    /*
     * 기존 데이터 조회
     */
    News news = newsRepository.findById(id)
            .orElseThrow(() ->
                    new EntityNotFoundException(
                            "해당 소식을 찾을 수 없습니다. id=" + id
                    )
            );


    /*
     * 수정 가능한 값
     */

    news.setCategory(
            request.getCategory()
    );

    news.setTitle(
            request.getTitle()
    );

    news.setExcerpt(
            request.getExcerpt()
    );

    news.setContent(
            request.getContent()
    );

    news.setImageUrl(
            request.getImageUrl()
    );

    news.setStartDate(
            request.getStartDate()
    );

    news.setEndDate(
            request.getEndDate()
    );

    news.setIsView(
            request.getIsView()
    );

    news.setModifiedBy(
            request.getModifiedBy()
    );

    news.setModifiedByIp(
            request.getModifiedByIp()
    );

    news.setModifiedAt(
            java.time.LocalDateTime.now()
    );


    /*
     * save()를 명시적으로 호출해도 되고
     * @Transactional의 dirty checking으로
     * 반영되기 때문에 그대로 반환해도 됨.
     */
    return news;

}


/*
 * ============================================================
 * 뉴스 삭제
 * ============================================================
 *
 * DELETE /admin/news/{id}
 * ============================================================
 */
@Transactional
public void delete(Long id) {

    /*
     * 존재하지 않는 ID인지 확인
     */
    News news = newsRepository.findById(id)
            .orElseThrow(() ->
                    new EntityNotFoundException(
                            "해당 소식을 찾을 수 없습니다. id=" + id
                    )
            );


    newsRepository.delete(news);

}

}
