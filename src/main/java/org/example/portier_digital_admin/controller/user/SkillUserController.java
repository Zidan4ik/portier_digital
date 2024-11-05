package org.example.portier_digital_admin.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.SkillDTOForAdd;
import org.example.portier_digital_admin.repository.SkillRepository;
import org.example.portier_digital_admin.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SkillUserController {
    private final SkillService skillService;

    @GetMapping("/skills-data")
    public ResponseEntity<List<SkillDTOForAdd>> getSkills() {
        return new ResponseEntity<>(skillService.getAll(), HttpStatus.OK);
    }
}
