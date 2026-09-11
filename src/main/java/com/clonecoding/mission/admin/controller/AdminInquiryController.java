package com.clonecoding.mission.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clonecoding.mission.admin.service.AdminInquiryService;
import com.clonecoding.mission.global.entity.Contact;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


@Controller 
@RequestMapping ("/admin/inquiries")
@RequiredArgsConstructor 
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;
    
    @GetMapping
    public String admin_inquiry(@RequestParam(name="type", required = false) Integer type,Model model) {
        model.addAttribute("currentUri","inquiry");
        List<Contact> inquiries;

    if (type == null ) {
        inquiries = adminInquiryService.findAllByOrderByCreatedAtDescIdDesc();
    } else {
        inquiries = adminInquiryService.findByTypeOrderByCreatedAtDescIdDesc(type);
    }
        model.addAttribute("type", type);
        model.addAttribute("inquiries", inquiries);
        return "admin/inquiry";
    }

 


    @GetMapping("/api/{id}")
    @ResponseBody
    public Contact getInquiryDetail(@PathVariable("id") Long id) {
        return adminInquiryService.findById(id);
    }
    
    @PostMapping("/delete/{id}")
    public String postMethodName(@PathVariable("id") Long id) throws IOException  {
        //TODO: process POST request
        adminInquiryService.delete(id);
        return "redirect:/admin/inquiries";
    }
    
}
