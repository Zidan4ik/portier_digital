package org.example.portier_digital_admin.controller.user;

import org.example.portier_digital_admin.dto.ApplicationDTOForAdd;
import org.example.portier_digital_admin.entity.Application;
import org.example.portier_digital_admin.service.ApplicationService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ApplicationUserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationUserController appController;
    private ApplicationDTOForAdd applicationDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appController).build();
    }

    @Test
    public void testSaveForm_Success() throws Exception {
        applicationDTO = new ApplicationDTOForAdd(1L, "name", "roo@gmail.com", "desire");
        when(applicationService.save(applicationDTO)).thenReturn(new Application(1L, "name", "roo@gmail.com", "desires"));
        mockMvc.perform(post("/user/applications/save")
                        .param("id", "1")
                        .param("name", "name")
                        .param("email", "roo@gmail.com")
                        .param("desire", "desire")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("roo@gmail.com"));

        verify(applicationService, times(1)).save(applicationDTO);
    }

    @Test
    public void testSaveForm_FailValidation() throws Exception {
        mockMvc.perform(post("/user/applications/save"))
                .andExpect(status().isBadRequest());
    }
}