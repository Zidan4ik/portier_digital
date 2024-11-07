package org.example.portier_digital_admin.controller.admin;

import org.example.portier_digital_admin.dto.FactDTOForAdd;
import org.example.portier_digital_admin.dto.FactDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Fact;
import org.example.portier_digital_admin.service.FactService;
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
class FactControllerTest {
    @InjectMocks
    private FactController factController;

    @Mock
    private FactService factService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(factController).build();
    }

    @Test
    void FactCardController_ViewFacts() throws Exception {
        mockMvc.perform(get("/admin/facts"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/facts"));
    }

    @Test
    void FactCardController_GetFacts() throws Exception {
        FactDTOForView factDTOForView = new FactDTOForView();
        PageResponse<FactDTOForView> pageResponse = new PageResponse<>(List.of(factDTOForView),null);

        when(factService.getAll(any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/facts/data")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(factService, times(1)).getAll(any(), any());
    }

    @Test
    void FactCardController_SaveNewFact_Success() throws Exception {
        Fact fact = new Fact(1L, "Test Title", "Test Description");

        when(factService.save(any(FactDTOForAdd.class))).thenReturn(fact);

        mockMvc.perform(post("/admin/fact/add")
                        .param("id", "1")
                        .param("title", "Test Title")
                        .param("description", "Test Description"))
                .andExpect(status().isCreated())
                .andExpect(content().string("New fact with id 1 was created!"));

        verify(factService, times(1)).save(any(FactDTOForAdd.class));
    }

    @Test
    void FactCardController_SaveNewFact_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/fact/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void FactCardController_EditFact_Success() throws Exception {
        Fact fact = new Fact(1L, "Test Title", "Test Description");

        when(factService.save(any(FactDTOForAdd.class))).thenReturn(fact);

        mockMvc.perform(post("/admin/fact/1/edit")
                        .param("id", "1")
                        .param("title", "Test Title")
                        .param("description", "Test Description"))
                .andExpect(status().isOk())
                .andExpect(content().string("Fact with id: 1 was updated!"));

        verify(factService, times(1)).save(any(FactDTOForAdd.class));
    }

    @Test
    void FactCardController_EditFact_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/fact/1/edit"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void FactCardController_GetFactById() throws Exception {
        FactDTOForAdd dto = new FactDTOForAdd(1L, "Test Title", "Test Description");
        when(factService.getByIdForAdd(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/admin/fact/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(factService, times(1)).getByIdForAdd(anyLong());
    }

    @Test
    void FactCardController_DeleteFactById() throws Exception {
        mockMvc.perform(get("/admin/fact/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Fact with id 1 was deleted!"));
        verify(factService, times(1)).deleteById(anyLong());
    }
}