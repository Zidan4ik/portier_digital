package org.example.portier_digital_admin.controller.user;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.portier_digital_admin.dto.ExperienceDTOForAdd;
import org.example.portier_digital_admin.service.ExperienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExperienceUserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private ExperienceService experienceService;

    @InjectMocks
    private ExperienceUserController experienceUserController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(experienceUserController).build();
    }

    @Test
    void testGetExperiences() throws Exception {
        ExperienceDTOForAdd experience1 = new ExperienceDTOForAdd(
                2L,"Nyx","Junior Java Developer",null,null);
        ExperienceDTOForAdd experience2 = new ExperienceDTOForAdd(
                5L,"Chopper","Middle Java Developer",null,null);

        when(experienceService.getAll()).thenReturn(List.of(experience1,experience2));

        mockMvc.perform(get("/user/experiences-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].company", is("Nyx")))
                .andExpect(jsonPath("$[0].position", is("Junior Java Developer")));
        verify(experienceService, times(1)).getAll();
    }
}