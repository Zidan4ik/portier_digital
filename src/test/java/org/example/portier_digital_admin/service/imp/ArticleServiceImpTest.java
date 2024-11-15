package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.portier_digital_admin.dto.ArticleDTOForAdd;
import org.example.portier_digital_admin.dto.ArticleDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.repository.ArticleRepository;
import org.example.portier_digital_admin.service.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImpTest {
    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ArticleServiceImp articleService;
    private Article article;
    private ArticleDTOForAdd articleDTOForAdd;
    private ArticleDTOForView articleDTOForView;
    private static final Long ID = 1L;
    private final String basePath = "/uploads";

    @BeforeEach
    void setUp() {
        article = new Article(1L, "Title", "Content", "/uploads/articles/newImage.jpg");
        articleDTOForAdd = new ArticleDTOForAdd(1L, "Title", "Content", null,
                new MockMultipartFile("image-name1", "image1.html", "text/html", "content".getBytes()));
        articleDTOForView = new ArticleDTOForView(1L, "Title", "Content");
        ReflectionTestUtils.setField(articleService, "path", basePath);
    }

    @Test
    void ArticleServiceImp_GetAll_ReturnAllArticles() {
        List<Article> articlesList = Collections.singletonList(article);
        Mockito.when(articleRepository.findAll()).thenReturn(articlesList);
        List<ArticleDTOForAdd> articles = articleService.getAll();
        assertNotNull(articles);
        assertEquals(1, articles.size(), "Sizes should be match");
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void ArticleServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Article> articlePage = new PageImpl<>(Collections.singletonList(article));
        when(articleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(articlePage);
        PageResponse<ArticleDTOForView> response = articleService.getAll(articleDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(articleRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void ArticleServiceImp_GetById_ReturnArticle() {
        when(articleRepository.findById(ID)).thenReturn(Optional.of(article));
        Article result = articleService.getById(ID);
        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(articleRepository, times(1)).findById(ID);
    }

    @Test
    void ArticleServiceImp_GetByIdForAdd_ReturnArticleDTOForAdd() {
        when(articleRepository.findById(ID)).thenReturn(Optional.of(article));
        ArticleDTOForAdd result = articleService.getByIdForAdd(ID);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(articleRepository, times(1)).findById(ID);
    }

    @Test
    void ArticleServiceImp_GetById_ThrowsException() {
        when(articleRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> articleService.getById(1L));
    }

    @Test
    void ArticleServiceImp_Save_ReturnArticle() {
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        Article savedArticle = articleService.save(articleDTOForAdd);
        assertNotNull(savedArticle);
        assertEquals(article.getId(), savedArticle.getId(), "Id's should be match");
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void ArticleServiceImp_DeleteById_WhenArticleIsPresent() throws IOException {
        when(articleRepository.findById(ID)).thenReturn(Optional.of(article));
        articleService.deleteById(ID);
        verify(articleRepository, times(1)).deleteById(ID);
        verify(imageService, times(1)).deleteByPath(anyString());
    }

    @Test
    void ArticleServiceImp_DeleteById_WhenArticleNotFound() {
        when(articleRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> articleService.deleteById(1L));
    }

    @Test
    void ArticleServiceImp_SaveFile_WheArticleIsNotExist() {
        when(articleRepository.findById(articleDTOForAdd.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> articleService.saveFile(articleDTOForAdd));
        verify(articleRepository, times(1)).findById(articleDTOForAdd.getId());
    }

    @Test
    void ArticleServiceImp_DeleteById_WhenImageIsNotFound() {
        article.setPathToImage(null);
        when(articleRepository.findById(ID)).thenReturn(Optional.of(article));
        articleService.deleteById(ID);
        verify(articleRepository, times(1)).deleteById(ID);
    }

    @Test
    void ArticleServiceImp_DeleteById_WhenImageIsEmpty() {
        article.setPathToImage("");
        when(articleRepository.findById(ID)).thenReturn(Optional.of(article));
        articleService.deleteById(ID);
        verify(articleRepository, times(1)).deleteById(ID);
    }

    @Test
    void ArticleServiceImp_SaveFile() {
        when(articleRepository.findById(articleDTOForAdd.getId())).thenReturn(Optional.of(article));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        Article savedArticle = articleService.saveFile(articleDTOForAdd);
        assertNotNull(savedArticle);
        verify(articleRepository, times(1)).save(any(Article.class));
        verify(imageService, times(1)).save(any(), eq("/uploads/articles/newImage.jpg"));
    }

    @Test
    void ArticleServiceImp_SaveFile_WhenPathToImageIsNull() {
        article.setPathToImage(null);
        when(articleRepository.findById(articleDTOForAdd.getId())).thenReturn(Optional.of(article));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        Article savedArticle = articleService.saveFile(articleDTOForAdd);
        assertNotNull(savedArticle);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void ArticleServiceImp_SaveFile_WhenPathsAreNotEqual() {
        article.setPathToImage("path/to/image1");
        articleDTOForAdd.setPathToImage("path/to/image1");
        when(articleRepository.findById(articleDTOForAdd.getId())).thenReturn(Optional.of(article));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        Article savedArticle = articleService.saveFile(articleDTOForAdd);
        assertNotNull(savedArticle);
        verify(articleRepository, times(1)).save(any(Article.class));
        verify(imageService, times(1)).save(any(), eq("path/to/image1"));
    }

    @Test
    void ArticleServiceImp_SaveFile_WhenIdIsNull() {
        when(articleRepository.save(any(Article.class))).thenReturn(article);
        Article savedArticle = articleService.saveFile(new ArticleDTOForAdd());
        assertNotNull(savedArticle);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void ArticleServiceImp_SaveFile_WhenFileIsNull() throws IOException {
        ArticleDTOForAdd dto = new ArticleDTOForAdd(1L, null, null, null, null);
        Article existingArticle = new Article(1L, null, "/base/path/articles/old-image.jpg", null);
        Article savedArticle = new Article(1L, null, null, null);

        Mockito.when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        Mockito.when(articleRepository.save(Mockito.any())).thenReturn(savedArticle);

        articleService.saveFile(dto);

        Mockito.verify(imageService, Mockito.never()).deleteByPath(Mockito.anyString());
        Mockito.verify(articleRepository).save(Mockito.any(Article.class));
    }

    @Test
    void convertToRelativePath_withAbsolutePathContainingBasePath_shouldReturnRelativePath() {
        String absolutePath = "/uploads/article/image.jpg";
        String relativePath = articleService.convertToRelativePath(absolutePath);
        Assertions.assertEquals("/article/image.jpg", relativePath);
    }

    @Test
    void convertToRelativePath_withNullAbsolutePath_shouldReturnNull() {
        String absolutePath = null;
        String relativePath = articleService.convertToRelativePath(absolutePath);
        Assertions.assertNull(relativePath);
    }
}
