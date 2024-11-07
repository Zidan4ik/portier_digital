package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.ReviewDTOForView;
import org.example.portier_digital_admin.entity.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewSpecificationBuilderTest {

    @Mock
    private Root<Review> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Test
    void ReviewSpecificationBuilder_GetSpecification_WithAllExistFields() {
        ReviewDTOForView request = new ReviewDTOForView(
                1L, "Test First Name", "Test Last Name", "Test Text", "Test Position");

        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("firstName"), "%Test First Name%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("lastName"), "%Test Last Name%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("text"), "%Test Text%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("position"), "%Test Position%")).thenReturn(predicate);

        Specification<Review> specification = ReviewSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("firstName"), "%Test First Name%");
        verify(criteriaBuilder).like(root.get("lastName"), "%Test Last Name%");
        verify(criteriaBuilder).like(root.get("text"), "%Test Text%");
        verify(criteriaBuilder).like(root.get("position"), "%Test Position%");
    }

    @Test
    void ReviewSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        ReviewDTOForView request = new ReviewDTOForView(null, "", "", "", "");

        Specification<Review> specification = ReviewSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }

    @Test
    void ReviewSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        ReviewDTOForView request = new ReviewDTOForView();

        Specification<Review> specification = ReviewSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}