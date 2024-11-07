package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.SkillDTOForView;
import org.example.portier_digital_admin.entity.Skill;
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
class SkillSpecificationBuilderTest {

    @Mock
    private Root<Skill> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Test
    void SkillSpecificationBuilder_GetSpecification_WithAllExistFields() {
        SkillDTOForView request = new SkillDTOForView(1L,"Test Title","Test Description");

        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("title"), "%Test Title%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("description"), "%Test Description%")).thenReturn(predicate);

        Specification<Skill> specification = SkillSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("title"), "%Test Title%");
        verify(criteriaBuilder).like(root.get("description"), "%Test Description%");
    }
    @Test
    void SkillSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        SkillDTOForView request = new SkillDTOForView(null,"","");

        Specification<Skill> specification = SkillSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }

    @Test
    void SkillSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        SkillDTOForView request = new SkillDTOForView();

        Specification<Skill> specification = SkillSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}