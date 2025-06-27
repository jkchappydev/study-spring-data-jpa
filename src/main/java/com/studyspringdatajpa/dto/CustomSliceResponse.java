package com.studyspringdatajpa.dto;

import lombok.Getter;
import org.springframework.data.domain.SliceImpl;

@Getter
public class CustomSliceResponse {

    private boolean first;
    private boolean last;
    private boolean hasNext;
    private int page;
    private int size;

    public CustomSliceResponse(SliceImpl page) {
        this.first = page.isFirst();
        this.last = page.isLast();
        this.hasNext = page.hasNext();
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
    }

}
