package org.example.portier_digital_admin.controller.user;

import org.example.portier_digital_admin.dto.SubscriberDTOForAdd;
import org.example.portier_digital_admin.entity.Subscriber;
import org.example.portier_digital_admin.service.SubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubscribeSubscribeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private SubscriberService applicationService;

    @InjectMocks
    private SubscriberUserController appController;
    private SubscriberDTOForAdd applicationDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appController).build();
    }

    @Test
    public void testSaveForm_Success() throws Exception {
        applicationDTO = new SubscriberDTOForAdd(1L, "roo@gmail.com");
        when(applicationService.save(applicationDTO)).thenReturn(new Subscriber(1L, "roo@gmail.com"));
        mockMvc.perform(post("/user/subscribe")
                        .param("id", "1")
                        .param("email", "roo@gmail.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("roo@gmail.com"));

        verify(applicationService, times(1)).save(applicationDTO);
    }

    @Test
    public void testSaveForm_FailValidation() throws Exception {
        mockMvc.perform(post("/user/subscribe"))
                .andExpect(status().isBadRequest());
    }
}