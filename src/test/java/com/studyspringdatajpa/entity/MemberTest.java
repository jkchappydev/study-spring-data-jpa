package com.studyspringdatajpa.entity;

import com.studyspringdatajpa.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
@ActiveProfiles("local")
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush(); // DB에 강제로 쿼리
        em.clear(); // 영속성 컨텍스트 비움

        // 확인
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : result) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

    @Test
    public void JpaEventBaseEntity() throws Exception {
        // given
        Member member = new Member("member1", 10);
        memberRepository.save(member); // @PrePersist 발생

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); // @PreUpdate 발생
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        // System.out.println("findMember.createDate = " + findMember.getCreateDate());
        // System.out.println("findMember.updateDate = " + findMember.getUpdateDate());
    }

    @Test
    public void EventBaseEntity() throws Exception {
        // given
        Member member = new Member("member1", 10);
        memberRepository.save(member); // @PrePersist 발생

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush(); // @PreUpdate 발생
        em.clear();

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        System.out.println("findMember.createDate = " + findMember.getCreateDate());
        System.out.println("findMember.updateDate = " + findMember.getLastModifiedDate());
        System.out.println("findMember.createBy = " + findMember.getCreatedBy());
        System.out.println("findMember.updateBy = " + findMember.getLastModifiedBy());
    }

}