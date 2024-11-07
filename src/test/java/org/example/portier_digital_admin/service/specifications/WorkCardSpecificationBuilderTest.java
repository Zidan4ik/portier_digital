package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.WorkCardDTOForView;
import org.example.portier_digital_admin.entity.WorkCard;
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
class WorkCardSpecificationBuilderTest {

    @Mock
    private Root<WorkCard> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Test
    void WorkCardSpecificationBuilder_GetSpecification_WithAllExistFields() {
        WorkCardDTOForView request = new WorkCardDTOForView(1L,"Test Title");

        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("title"), "%Test Title%")).thenReturn(predicate);

        Specification<WorkCard> specification = WorkCardSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("title"), "%Test Title%");
    }
    @Test
    void WorkCardSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        WorkCardDTOForView request = new WorkCardDTOForView(null,"");

        Specification<WorkCard> specification = WorkCardSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }

    @Test
    void WorkCardSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        WorkCardDTOForView request = new WorkCardDTOForView();

        Specification<WorkCard> specification = WorkCardSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}