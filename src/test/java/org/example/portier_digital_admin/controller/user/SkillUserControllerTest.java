package org.example.portier_digital_admin.controller.user;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.portier_digital_admin.dto.SkillDTOForAdd;
import org.example.portier_digital_admin.service.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@ExtendWith(MockitoExtension.class)
class SkillUserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillUserController skillUserController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(skillUserController).build();
    }

    @Test
    void testGetSkills() throws Exception {
        SkillDTOForAdd skill1 = new SkillDTOForAdd(1L,"Java","The most of simplest language among other",null,null);
        SkillDTOForAdd skill2 = new SkillDTOForAdd(2L,"Spring Boot","The most of bigger framework for Java",null,null);

        when(skillService.getAll()).thenReturn(List.of(skill1, skill2));

        mockMvc.perform(get("/skills-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Java")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Spring Boot")));

        verify(skillService, times(1)).getAll();
    }
}