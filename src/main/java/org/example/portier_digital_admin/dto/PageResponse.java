package org.example.portier_digital_admin.dto;

import lombok.Value;

import java.util.List;

@Value
public class PageResponse<T> {
    List<T> content;
    Metadata metadata;

    @Value
    public static class Metadata{
        int page;
        int size;
        long totalElements;
        long totalPages;
    }
}
