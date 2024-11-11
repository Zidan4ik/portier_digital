package org.example.portier_digital_admin.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.portier_digital_admin.dto.SubscriberDTOForView;
import org.example.portier_digital_admin.entity.Subscriber;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class SubscriberSpecificationBuilder {
    public static Specification<Subscriber> getSpecification(SubscriberDTOForView request) {
        return Specification.where(
                        hasId(request.getId()))
                .and(likeEmail(request.getEmail()));
    }

    private static Specification<Subscriber> hasId(Long id) {
        if (id == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Subscriber> likeEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%"));
    }
}
