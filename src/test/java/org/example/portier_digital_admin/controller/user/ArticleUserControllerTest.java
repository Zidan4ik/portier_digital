package org.example.portier_digital_admin.controller.user;

import org.example.portier_digital_admin.dto.ArticleDTOForAdd;
import org.example.portier_digital_admin.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ArticleUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleUserController articleUserController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleUserController).build();
    }

    @Test
    void testGetArticles() throws Exception {
        ArticleDTOForAdd article1 = new ArticleDTOForAdd(1L, "Test Title", null, null, null);

        when(articleService.getAll()).thenReturn(List.of(article1, new ArticleDTOForAdd()));

        mockMvc.perform(get("/user/articles-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$", hasSize(2)),
                        jsonPath("$[0].id", is(1)),
                        jsonPath("$[0].title", is("Test Title")));
        verify(articleService, times(1)).getAll();
    }

    @Test
    void testShowArticle() throws Exception {
        mockMvc.perform(get("/user/article/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/blog"));
    }

    @Test
    void testGetArticleById() throws Exception {
        ArticleDTOForAdd article1 = new ArticleDTOForAdd(1L, "Test Title", null, null, null);

        when(articleService.getByIdForAdd(1L)).thenReturn(article1);

        mockMvc.perform(get("/user/article/1/data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id", is(1)),
                        jsonPath("$.title", is("Test Title")));

        verify(articleService, times(1)).getByIdForAdd(1L);
    }
}