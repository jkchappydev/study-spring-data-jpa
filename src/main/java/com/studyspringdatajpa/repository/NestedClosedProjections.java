package com.studyspringdatajpa.repository;

public interface NestedClosedProjections {

    String getUsername(); // 최적화 됨 (username 만 가져옴) - root라고 부름
    TeamInfo getTeam(); // Team은 최적화 되지 않음

    interface TeamInfo {
        String getName();
    }

}
