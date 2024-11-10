package org.example.portier_digital_admin.repository;

import jakarta.annotation.Nonnull;
import org.example.portier_digital_admin.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long>, JpaSpecificationExecutor<Application> {
    @Nonnull
    Page<Application> findAll(Specification<Application> specification, @Nonnull Pageable pageable);
}
