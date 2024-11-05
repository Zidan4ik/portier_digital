package org.example.portier_digital_admin.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ExperienceDTOForAdd;
import org.example.portier_digital_admin.service.ExperienceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExperienceUserController {
    private final ExperienceService experienceService;

    @GetMapping("/experiences-data")
    public ResponseEntity<List<ExperienceDTOForAdd>> getExperiences() {
        return new ResponseEntity<>(experienceService.getAll(), HttpStatus.OK);
    }
}
