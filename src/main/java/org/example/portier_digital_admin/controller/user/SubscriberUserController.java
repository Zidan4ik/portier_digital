package org.example.portier_digital_admin.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.SubscriberDTOForAdd;
import org.example.portier_digital_admin.entity.Subscriber;
import org.example.portier_digital_admin.service.SubscriberService;
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
public class SubscriberUserController {
    private final SubscriberService subscriberService;

    @PostMapping("/subscribe")
    public ResponseEntity<Subscriber> saveSubscribe(@ModelAttribute @Valid SubscriberDTOForAdd dto,
                                                    BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethod("saveSubscribe", SubscriberDTOForAdd.class, BindingResult.class), 0);
            throw new MethodArgumentNotValidException(methodParameter, bindingResult);
        }
        Subscriber subscriber = subscriberService.save(dto);
        return new ResponseEntity<>(subscriber, HttpStatus.OK);
    }
}
