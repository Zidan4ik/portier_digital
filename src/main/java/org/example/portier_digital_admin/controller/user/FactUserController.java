package org.example.portier_digital_admin.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.FactDTOForAdd;
import org.example.portier_digital_admin.repository.FactRepository;
import org.example.portier_digital_admin.service.FactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FactUserController {
    public final FactService factService;

    @GetMapping("/facts-data")
    public ResponseEntity<List<FactDTOForAdd>> getFacts() {
        return new ResponseEntity<>(factService.getAll(), HttpStatus.OK);
    }
}
