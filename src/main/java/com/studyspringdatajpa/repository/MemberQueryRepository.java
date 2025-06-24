package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 클래스로 생성하고 해당 어노테이션을 사용하는 것 만으로도 스프링의 컴포넌트 스캔 대상이 된다.
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

}
