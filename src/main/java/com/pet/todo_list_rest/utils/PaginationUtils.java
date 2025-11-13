package com.pet.todo_list_rest.utils;


import com.pet.todo_list_rest.dto.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public final class PaginationUtils {

    private PaginationUtils() { }

    public static <E, D> PaginatedResponse<D> toPaginatedResponse(Page<E> page, Function<E, D> mapper) {
        List<D> content = page.getContent().stream()
                .map(mapper)
                .toList();

        return new PaginatedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
