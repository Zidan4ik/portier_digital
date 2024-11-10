package org.example.portier_digital_admin.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ApplicationDTOForAdd;
import org.example.portier_digital_admin.entity.Application;
import org.example.portier_digital_admin.service.ApplicationService;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class ApplicationUserController {
    private final ApplicationService applicationService;

    @PostMapping("/applications/save")
    public ResponseEntity<Application> saveApplication(@ModelAttribute @Valid ApplicationDTOForAdd dto,
                                                       BindingResult bindingResult) throws MethodArgumentNotValidException, NoSuchMethodException {
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("saveApplication", ApplicationDTOForAdd.class, BindingResult.class), 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        Application application = applicationService.save(dto);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }
}