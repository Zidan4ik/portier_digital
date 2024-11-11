package org.example.portier_digital_admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.UserDTO;
import org.example.portier_digital_admin.service.UserService;
import org.example.portier_digital_admin.validation.UserValidator;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserValidator userValidator;
    private final UserService userService;
    @GetMapping("/login")
    public ModelAndView viewLogin(){
        return new ModelAndView("auth/login");
    }
    @PostMapping("/registration")
    public ResponseEntity<UserDTO> registrationForm(@ModelAttribute @Valid UserDTO user,
                                                    BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("registrationForm", UserDTO.class, BindingResult.class), 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        userService.registration(user);
        return ResponseEntity.ok(user);
    }
}
