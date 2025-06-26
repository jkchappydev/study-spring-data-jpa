package com.studyspringdatajpa.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class CommonPageRequest {

    private int page = 1;
    private int size = 10;
    private String sortBy;
    private String direction = "asc";

    public PageRequest toPageRequest() {
        int pageIndex = Math.max(page - 1, 0);
        int pageSize = Math.max(size, 1);

        if (sortBy != null && !sortBy.isBlank()) {
            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            return PageRequest.of(pageIndex, pageSize, sort);
        } else {
            return PageRequest.of(pageIndex, pageSize); // 정렬 없음
        }
    }

}
