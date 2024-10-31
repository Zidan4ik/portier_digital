package org.example.portier_digital_admin.repository;

import org.example.portier_digital_admin.entity.Fact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactRepository extends JpaRepository<Fact,Long> {
}
