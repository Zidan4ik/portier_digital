package org.example.portier_digital_admin.repository;

import jakarta.annotation.Nonnull;
import org.example.portier_digital_admin.entity.WorkCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkCardRepository extends JpaRepository<WorkCard,Long>, JpaSpecificationExecutor<WorkCard> {
    @Nonnull
    Page<WorkCard> findAll(Specification<WorkCard> specification,@Nonnull Pageable pageable);
}
