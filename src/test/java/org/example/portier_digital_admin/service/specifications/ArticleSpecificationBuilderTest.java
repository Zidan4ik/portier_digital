package org.example.portier_digital_admin.service.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.portier_digital_admin.dto.ArticleDTOForView;
import org.example.portier_digital_admin.entity.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleSpecificationBuilderTest {

    @Mock
    private Root<Article> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate predicate;

    @Test
    void ArticleSpecificationBuilder_GetSpecification_WithAllExistFields() {
        ArticleDTOForView request = new ArticleDTOForView(1L,"Test Title","Test Description");

        when(criteriaBuilder.equal(root.get("id"), 1L)).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("title"), "%Test Title%")).thenReturn(predicate);
        when(criteriaBuilder.like(root.get("description"), "%Test Description%")).thenReturn(predicate);

        Specification<Article> specification = ArticleSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).equal(root.get("id"), 1L);
        verify(criteriaBuilder).like(root.get("title"), "%Test Title%");
        verify(criteriaBuilder).like(root.get("description"), "%Test Description%");
    }
    @Test
    void ArticleSpecificationBuilder_GetSpecification_WhereFieldsAreNotBlank() {
        ArticleDTOForView request = new ArticleDTOForView(null,"","");

        Specification<Article> specification = ArticleSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }

    @Test
    void ArticleSpecificationBuilder_GetSpecification_WhereAllFieldsAreNull() {
        ArticleDTOForView request = new ArticleDTOForView();

        Specification<Article> specification = ArticleSpecificationBuilder.getSpecification(request);

        assertNotNull(specification);
        Predicate resultPredicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(resultPredicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}