package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.FactDTOForView;
import org.example.portier_digital_admin.entity.Fact;
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
class FactSpecificationBuilderTest {

    @Mock
    private Root<Fact> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Test
    void FactSpecificationBuilder_GetSpecification_WithAllExistFields() {
        FactDTOForView request = new FactDTOForView(1L,"Test Title","Test Description");

        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("title"), "%Test Title%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("description"), "%Test Description%")).thenReturn(predicate);

        Specification<Fact> specification = FactSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("title"), "%Test Title%");
        verify(criteriaBuilder).like(root.get("description"), "%Test Description%");
    }
    @Test
    void FactSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        FactDTOForView request = new FactDTOForView(null,"","");

        Specification<Fact> specification = FactSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }

    @Test
    void FactSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        FactDTOForView request = new FactDTOForView();

        Specification<Fact> specification = FactSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}