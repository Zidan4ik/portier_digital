package org.example.portier_digital_admin.controller.admin;

import org.example.portier_digital_admin.dto.ExperienceDTOForAdd;
import org.example.portier_digital_admin.dto.ExperienceDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Experience;
import org.example.portier_digital_admin.service.ExperienceService;
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
class ExperienceControllerTest {
    @InjectMocks
    private ExperienceController experienceController;

    @Mock
    private ExperienceService experienceService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(experienceController).build();
    }

    @Test
    void ExperienceCardController_ViewExperiences() throws Exception {
        mockMvc.perform(get("/admin/experiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/experiences"));
    }

    @Test
    void ExperienceCardController_GetExperiences() throws Exception {
        ExperienceDTOForView experienceDTOForView = new ExperienceDTOForView();
        PageResponse<ExperienceDTOForView> pageResponse = new PageResponse<>(List.of(experienceDTOForView),null);

        when(experienceService.getAll(any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/experiences/data")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(experienceService, times(1)).getAll(any(), any());
    }

    @Test
    void ExperienceCardController_SaveNewExperience_Success() throws Exception {
        Experience experience = new Experience(1L, "Test Company", "Test Position", null);

        when(experienceService.saveFile(any(ExperienceDTOForAdd.class))).thenReturn(experience);

        mockMvc.perform(post("/admin/experience/add")
                        .param("id", "1")
                        .param("company", "Test Company")
                        .param("position", "Test Position"))
                .andExpect(status().isCreated())
                .andExpect(content().string("New experience with id 1 was created!"));

        verify(experienceService, times(1)).saveFile(any(ExperienceDTOForAdd.class));
    }

    @Test
    void ExperienceCardController_SaveNewExperience_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/experience/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ExperienceCardController_EditExperience_Success() throws Exception {
        Experience experience = new Experience(1L, "Test Company", "Test Position", null);

        when(experienceService.saveFile(any(ExperienceDTOForAdd.class))).thenReturn(experience);

        mockMvc.perform(post("/admin/experience/1/edit")
                        .param("id", "1")
                        .param("company", "Test Company")
                        .param("position", "Test Position"))
                .andExpect(status().isOk())
                .andExpect(content().string("Experience with id: 1 was updated!"));

        verify(experienceService, times(1)).saveFile(any(ExperienceDTOForAdd.class));
    }

    @Test
    void ExperienceCardController_EditExperience_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/experience/1/edit"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ExperienceCardController_GetExperienceById() throws Exception {
        ExperienceDTOForAdd dto = new ExperienceDTOForAdd(1L, "Test Company", "Test Position", null,null);
        when(experienceService.getByIdForAdd(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/admin/experience/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(experienceService, times(1)).getByIdForAdd(anyLong());
    }

    @Test
    void ExperienceCardController_DeleteExperienceById() throws Exception {
        mockMvc.perform(get("/admin/experience/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Experience with id 1 was deleted!"));

        verify(experienceService, times(1)).deleteById(anyLong());
    }
}