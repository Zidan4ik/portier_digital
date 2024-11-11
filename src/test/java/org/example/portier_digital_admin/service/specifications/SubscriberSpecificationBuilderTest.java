package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.SubscriberDTOForView;
import org.example.portier_digital_admin.entity.Subscriber;
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
class SubscriberSpecificationBuilderTest {
    @Mock
    private Root<Subscriber> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Test
    void SubscriberSpecificationBuilder_GetSpecification_WithAllExistFields() {
        SubscriberDTOForView request = SubscriberDTOForView.builder().id(1L).email("email@gmail.com").build();
        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("email"), "%email@gmail.com%")).thenReturn(predicate);

        Specification<Subscriber> specification = SubscriberSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("email"), "%email@gmail.com%");
    }
    @Test
    void SubscriberSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        SubscriberDTOForView request = SubscriberDTOForView.builder().id(1L).email("").build();

        Specification<Subscriber> specification = SubscriberSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
    }

    @Test
    void SubscriberSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        SubscriberDTOForView request = SubscriberDTOForView.builder().build();

        Specification<Subscriber> specification = SubscriberSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}