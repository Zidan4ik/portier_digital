package org.example.portier_digital_admin.controller.admin;

import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.ApplicationDTOForView;
import org.example.portier_digital_admin.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {
    @InjectMocks
    private ApplicationController articleController;

    @Mock
    private ApplicationService articleService;

    private MockMvc mockMvc;
    private ApplicationDTOForView applicationDTOForView;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
        applicationDTOForView = ApplicationDTOForView.builder().id(1L).email("email@gmail.com").build();
    }

    @Test
    void ApplicationCardController_ViewApplications() throws Exception {
        mockMvc.perform(get("/admin/applications"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/applications"));
    }

    @Test
    void ApplicationCardController_GetApplications() throws Exception {
        PageResponse<ApplicationDTOForView> pageResponse = new PageResponse<>(List.of(applicationDTOForView),null);

        when(articleService.getAll(any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/applications/data")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(articleService, times(1)).getAll(any(), any());
    }

    @Test
    void ApplicationCardController_DeleteApplicationById() throws Exception {

        mockMvc.perform(get("/admin/application/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application with id 1 was deleted!"));

        verify(articleService, times(1)).deleteById(anyLong());
    }
}