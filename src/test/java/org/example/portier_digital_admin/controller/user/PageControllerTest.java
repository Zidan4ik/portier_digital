package org.example.portier_digital_admin.controller.user;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PageControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private PageController pageController;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pageController).build();
    }

    @Test
    void testViewAbout() throws Exception {
        mockMvc.perform(get("/user/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/about"));
    }

    @Test
    void testViewBlogs() throws Exception {
        mockMvc.perform(get("/user/blogs"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/blogs"));
    }

    @Test
    void testViewHome() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/home"));
    }

    @Test
    void testViewPortfolio() throws Exception {
        mockMvc.perform(get("/user/portfolio"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/portfolio"));
    }

    @Test
    void testViewBlog() throws Exception {
        mockMvc.perform(get("/user/blog"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/blog"));
    }
}