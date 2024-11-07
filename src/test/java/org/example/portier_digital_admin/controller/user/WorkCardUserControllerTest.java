package org.example.portier_digital_admin.controller.user;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.repository.WorkCardRepository;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WorkCardUserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private WorkCardRepository cardRepository;

    @InjectMocks
    private WorkCardUserController workCardUserController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(workCardUserController).build();
    }

    @Test
    void testGetWorkCards() throws Exception {
        WorkCard workCard1 = new WorkCard(1L,"Title1",null);
        WorkCard workCard2 = new WorkCard(2L,"Title2",null);

        when(cardRepository.findAll()).thenReturn(List.of(workCard1, workCard2));

        mockMvc.perform(get("/workCard-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Title1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Title2")));

        verify(cardRepository, times(1)).findAll();
    }
}