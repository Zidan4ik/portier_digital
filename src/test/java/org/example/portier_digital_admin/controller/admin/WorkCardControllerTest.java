package org.example.portier_digital_admin.controller.admin;


import org.example.portier_digital_admin.dto.WorkCardDTOForAdd;
import org.example.portier_digital_admin.dto.WorkCardDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.service.WorkCardService;
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
class WorkCardControllerTest {
    @InjectMocks
    private WorkCardController workCardController;

    @Mock
    private WorkCardService workCardService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(workCardController).build();
    }

    @Test
    void WorkCardController_ViewWorkCards() throws Exception {
        mockMvc.perform(get("/admin/work-cards"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/work-cards"));
    }

    @Test
    void WorkCardController_GetWorkCards() throws Exception {
        WorkCardDTOForView workCardDTOForView = new WorkCardDTOForView();
        PageResponse<WorkCardDTOForView> pageResponse = new PageResponse<>(List.of(workCardDTOForView),null);

        when(workCardService.getAll(any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/work-cards/data")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(workCardService, times(1)).getAll(any(), any());
    }

    @Test
    void WorkCardController_SaveNewWorkCard_Success() throws Exception {
        WorkCard workCard = new WorkCard(1L, "Test Title", null);

        when(workCardService.saveFile(any(WorkCardDTOForAdd.class))).thenReturn(workCard);

        mockMvc.perform(post("/admin/work-card/add")
                        .param("id", "1")
                        .param("title", "Test Title"))
                .andExpect(status().isCreated())
                .andExpect(content().string("New work card with id 1 was created!"));

        verify(workCardService, times(1)).saveFile(any(WorkCardDTOForAdd.class));
    }

    @Test
    void WorkCardController_SaveNewWorkCard_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/work-card/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void WorkCardController_EditWorkCard_Success() throws Exception {
        WorkCard workCard = new WorkCard(1L, "Test Title", null);

        when(workCardService.saveFile(any(WorkCardDTOForAdd.class))).thenReturn(workCard);

        mockMvc.perform(post("/admin/work-card/1/edit")
                        .param("id", "1")
                        .param("title", "Test Title"))
                .andExpect(status().isOk())
                .andExpect(content().string("Work card with id: 1 was updated!"));

        verify(workCardService, times(1)).saveFile(any(WorkCardDTOForAdd.class));
    }

    @Test
    void WorkCardController_EditWorkCard_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/work-card/1/edit"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void WorkCardController_GetWorkCardById() throws Exception {
        WorkCardDTOForAdd dto = new WorkCardDTOForAdd(1L, "Test Title", null,null);
        when(workCardService.getByIdForAdd(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/admin/work-card/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(workCardService, times(1)).getByIdForAdd(anyLong());
    }

    @Test
    void WorkCardController_DeleteWorkCardById() throws Exception {
        WorkCard workCard = new WorkCard(1L, "Test Title", null);

        when(workCardService.getById(anyLong())).thenReturn(workCard);

        mockMvc.perform(get("/admin/work-card/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Work card with id 1 was deleted!"));

        verify(workCardService, times(1)).getById(anyLong());
        verify(workCardService, times(1)).deleteById(anyLong());
    }
}