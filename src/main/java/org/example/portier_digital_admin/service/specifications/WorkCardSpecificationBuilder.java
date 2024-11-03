package org.example.portier_digital_admin.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.portier_digital_admin.dto.WorkCardDTOForView;
import org.example.portier_digital_admin.entity.WorkCard;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class WorkCardSpecificationBuilder {
    public static Specification<WorkCard> getSpecification(WorkCardDTOForView request) {
        return Specification.where(
                        hasId(request.getId()))
                .and(likeTitle(request.getTitle()));
    }

    private static Specification<WorkCard> hasId(Long id) {
        if (id == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<WorkCard> likeTitle(String title) {
        if (title == null || title.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%" + title + "%"));
    }
}
