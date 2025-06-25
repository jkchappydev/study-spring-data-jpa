package com.studyspringdatajpa.dto;

import com.studyspringdatajpa.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }

}
