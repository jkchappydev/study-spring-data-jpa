package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 쿼리 스트링
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

}
