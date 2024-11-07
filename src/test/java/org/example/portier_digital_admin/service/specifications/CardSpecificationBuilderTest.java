package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.CardDTOForView;
import org.example.portier_digital_admin.entity.Card;
import org.junit.jupiter.api.BeforeEach;
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
class CardSpecificationBuilderTest {
    @Mock
    private Root<Card> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @BeforeEach
    void setUp() {

    }

    @Test
    void ArticleSpecificationBuilder_GetSpecification_WithAllExistFields() {
        CardDTOForView request = new CardDTOForView(1L,"Test Title","Test Description");

        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("title"), "%Test Title%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("description"), "%Test Description%")).thenReturn(predicate);

        Specification<Card> specification = CardSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("title"), "%Test Title%");
        verify(criteriaBuilder).like(root.get("description"), "%Test Description%");
    }
    @Test
    void CardSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        CardDTOForView request = new CardDTOForView(null,"","");

        Specification<Card> specification = CardSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }

    @Test
    void CardSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        CardDTOForView request = new CardDTOForView();

        Specification<Card> specification = CardSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}