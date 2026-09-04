package com.clonecoding.mission.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.clonecoding.mission.global.entity.Post;
import com.clonecoding.mission.user.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    
    
    public Page<Post> getPosts(
            Integer type,
            String keyword,
            Pageable pageable
    ) {
 
        boolean hasKeyword =
                keyword != null && !keyword.trim().isEmpty();

        String searchKeyword =
                hasKeyword ? keyword.trim() : null;

        // 최신 공지 조회
        Post latestNotice = getLatestNotice();


        /*
         * 자료실(type=2)은
         * 최신 공지를 제외할 필요가 없다.
         */
        if (type != null && type == 2) {

            if (hasKeyword) {
                return noticeRepository.findByTypeAndTitleContaining(
                        type,
                        searchKeyword,
                        pageable
                );
            }

            return noticeRepository.findByType(
                    type,
                    pageable
            );
        }


        /*
         * 여기부터는 전체(type=null) 또는
         * 공지사항(type=1)
         *
         * 최신 공지가 존재한다면
         * 해당 게시글은 Page 조회에서 제외한다.
         */
        if (latestNotice != null) {

            Long latestNoticeId = latestNotice.getId();

            // 공지사항 + 검색
            if (type != null && hasKeyword) {

                return noticeRepository
                        .findByIdNotAndTypeAndTitleContaining(
                                latestNoticeId,
                                type,
                                searchKeyword,
                                pageable
                        );
            }

            // 공지사항
            if (type != null) {

                return noticeRepository.findByIdNotAndType(
                        latestNoticeId,
                        type,
                        pageable
                );
            }

            // 전체 + 검색
            if (hasKeyword) {

                return noticeRepository
                        .findByIdNotAndTitleContaining(
                                latestNoticeId,
                                searchKeyword,
                                pageable
                        );
            }

            // 전체
            return noticeRepository.findByIdNot(
                    latestNoticeId,
                    pageable
            );
        }


        /*
         * 최신 공지가 아예 없는 경우
         *
         * id 제외 조건을 사용하지 않는다.
         */
        if (type != null) {

            if (hasKeyword) {
                return noticeRepository.findByTypeAndTitleContaining(
                        type,
                        searchKeyword,
                        pageable
                );
            }

            return noticeRepository.findByType(
                    type,
                    pageable
            );
        }

        if (hasKeyword) {
            return noticeRepository.findByTitleContaining(
                    searchKeyword,
                    pageable
            );
        }

        return noticeRepository.findAll(pageable);
    }


    // 최신 공지사항
    public Post getLatestNotice() {
        return noticeRepository.findFirstByTypeOrderByCreatedAtDesc(1);
    }


    // 전체 게시글 중 가장 최신 게시글
    // NEW 뱃지 판별용
    public Post getLatestPost() {
        return noticeRepository.findFirstByOrderByCreatedAtDesc();
    }


    // 전체 게시글 수
    public long getTotalCount() {
        return noticeRepository.count();
    }


    // 공지사항 수
    public long getNoticeCount() {
        return noticeRepository.countByType(1);
    }


    // 자료실 수
    public long getDataCount() {
        return noticeRepository.countByType(2);
    }
}
