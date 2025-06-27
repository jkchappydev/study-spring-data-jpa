package com.studyspringdatajpa.dto;

import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class CustomPage<T> {

    // 이 필드는 클래스 전역에서 사용되며,
    // 내부에 어떤 타입의 데이터가 들어올지는 상황에 따라 달라질 수 있습니다.
    // 예를 들어 User 목록일 수도 있고, Board 목록일 수도 있습니다.
    // 따라서 제네릭 타입 <T>를 사용해 유연하게 처리합니다.
    // 예: List<User>, List<Board> 등 상황에 따라 유연하게 대응 가능
    private List<T> content;
    private CustomPageResponse pageInfo;

    public CustomPage(List<T> content, Pageable pageable, long total) {
        this.content = content;
        this.pageInfo = new CustomPageResponse(new PageImpl(content, pageable, total));
    }

}
