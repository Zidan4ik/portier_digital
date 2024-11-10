package org.example.portier_digital_admin.validation;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.portier_digital_admin.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.validation.*;

import java.util.Locale;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {
    private UserValidator userValidator;

    @Mock
    private MessageSource messageSource;

    @Mock
    private Errors errors;
    private  UserDTO userDTO;
    @BeforeEach
    public void setUp() {
        userValidator = new UserValidator(messageSource);
        userDTO = new UserDTO(1L, "Roma", "roo@gmail.com", "1234", "5678");
    }

    @Test
    public void testSupports() {
        assertFalse(userValidator.supports(UserDTO.class));
    }

    @Test
    public void testValidate_PasswordsMatch() {
        userDTO.setPasswordRepeat("1234");
        userValidator.validate(userDTO, errors);
        verify(errors, never()).rejectValue(eq("password"), anyString(), anyString());
    }

    @Test
    public void testValidate_PasswordsDoNotMatch() {
        String errorMessage = "Паролі не співпадають";
        when(messageSource.getMessage(eq("error.field.password.repeat"), any(), eq(Locale.getDefault()))).thenReturn(errorMessage);
        userValidator.validate(userDTO, errors);
        verify(errors).rejectValue(eq("password"), anyString(), eq(errorMessage));
    }

    @Test
    public void testValidate_BlankPassword() {
        userDTO.setPassword("");
        userValidator.validate(userDTO, errors);
    }
}