package com.electronic.store.Electronic_Store.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedResponseDTO <T>{
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PaginatedResponseDTO(List<T> content, int pageNumber, int pageSize,
                             long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }
}
