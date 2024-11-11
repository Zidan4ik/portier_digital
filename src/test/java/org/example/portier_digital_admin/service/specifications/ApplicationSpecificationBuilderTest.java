package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.ApplicationDTOForView;
import org.example.portier_digital_admin.entity.Application;
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
class ApplicationSpecificationBuilderTest {
    @Mock
    private Root<Application> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Test
    void ApplicationSpecificationBuilder_GetSpecification_WithAllExistFields() {
        ApplicationDTOForView request = ApplicationDTOForView.builder().id(1L).name("name").email("email").desire("desire").build();

        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("name"), "%name%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("email"), "%email%")).thenReturn(predicate);

        Specification<Application> specification = ApplicationSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("name"), "%name%");
        verify(criteriaBuilder).like(root.get("email"), "%email%");
    }
    @Test
    void ApplicationSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        ApplicationDTOForView request = ApplicationDTOForView.builder().id(null).name("").email("").desire("").build();
        request.setEmail("");
        Specification<Application> specification = ApplicationSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }

    @Test
    void ApplicationSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        ApplicationDTOForView request = ApplicationDTOForView.builder().build();

        Specification<Application> specification = ApplicationSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}