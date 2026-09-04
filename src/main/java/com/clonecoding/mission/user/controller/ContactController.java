package com.clonecoding.mission.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.clonecoding.mission.global.dto.ContactDTO;
import com.clonecoding.mission.global.entity.Contact;
import com.clonecoding.mission.user.repository.ContactRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactRepository contactRepository;

    @GetMapping
    public String getMethodName(Model model) {
        model.addAttribute("currentUri", "/contact");
        return "user/contact/contact";
    }

    @PostMapping
    public ResponseEntity<Contact> addContact(@ModelAttribute ContactDTO form, HttpServletRequest request) {

        Contact contact = new Contact();

        contact.setTitle(convertType(form.getType()));
        contact.setType(form.getType());
        contact.setContact(form.getContact());
        contact.setSchedule(form.getSchedule());
        contact.setMessage(form.getMessage());

        // contact.setCreatedAt(LocalDateTime.now());
        contact.setCreatedBy(form.getName());
        contact.setCreatedByIp(request.getRemoteAddr());
        
        Contact savedContact = contactRepository.save(contact);
        

        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    // 타입에 따라 타이틀 만들어주기
    private String convertType(Integer type) {
            // 1 : 협력 , 2 : 단체 수업 요청 , 3 : 질문 , 4 : 기타
        switch (type) {
            case 1:
                return "협력";
            case 2:
                return "단체 수업 요청";
            case 3:
                return "질문";
            default:
                return "기타";
        }
    }
    
    
}
