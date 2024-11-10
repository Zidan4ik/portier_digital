package org.example.portier_digital_admin.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.portier_digital_admin.dto.ApplicationDTOForView;
import org.example.portier_digital_admin.entity.Application;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ApplicationSpecificationBuilder {
    public static Specification<Application> getSpecification(ApplicationDTOForView request) {
        return Specification.where(
                        hasId(request.getId()))
                .and(likeName(request.getName()))
                .and(likeEmail(request.getEmail()));
    }

    private static Specification<Application> hasId(Long id) {
        if (id == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Application> likeName(String name) {
        if (name == null || name.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%"));
    }
    private static Specification<Application> likeEmail(String email) {
        if (email == null || email.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%"));
    }
}
