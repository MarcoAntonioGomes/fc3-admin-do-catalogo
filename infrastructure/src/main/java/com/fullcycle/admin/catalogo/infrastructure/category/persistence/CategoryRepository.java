package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryJPAEntity, String> {


    Page<CategoryJPAEntity> findAll(Specification<CategoryJPAEntity> whereClause, Pageable pageable);

    @Query(value = "SELECT c.id FROM Category c WHERE c.id IN :ids")
    List<String> existsByIdIs(@Param("ids") List<String> ids);

}
