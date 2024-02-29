package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;
import java.util.zip.CRC32;

public interface CategoryGateway {

    Category create(Category aCategory);
    void deleteById(CategoryID anId);
    Optional<Category> findById(CategoryID anId);

    Category update(Category aCategory);

    Pagination<Category> findAll(CategorySearchQuery aQuery);

}
