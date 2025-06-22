package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 쿼리 메서
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

    // 실무에서 많이 사용
    // 쿼리 메서드의 단점인 조건이 많아지면 메서드명의 길이가 길어지는 단점 극복
    // NamedQuery의 단점인 Entity측에 쿼리를 작성해야하는 단점 극복 및 NamedQuery의 장점(애플리케이션 로딩 시점에서 체크)를 그대로 사용할 수 있음
    // 메서드명 관례에 영향받지 않음
    // 동적 쿼리는 QueryDSL 사용해야 함
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

}
