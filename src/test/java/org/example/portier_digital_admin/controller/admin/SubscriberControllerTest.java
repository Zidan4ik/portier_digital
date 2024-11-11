package org.example.portier_digital_admin.controller.admin;

import org.example.portier_digital_admin.dto.SubscriberDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.service.SubscriberService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SubscriberControllerTest {
    @InjectMocks
    private SubscriberController articleController;

    @Mock
    private SubscriberService articleService;

    private MockMvc mockMvc;
    private SubscriberDTOForView subscriberDTOForView;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
        subscriberDTOForView = SubscriberDTOForView.builder().id(1L).email("email@gmail.com").build();
    }

    @Test
    void SubscriberCardController_ViewSubscribers() throws Exception {
        mockMvc.perform(get("/admin/subscribers"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/subscribers"));
    }

    @Test
    void SubscriberCardController_GetSubscribers() throws Exception {
        PageResponse<SubscriberDTOForView> pageResponse = new PageResponse<>(List.of(subscriberDTOForView),null);

        when(articleService.getAll(any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/subscribers/data")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(articleService, times(1)).getAll(any(), any());
    }

    @Test
    void SubscriberCardController_DeleteSubscriberById() throws Exception {

        mockMvc.perform(get("/admin/subscriber/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Subscriber with id 1 was deleted!"));

        verify(articleService, times(1)).deleteById(anyLong());
    }
}