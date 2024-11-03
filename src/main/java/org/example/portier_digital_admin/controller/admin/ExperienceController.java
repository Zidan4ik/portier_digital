package org.example.portier_digital_admin.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.entity.Experience;
import org.example.portier_digital_admin.service.ExperienceService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ExperienceController {
    private final ExperienceService experienceService;
    @GetMapping("/experiences")
    public ModelAndView viewExperiences() {
        return new ModelAndView("experiences");
    }
    @GetMapping("/experiences/data")
    @ResponseBody
    public ResponseEntity<PageResponse<ExperienceDTOForView>> getExperience(
            @ModelAttribute ExperienceDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ExperienceDTOForView> all = experienceService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/experience/add")
    public ResponseEntity<?> saveNewExperience(
            @ModelAttribute @Valid ExperienceDTOForAdd dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Experience experience = experienceService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("New experience with id " + experience.getId() + "was created!");
    }

    @PostMapping("/experience/{id}/edit")
    public ResponseEntity<?> editExperience(@PathVariable(name = "id") Long id,
                                         @ModelAttribute @Valid ExperienceDTOForAdd dto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        dto.setId(id);
        Experience experience = experienceService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Experience with id: " + experience.getId() + "was updated!");
    }

    @GetMapping("/experience/{id}")
    @ResponseBody
    public ResponseEntity<ExperienceDTOForAdd> getExperienceById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(experienceService.getByIdForAdd(id), HttpStatus.OK);
    }

    @GetMapping("/experience/{id}/delete")
    public ResponseEntity<String> deleteExperienceById(@PathVariable(name = "id") Long id) {
        experienceService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Experience with id " + id + " was deleted.");
    }
}
