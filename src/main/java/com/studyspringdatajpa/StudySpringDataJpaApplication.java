package com.studyspringdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing /* (modifyOnCreate = false) 하면 최초 등록시 update컬럼에는 null이 들어감 */
@SpringBootApplication
public class StudySpringDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringDataJpaApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        // 스프링 시큐리티에서는 SpringSecurityContextHolder 에서 세션 정보를 꺼내와서 로그인한 사용자의 ID를 가져와야 한다.
        // 여기서는 테스트용으로 대충 작성
        return () -> Optional.of(UUID.randomUUID().toString());
    }

}
