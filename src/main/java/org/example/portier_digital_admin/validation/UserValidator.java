package org.example.portier_digital_admin.validation;

import org.example.portier_digital_admin.dto.UserDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO professor = (UserDTO) target;
        if(!professor.getPassword().equals(professor.getPasswordRepeat()) && !professor.getPassword().isBlank()){
            errors.rejectValue("password", "", getMessage("error.field.password.repeat"));
        }
    }
}
