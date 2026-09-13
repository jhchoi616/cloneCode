package com.clonecoding.mission.user.controller;

import com.clonecoding.mission.global.dto.ContactDTO;
import com.clonecoding.mission.global.entity.Contact;
import com.clonecoding.mission.user.repository.ContactRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// DB 연결 없이(Mockito로 ContactRepository를 대체) 연락처/이메일 검증·정규화 로직만 확인한다.
class ContactControllerTest {

    private ResponseEntity<?> submit(String contact) {
        ContactRepository repository = mock(ContactRepository.class);
        when(repository.save(org.mockito.ArgumentMatchers.any(Contact.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        ContactController controller = new ContactController(repository);

        ContactDTO form = new ContactDTO();
        form.setName("홍길동");
        form.setContact(contact);
        form.setType(1);
        form.setSchedule(null);
        form.setMessage("문의합니다");

        return controller.addContact(form, request);
    }

    @ParameterizedTest
    @CsvSource({
            "'010 1234 5678',        010-1234-5678",
            "'010/1234/5678',        010-1234-5678",
            "'010.1234.5678',        010-1234-5678",
            "'0212345678',           02-1234-5678",
            "'021234567',            02-123-4567",
            "'0312345678',           031-234-5678",
    })
    void 공백이나_구분자가_섞인_전화번호는_하이픈으로_정규화된다(String input, String expected) {
        ResponseEntity<?> response = submit(input);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(((Contact) response.getBody()).getContact()).isEqualTo(expected);
    }

    @Test
    void 이메일만_입력하면_이메일만_저장된다() {
        ResponseEntity<?> response = submit("test@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(((Contact) response.getBody()).getContact())
                .isEqualTo("test@example.com");
    }

    @Test
    void 전화번호와_이메일이_공백으로_함께_들어오면_표준구분자로_합쳐진다() {
        ResponseEntity<?> response = submit("010 1234 5678   test@example.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(((Contact) response.getBody()).getContact())
                .isEqualTo("010-1234-5678 / test@example.com");
    }

    @Test
    void 전화번호와_이메일이_슬래시로_섞여도_정규화된다() {
        ResponseEntity<?> response = submit("test@example.com / 010-1234-5678");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(((Contact) response.getBody()).getContact())
                .isEqualTo("010-1234-5678 / test@example.com");
    }

    @Test
    void 유효한_연락처나_이메일이_없으면_400과_안내메시지를_반환한다() {
        ResponseEntity<?> response = submit("아무 말이나 적어봄 123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(((Map<?, ?>) response.getBody()).get("message"))
                .isEqualTo("회신받으실 연락처 또는 이메일이 없습니다. 정확한 연락처를 입력해주세요.");
    }

    @Test
    void 공백만_입력하면_400을_반환한다() {
        ResponseEntity<?> response = submit("     ");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
