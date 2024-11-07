package org.example.portier_digital_admin.controller.user;

import org.example.portier_digital_admin.dto.CardDTOForAdd;
import org.example.portier_digital_admin.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CardUserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardUserController cardUserController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardUserController).build();
    }

    @Test
    void testGetCards() throws Exception {
        CardDTOForAdd card1 = new CardDTOForAdd(1L,"Test Card 1",null,null,null);
        CardDTOForAdd card2 = new CardDTOForAdd(2L,"Test Card 2",null,null,null);

        when(cardService.getAll()).thenReturn(List.of(card1, card2));

        mockMvc.perform(get("/cards-data")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Card 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Test Card 2")));

        verify(cardService, times(1)).getAll();
    }
}