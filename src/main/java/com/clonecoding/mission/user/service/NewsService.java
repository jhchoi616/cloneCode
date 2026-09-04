package com.clonecoding.mission.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.clonecoding.mission.global.entity.News;
import com.clonecoding.mission.user.repository.NewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;

    public Page<News> findNews(Integer category,Pageable pageable) {

        if (category == null ) {
            return newsRepository.findAll(pageable);
        }

        return newsRepository.findByCategory( category, pageable );
    }

    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow();
    }
    public long countAll() {
    return newsRepository.count();
    }

    public long countByCategory(Integer category) {
        return newsRepository.countByCategory(category);
    }
public News findPrevious(Long id) {
    return newsRepository.findFirstByIdLessThanOrderByIdDesc(id)
            .orElse(null);
}

public News findNext(Long id) {
    return newsRepository.findFirstByIdGreaterThanOrderByIdAsc(id)
            .orElse(null);
}
}
