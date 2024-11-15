package org.example.portier_digital_admin.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.portier_digital_admin.dto.UserDTO;
import org.example.portier_digital_admin.service.UserService;
import org.example.portier_digital_admin.validation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testViewDefault() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }
    @Test
    public void testViewLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    public void testRegistrationForm_Success() throws Exception {
        userDTO = new UserDTO(1L, "Roma", "roo@gmail.com", "1234", "1234");
        when(userService.existsByEmail(userDTO.getEmail())).thenReturn(false);
        doNothing().when(userService).registration(userDTO);
        mockMvc.perform(post("/registration")
                        .param("id", "1")
                        .param("firstName", "Roma")
                        .param("email", "roo@gmail.com")
                        .param("password", "1234")
                        .param("passwordRepeat", "1234")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("roo@gmail.com"));

        verify(userService, times(1)).registration(userDTO);
    }

    @Test
    public void testRegistrationForm_FailValidation() throws Exception {
        userDTO = new UserDTO(1L, "Roma", "roo@gmail.com", "1234", "1234");
        when(userService.existsByEmail(userDTO.getEmail())).thenReturn(false);
        mockMvc.perform(post("/registration")
                        .param("email", "roo@gmail.com")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegistrationForm_WhenEmailExist() throws Exception {
        userDTO = new UserDTO(1L, "Roma", "roo@gmail.com", "1234", "1234");
        when(userService.existsByEmail(userDTO.getEmail())).thenReturn(true);
        mockMvc.perform(post("/registration")
                        .param("email", "roo@gmail.com"))
                .andExpect(status().isConflict());
    }
}