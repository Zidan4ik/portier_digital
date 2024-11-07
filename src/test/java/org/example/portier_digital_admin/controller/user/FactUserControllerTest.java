package org.example.portier_digital_admin.controller.user;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.portier_digital_admin.dto.FactDTOForAdd;
import org.example.portier_digital_admin.service.FactService;
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
class FactUserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private FactService factService;

    @InjectMocks
    private FactUserController factUserController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(factUserController).build();
    }

    @Test
    void testGetFacts() throws Exception {
        FactDTOForAdd fact1 = new FactDTOForAdd(1L,"10%","The best moment for company");
        FactDTOForAdd fact2 = new FactDTOForAdd(2L,"20%","The bad moment for company");

        when(factService.getAll()).thenReturn(List.of(fact1, fact2));

        mockMvc.perform(get("/facts-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("10%")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("20%")));

        verify(factService, times(1)).getAll();
    }
}