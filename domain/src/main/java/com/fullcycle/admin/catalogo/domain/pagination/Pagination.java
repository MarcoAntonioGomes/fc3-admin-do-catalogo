package com.fullcycle.admin.catalogo.domain.pagination;

import java.util.List;

public record Pagination<T>(
        int currentPage,
        int perPage,
        int total,
        List<T> items
) {
}
