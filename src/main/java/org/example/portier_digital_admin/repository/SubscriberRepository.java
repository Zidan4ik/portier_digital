package org.example.portier_digital_admin.repository;

import jakarta.annotation.Nonnull;
import org.example.portier_digital_admin.entity.Subscriber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>, JpaSpecificationExecutor<Subscriber> {
    @Nonnull
    Page<Subscriber> findAll(Specification<Subscriber> specification, @Nonnull Pageable pageable);
}
