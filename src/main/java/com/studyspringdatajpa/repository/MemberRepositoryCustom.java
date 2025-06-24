package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;

import java.util.List;

// 사용자 정의 리포지토리
// 좀 많이 복잡한 동적 쿼리를 사용하거나,
// 위의 이유로 인해 QueryDSL을 사용할 때, 사용자 정의 레포지토리를 구현한다.
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
