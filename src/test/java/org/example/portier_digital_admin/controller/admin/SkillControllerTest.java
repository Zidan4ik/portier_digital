package org.example.portier_digital_admin.controller.admin;

import org.example.portier_digital_admin.dto.SkillDTOForAdd;
import org.example.portier_digital_admin.dto.SkillDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Skill;
import org.example.portier_digital_admin.service.SkillService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {
    @InjectMocks
    private SkillController skillController;

    @Mock
    private SkillService skillService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(skillController).build();
    }

    @Test
    void SkillCardController_ViewSkills() throws Exception {
        mockMvc.perform(get("/admin/skills"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/skills"));
    }

    @Test
    void SkillCardController_GetSkills() throws Exception {
        SkillDTOForView skillDTOForView = new SkillDTOForView();
        PageResponse<SkillDTOForView> pageResponse = new PageResponse<>(List.of(skillDTOForView),null);

        when(skillService.getAll(any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/skills/data")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(skillService, times(1)).getAll(any(), any());
    }

    @Test
    void SkillCardController_SaveNewSkill_Success() throws Exception {
        Skill skill = new Skill(1L, "Test Title", "Test Description", null);

        when(skillService.saveFile(any(SkillDTOForAdd.class))).thenReturn(skill);

        mockMvc.perform(post("/admin/skill/add")
                        .param("id", "1")
                        .param("title", "Test Title")
                        .param("description", "Test Description"))
                .andExpect(status().isCreated())
                .andExpect(content().string("New skill with id 1 was created!"));

        verify(skillService, times(1)).saveFile(any(SkillDTOForAdd.class));
    }

    @Test
    void SkillCardController_SaveNewSkill_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/skill/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void SkillCardController_EditSkill_Success() throws Exception {
        Skill skill = new Skill(1L, "Test Title", "Test Description", null);

        when(skillService.saveFile(any(SkillDTOForAdd.class))).thenReturn(skill);

        mockMvc.perform(post("/admin/skill/1/edit")
                        .param("id", "1")
                        .param("title", "Test Title")
                        .param("description", "Test Description"))
                .andExpect(status().isOk())
                .andExpect(content().string("Skill with id: 1 was updated!"));

        verify(skillService, times(1)).saveFile(any(SkillDTOForAdd.class));
    }

    @Test
    void SkillCardController_EditSkill_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/skill/1/edit"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void SkillCardController_GetSkillById() throws Exception {
        SkillDTOForAdd dto = new SkillDTOForAdd(1L, "Test Title", "Test Description", null,null);
        when(skillService.getByIdForAdd(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/admin/skill/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(skillService, times(1)).getByIdForAdd(anyLong());
    }

    @Test
    void SkillCardController_DeleteSkillById() throws Exception {
        mockMvc.perform(get("/admin/skill/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Skill with id 1 was deleted!"));

        verify(skillService, times(1)).deleteById(anyLong());
    }
}