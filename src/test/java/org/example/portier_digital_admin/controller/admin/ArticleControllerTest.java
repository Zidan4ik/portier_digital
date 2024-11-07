package org.example.portier_digital_admin.controller.admin;

import org.example.portier_digital_admin.dto.ArticleDTOForAdd;
import org.example.portier_digital_admin.dto.ArticleDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {
    @InjectMocks
    private ArticleController articleController;

    @Mock
    private ArticleService articleService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
    }

    @Test
    void ArticleCardController_ViewArticles() throws Exception {
        mockMvc.perform(get("/admin/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/articles"));
    }

    @Test
    void ArticleCardController_GetArticles() throws Exception {
        ArticleDTOForView articleDTOForView = new ArticleDTOForView();
        PageResponse<ArticleDTOForView> pageResponse = new PageResponse<>(List.of(articleDTOForView),null);

        when(articleService.getAll(any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/articles/data")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(articleService, times(1)).getAll(any(), any());
    }

    @Test
    void ArticleCardController_SaveNewArticle_Success() throws Exception {
        Article article = new Article(1L, "Test Title", "Test Description", null);

        when(articleService.saveFile(any(ArticleDTOForAdd.class))).thenReturn(article);

        mockMvc.perform(post("/admin/article/add")
                        .param("id", "1")
                        .param("title", "Test Title")
                        .param("description", "Test Description"))
                .andExpect(status().isCreated())
                .andExpect(content().string("New article with id 1 was created!"));

        verify(articleService, times(1)).saveFile(any(ArticleDTOForAdd.class));
    }

    @Test
    void ArticleCardController_SaveNewArticle_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/article/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ArticleCardController_EditArticle_Success() throws Exception {
        Article article = new Article(1L, "Test Title", "Test Description", null);

        when(articleService.saveFile(any(ArticleDTOForAdd.class))).thenReturn(article);

        mockMvc.perform(post("/admin/article/1/edit")
                        .param("id", "1")
                        .param("title", "Test Title")
                        .param("description", "Test Description"))
                .andExpect(status().isOk())
                .andExpect(content().string("Article with id: 1 was updated!"));

        verify(articleService, times(1)).saveFile(any(ArticleDTOForAdd.class));
    }

    @Test
    void ArticleCardController_EditArticle_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/article/1/edit"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ArticleCardController_GetArticleById() throws Exception {
        ArticleDTOForAdd dto = new ArticleDTOForAdd(1L, "Test Title", "Test Description", null,null);
        when(articleService.getByIdForAdd(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/admin/article/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(articleService, times(1)).getByIdForAdd(anyLong());
    }

    @Test
    void ArticleCardController_DeleteArticleById() throws Exception {
        Article article = new Article(1L, "Test Title", "Test Description", null);

        when(articleService.getById(anyLong())).thenReturn(article);

        mockMvc.perform(get("/admin/article/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Article with id 1 was deleted!"));

        verify(articleService, times(1)).getById(anyLong());
        verify(articleService, times(1)).deleteById(anyLong());
    }
}