package org.example.portier_digital_admin.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.WorkCardDTOForAdd;
import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.repository.WorkCardRepository;
import org.example.portier_digital_admin.service.WorkCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class WorkCardUserController {
    private final WorkCardService cardService;

    @GetMapping("/workCard-data")
    public ResponseEntity<List<WorkCardDTOForAdd>> getWorkCards() {
        return new ResponseEntity<>(cardService.getAll(), HttpStatus.OK);
    }
}
