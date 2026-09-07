package com.clonecoding.mission.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clonecoding.mission.admin.repository.AdminInquiryRepository;
import com.clonecoding.mission.global.entity.Contact;
import com.clonecoding.mission.global.entity.News;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AdminInquiryService {
    private final AdminInquiryRepository adminInquiryRepository;

    // 문의 전체 조회 단 만들어진 아이디와 생성일순
    @Transactional (readOnly = true)
    public List<Contact> findAllByOrderByCreatedAtDescIdDesc(){
        return adminInquiryRepository.findAllByOrderByCreatedAtDescIdDesc();
    }

    @Transactional (readOnly = true)
    public Contact findById(Long id){
        return adminInquiryRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException(
                            "해당 소식을 찾을 수 없습니다. id=" + id
                    )
            );
    }


    @Transactional
    public void delete(Long id) {

    /*
     * 존재하지 않는 ID인지 확인
     */
    Contact contact = adminInquiryRepository.findById(id).orElseThrow(() ->
                    new EntityNotFoundException(
                            "해당 소식을 찾을 수 없습니다. id=" + id
                    )
            );


    adminInquiryRepository.delete(contact);

    }

}
