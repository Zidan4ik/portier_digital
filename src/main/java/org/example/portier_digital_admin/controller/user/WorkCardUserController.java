package org.example.portier_digital_admin.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.repository.WorkCardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WorkCardUserController {
    private final WorkCardRepository cardRepository;

    @GetMapping("/workCard-data")
    public ResponseEntity<List<WorkCard>> getWorkCards() {
        return new ResponseEntity<>(cardRepository.findAll(), HttpStatus.OK);
    }
}
