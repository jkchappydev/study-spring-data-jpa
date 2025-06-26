package com.studyspringdatajpa.repository;

import com.studyspringdatajpa.entity.Member;
import com.studyspringdatajpa.entity.Team;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class MemberSpec {

    // 팀 이름을 검색조건으로
    public static Specification<Member> teamName(final String teamName) {
        return (root, query, criteriaBuilder) -> {
            if(StringUtils.isEmpty(teamName)) {
                return null;
            }

            Join<Member, Team> team = root.join("team", JoinType.INNER);
            return criteriaBuilder.equal(team.get("name"), teamName);
        };
    }

    public static Specification<Member> username(final String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);
    }

}
