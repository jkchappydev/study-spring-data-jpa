package com.studyspringdatajpa.controller;

import com.studyspringdatajpa.dto.MemberDto;
import com.studyspringdatajpa.entity.Member;
import com.studyspringdatajpa.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 여기서 id는 pk이기 때문에, 도메인 클래스 컨버터 사용 가능
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 아래처럼 도메인 클래스 컨버터 사용 가능
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // 페이징과 정렬
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        // 1.
        // return page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        // 2.
        // return page.map(member ->
        //         MemberDto.builder()
        //                 .id(member.getId())
        //                 .username(member.getUsername())
        //                 .build()
        // );
        return page.map(MemberDto::new);
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
             memberRepository.save(new Member("user" + i, i));
        }
    }

}
