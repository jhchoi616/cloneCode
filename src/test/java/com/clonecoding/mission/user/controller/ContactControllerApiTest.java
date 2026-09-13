package com.clonecoding.mission.user.controller;

import com.clonecoding.mission.global.entity.Contact;
import com.clonecoding.mission.user.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 실제 스프링 MVC 라우팅/폼 바인딩/JSON 응답까지 거치는 HTTP 레벨 테스트.
// 시큐리티 필터(CSRF 등)는 이 컨트롤러 로직 검증과 무관하므로 addFilters=false로 끔.
@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactRepository contactRepository;

    @Test
    void 이메일만_폼으로_전송하면_201과_저장된_연락처를_반환한다() throws Exception {
        when(contactRepository.save(any(Contact.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/contact")
                        .param("name", "홍길동")
                        .param("contact", "test@example.com")
                        .param("type", "1")
                        .param("message", "문의합니다"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contact").value("test@example.com"));
    }

    @Test
    void 전화번호와_이메일이_공백으로_섞여도_정규화되어_저장된다() throws Exception {
        when(contactRepository.save(any(Contact.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/contact")
                        .param("name", "홍길동")
                        .param("contact", "010 1234 5678   test@example.com")
                        .param("type", "1")
                        .param("message", "문의합니다"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contact").value("010-1234-5678 / test@example.com"));
    }

    @Test
    void 유효하지_않은_연락처면_400과_안내메시지를_반환한다() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("name", "홍길동")
                        .param("contact", "아무 말이나 적어봄 123")
                        .param("type", "1")
                        .param("message", "문의합니다"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("회신받으실 연락처 또는 이메일이 없습니다. 정확한 연락처를 입력해주세요."));
    }
}
