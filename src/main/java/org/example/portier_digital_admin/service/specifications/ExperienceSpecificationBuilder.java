package org.example.portier_digital_admin.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.portier_digital_admin.dto.ExperienceDTOForView;
import org.example.portier_digital_admin.entity.Experience;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ExperienceSpecificationBuilder {
    public static Specification<Experience> getSpecification(ExperienceDTOForView request) {
        return Specification.where(
                        hasId(request.getId()))
                .and(likeCompany(request.getCompany()))
                .and(likePosition(request.getPosition()));
    }

    private static Specification<Experience> hasId(Long id) {
        if (id == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Experience> likeCompany(String company) {
        if (company == null || company.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("company"), "%" + company + "%"));
    }

    private static Specification<Experience> likePosition(String position) {
        if (position == null || position.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("position"), "%" + position + "%"));
    }
}
