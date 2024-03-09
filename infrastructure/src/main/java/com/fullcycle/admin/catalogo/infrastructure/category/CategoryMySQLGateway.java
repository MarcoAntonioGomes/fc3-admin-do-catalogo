package com.fullcycle.admin.catalogo.infrastructure.category;


import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Optional;

import static com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.repository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return this.repository.save(CategoryJPAEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(CategoryID anId) {
        if (this.repository.existsById(anId.getValue())) {
            this.repository.deleteById(anId.getValue());
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return this.repository.findById(anId.getValue()).map(CategoryJPAEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return this.repository.save(CategoryJPAEntity.from(aCategory)).toAggregate();
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {


        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specification = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map (str ->
                    SpecificationUtils
                           .<CategoryJPAEntity>like("name", str)
                            .or(like("description", str))

                ).orElse(null);


        var pageResult = this.repository.findAll(Specification.where(specification), page);


        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJPAEntity::toAggregate).toList()
        );
    }
}
