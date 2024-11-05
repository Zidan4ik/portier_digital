package org.example.portier_digital_admin.repository;

import jakarta.annotation.Nonnull;
import org.example.portier_digital_admin.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {
    @Nonnull
    Page<Skill> findAll(Specification<Skill> specification, @Nonnull Pageable pageable);
}
