package org.example.portier_digital_admin.service.specifications;

import lombok.experimental.UtilityClass;
import org.example.portier_digital_admin.dto.ReviewDTOForView;
import org.example.portier_digital_admin.entity.Review;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ReviewSpecificationBuilder {
    public static Specification<Review> getSpecification(ReviewDTOForView request) {
        return Specification.where(
                        hasId(request.getId()))
                .and(likeFirstName(request.getFirstName()))
                .and(likeLastName(request.getLastName()))
                .and(likePosition(request.getPosition()))
                .and(likeText(request.getText()));
    }

    private static Specification<Review> hasId(Long id) {
        if (id == null) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id));
    }

    private static Specification<Review> likeFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"));
    }

    private static Specification<Review> likeLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
    }

    private static Specification<Review> likePosition(String position) {
        if (position == null || position.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("position"), "%" + position + "%"));
    }

    private static Specification<Review> likeText(String text) {
        if (text == null || text.isBlank()) return null;
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("text"), "%" + text + "%"));
    }

}
