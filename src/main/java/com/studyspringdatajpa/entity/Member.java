package com.studyspringdatajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    protected Member() { // JPA 표준 스펙에 기본 생성자 반드시 한개 생성 (단, private는 안됨)
    }

    public Member(String username) {
        this.username = username;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

}
