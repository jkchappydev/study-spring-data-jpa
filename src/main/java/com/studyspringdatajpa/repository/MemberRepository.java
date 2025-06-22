package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 쿼리 스트링
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // NamedQuery (Entity에 NamedQuery 정의되어 있어야 함)
    /*
        @Param
        - 아래처럼 @NamedQuery에 :username 처럼 JPQL을 명확하게 작성했을 경우에 해당 파라미터에 대해 사용
        @NamedQuery(
            name = "Member.findByUsername",
            query = "select m from Member m where m.username = :username"
        )
    */
    @Query(name = "Member.findByUsername") // 메서드명이랑 NamedQuery명이 일치할경우, 생략 가능 (내부적으로 NamedQuery 우선 찾고, 없으면 쿼리 메소드로 실행하게 되어있음)
    List<Member> findByUsername(@Param("username") String username);

}
