package org.example.portier_digital_admin.repository;

import jakarta.annotation.Nonnull;
import org.example.portier_digital_admin.entity.Experience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long>, JpaSpecificationExecutor<Experience> {
    @Nonnull
    Page<Experience> findAll(Specification<Experience> specification, @Nonnull Pageable pageable);
}
