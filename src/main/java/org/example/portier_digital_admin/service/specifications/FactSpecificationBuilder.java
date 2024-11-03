package org.example.portier_digital_admin.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.portier_digital_admin.dto.FactDTOForView;
import org.example.portier_digital_admin.entity.Fact;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class FactSpecificationBuilder {
    public static Specification<Fact> getSpecification(FactDTOForView request) {
        return Specification.where(
                        hasId(request.getId()))
                .and(likeTitle(request.getTitle()))
                .and(likeDescription(request.getDescription()));
    }

    private static Specification<Fact> hasId(Long id) {
        if (id == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Fact> likeTitle(String title) {
        if (title == null || title.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%" + title + "%"));
    }

    private static Specification<Fact> likeDescription(String description) {
        if (description == null || description.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("description"), "%" + description + "%"));
    }
}
