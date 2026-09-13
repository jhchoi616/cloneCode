package com.clonecoding.mission.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// @SpringBootApplication 클래스가 아닌 별도 설정으로 분리해야
// @WebMvcTest 같은 슬라이스 테스트가 이 설정까지 함께 로드하지 않는다.
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
