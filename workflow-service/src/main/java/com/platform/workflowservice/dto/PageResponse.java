package com.platform.workflowservice.dto;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public class PageResponse<T> implements Serializable {

    private List<T> content;
    private int page;
    private int size;
    private long totalElement;
    private int totalPage;

    public PageResponse(){}

    public PageResponse(Page<T> page){
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElement = page.getTotalElements();
        this.totalPage = page.getTotalPages();
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElement() {
        return totalElement;
    }

    public int getTotalPage() {
        return totalPage;
    }
}
