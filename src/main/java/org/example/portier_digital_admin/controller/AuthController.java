package org.example.portier_digital_admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.UserDTO;
import org.example.portier_digital_admin.repository.UserRepository;
import org.example.portier_digital_admin.service.UserService;
import org.example.portier_digital_admin.validation.UserValidator;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
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
    private final UserRepository userRepository;

    @GetMapping("/login")
    public ModelAndView viewLogin() {
        return new ModelAndView("auth/login");
    }

    @GetMapping("/")
    public ModelAndView viewDefault() {
        return new ModelAndView("auth/login");
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> registrationForm(@ModelAttribute @Valid UserDTO dto,
                                                    BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {
        userValidator.validate(dto, bindingResult);
        if(userRepository.existsByEmail(dto.getEmail())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("registrationForm", UserDTO.class, BindingResult.class), 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        userService.registration(dto);
        return ResponseEntity.ok(dto);
    }
}
