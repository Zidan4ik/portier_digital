package org.example.portier_digital_admin.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class ExceptionControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ExceptionController exceptionController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(exceptionController).build();
    }

    @Test
    public void testExceptionHandler()  {
        FieldError fieldError = new FieldError("userDTO", "username", "Username is required");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, String>> responseEntity = exceptionController.handleMethodArgumentNotValid(exception);
        responseEntity.getBody();
    }
}