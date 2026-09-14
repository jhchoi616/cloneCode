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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactRepository contactRepository;

    // 이메일 형식 검증용. 하이픈 포맷은 자릿수로만 판단하므로 별도 지역번호 목록은 두지 않는다.
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[^\\s@]+@[^\\s@]+\\.[^\\s@]+");

    @GetMapping
    public String getMethodName(Model model) {
        model.addAttribute("currentUri", "/contact");
        return "user/contact/contact";
    }

    @PostMapping
    public ResponseEntity<?> addContact(@ModelAttribute ContactDTO form, HttpServletRequest request) {

        String normalizedContact = normalizeContact(form.getContact());
        if (normalizedContact == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "회신받으실 연락처 또는 이메일이 없습니다. 정확한 연락처를 입력해주세요."));
        }
        String normalizedMsg = form.getMessage().replace("\r\n","").trim();
        if( normalizedMsg.length()>300){
            return ResponseEntity.badRequest().body(Map.of("message","요청 사항은 300자를 초과하여 작성 할 수 없습니다."));
        }
        if( form.getSchedule().length() >30){
            return ResponseEntity.badRequest().body(Map.of("message","인원 및 일정은 30자를 초과하여 작성 할 수 없습니다."));
        }

        Contact contact = new Contact();

        contact.setTitle(convertType(form.getType()));
        contact.setType(form.getType());
        contact.setContact(normalizedContact);
        contact.setSchedule(form.getSchedule());
        contact.setMessage(form.getMessage());

        // contact.setCreatedAt(LocalDateTime.now());
        contact.setCreatedBy(form.getName());
        contact.setCreatedByIp(request.getRemoteAddr());

        Contact savedContact = contactRepository.save(contact);


        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    // 프론트에서 우회해 보낸 값을 대비한 서버측 재검증.
    // 이메일을 먼저 뽑아내고 남은 문자열의 숫자만 모아 전화번호 하나로 포맷해 " / "로 합친다.
    // (공백을 구분자로 잡으면 "010 1234 5678"처럼 그룹을 띄어 쓴 전화번호까지 쪼개지므로
    //  공백/하이픈/점 등은 전화번호 내부 표기로 취급한다.) 유효한 값이 하나도 없으면 null.
    private String normalizeContact(String raw) {
        if (raw == null) {
            return null;
        }

        List<String> emails = new ArrayList<>();
        Matcher matcher = EMAIL_PATTERN.matcher(raw);
        while (matcher.find()) {
            emails.add(matcher.group());
        }

        String digits = matcher.replaceAll("").replaceAll("\\D", "");
        String phone = digits.isEmpty() ? null : formatPhone(digits);

        List<String> formatted = new ArrayList<>();
        if (phone != null) {
            formatted.add(phone);
        }
        formatted.addAll(emails);

        return formatted.isEmpty() ? null : String.join(" / ", formatted);
    }

    // 02는 9~10자리(2-3-4 / 2-4-4), 그 외 3자리 국번(휴대폰·지역번호 공통)은 10~11자리(3-3-4 / 3-4-4)로 하이픈 처리.
    private String formatPhone(String digits) {
        if (digits.startsWith("02")) {
            if (digits.length() == 9) {
                return digits.replaceAll("(\\d{2})(\\d{3})(\\d{4})", "$1-$2-$3");
            }
            if (digits.length() == 10) {
                return digits.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "$1-$2-$3");
            }
            return null;
        }
        if (digits.length() == 10) {
            return digits.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
        }
        if (digits.length() == 11) {
            return digits.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        }
        return null;
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
