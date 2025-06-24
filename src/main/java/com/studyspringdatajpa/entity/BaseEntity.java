package com.studyspringdatajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class) // 반드시 적용 (추가적으로 스프링 부트 설정 클래스에 @EnableJpaAuditing 적용)
@Getter
@MappedSuperclass
public class BaseEntity extends BaseTimeEntity {

    // 아래 두가지는 스프링 부트 설정 클래스에서 AuditorAware 스프링 빈 등록
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

}
