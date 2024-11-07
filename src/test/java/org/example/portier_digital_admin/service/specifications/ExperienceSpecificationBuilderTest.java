package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.ExperienceDTOForView;
import org.example.portier_digital_admin.entity.Experience;
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
class ExperienceSpecificationBuilderTest {
    @Mock
    private Root<Experience> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Test
    void ExperienceSpecificationBuilder_GetSpecification_WithAllExistFields() {
        ExperienceDTOForView request = new ExperienceDTOForView(1L,"Test Company","Test Position");

        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("company"), "%Test Company%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("position"), "%Test Position%")).thenReturn(predicate);

        Specification<Experience> specification = ExperienceSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("company"), "%Test Company%");
        verify(criteriaBuilder).like(root.get("position"), "%Test Position%");
    }
    @Test
    void ExperienceSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        ExperienceDTOForView request = new ExperienceDTOForView(null,"","");

        Specification<Experience> specification = ExperienceSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }

    @Test
    void ExperienceSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        ExperienceDTOForView request = new ExperienceDTOForView();

        Specification<Experience> specification = ExperienceSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}