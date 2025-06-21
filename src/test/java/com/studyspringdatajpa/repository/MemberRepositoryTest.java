package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
@ActiveProfiles("local")
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        // 여기서는 Optional (saveMember가 있을수도 있고, 없을수도 있기 때문에)
        Optional<Member> byId = memberRepository.findById(savedMember.getId());

        // then
        assertThat(byId).isPresent(); // Optional이 비어있지 않은지 검증
        Member foundMember = byId.get();

        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
        assertThat(foundMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(foundMember).isEqualTo(savedMember); // equals(), hashCode() 오버라이드 필요
    }

}