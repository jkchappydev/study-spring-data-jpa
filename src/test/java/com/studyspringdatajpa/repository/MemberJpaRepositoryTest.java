package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional // JPA는 트랜잭션 단위로 실행되므로 해당 어노테이션 반드시 필요함.
@Rollback(false) // SpringBootTest에서는 기본적으로 트랜잭션 끝나고 rollback을 시킨다. 따라서 DB에는 아무것도 없는 것이 정상임. rollback을 시키지 않으려면 해당 어노테이션 false로 작성
@ActiveProfiles("local")
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(saveMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

}