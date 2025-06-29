package com.studyspringdatajpa.dto;

import lombok.Getter;
import org.springframework.data.domain.*;

@Getter
public class CustomPageResponse {

    private boolean first;
    private boolean last;
    private boolean hasNext;
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;

    public CustomPageResponse(PageImpl page) {
        this.first = page.isFirst();
        this.last = page.isLast();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
    }

}
