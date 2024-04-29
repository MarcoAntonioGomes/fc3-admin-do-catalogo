package com.fullcycle.admin.catalogo.infrastructure.castmember.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastMemberRepository extends JpaRepository<CastMemberJPAEntity, String> {

    Page<CastMemberJPAEntity> findAll(Specification<CastMemberJPAEntity> specification, Pageable pageable);


}
