package com.studyspringdatajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    // 1. close projection (select 절 최적화 됨)
    String getUsername();
    int getAge();

    // 2. open projection (select 절 최적화 되지는 않음)
    // @Value("#{target.username + ' ' + target.age}")
    // String getUsername();

}
