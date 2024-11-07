package org.example.portier_digital_admin.controller.admin;

import org.example.portier_digital_admin.dto.CardDTOForAdd;
import org.example.portier_digital_admin.dto.CardDTOForView;
import org.example.portier_digital_admin.entity.Card;
import org.example.portier_digital_admin.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import org.example.portier_digital_admin.dto.PageResponse;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {
    @InjectMocks
    private CardController cardController;
    @Mock
    private CardService cardService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    void CardCardController_ViewCards() throws Exception {
        mockMvc.perform(get("/admin/cards"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/cards"));
    }

    @Test
    void CardCardController_GetCards() throws Exception {
        CardDTOForView dto = new CardDTOForView();
        Pageable pageable = PageRequest.of(0, 3);
        PageResponse<CardDTOForView> pageResponse = new PageResponse<>(List.of(dto),null);

        when(cardService.getAll(dto, pageable)).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/cards/data")
                        .param("page", "0")
                        .param("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    void CardCardController_SaveNewCard_Success() throws Exception {
        Card card = new Card(1L, "Test Title", "Test Description", null);

        when(cardService.saveFile(any(CardDTOForAdd.class))).thenReturn(card);

        mockMvc.perform(post("/admin/card/add")
                        .param("id", "1")
                        .param("title", "Test title")
                        .param("description", "Test description"))
                .andExpect(status().isCreated())
                .andExpect(content().string("New card with id " + card.getId() + " was created!"));

        verify(cardService, times(1)).saveFile(any(CardDTOForAdd.class));
    }

    @Test
    void CardCardController_SaveNewCard_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/card/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CardCardController_EditCard_Success() throws Exception {
        Card card = new Card(1L, "Test Title", "Test Description", null);

        when(cardService.saveFile(any(CardDTOForAdd.class))).thenReturn(card);

        mockMvc.perform(post("/admin/card/1/edit")
                        .param("id", "1")
                        .param("title", "Test title")
                        .param("description", "Test description"))
                .andExpect(status().isOk())
                .andExpect(content().string("Card with id: 1 was updated!"));

        verify(cardService, times(1)).saveFile(any(CardDTOForAdd.class));
    }

    @Test
    void CardCardController_EditCard_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/card/1/edit"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void CardCardController_GetCardById() throws Exception {
        CardDTOForAdd dto = new CardDTOForAdd(1L, "Test Title", "Test Description", null,null);
        when(cardService.getByIdForAdd(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/admin/card/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(cardService, times(1)).getByIdForAdd(anyLong());
    }

    @Test
    void CardCardController_DeleteCardById() throws Exception {
        Card card = new Card(1L, "Test Title", "Test Description", null);

        when(cardService.getById(anyLong())).thenReturn(card);

        mockMvc.perform(get("/admin/card/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Card with id 1 was deleted!"));

        verify(cardService, times(1)).getById(anyLong());
        verify(cardService, times(1)).deleteById(anyLong());
    }
}