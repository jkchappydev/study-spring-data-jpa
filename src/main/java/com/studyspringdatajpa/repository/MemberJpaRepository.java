package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository // 해당 어노테이션 반드시 작성
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
         em.persist(member);
         return member;
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

}
