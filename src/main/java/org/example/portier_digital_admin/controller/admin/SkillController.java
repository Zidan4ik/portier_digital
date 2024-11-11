package org.example.portier_digital_admin.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.SkillDTOForAdd;
import org.example.portier_digital_admin.dto.SkillDTOForView;
import org.example.portier_digital_admin.entity.Skill;
import org.example.portier_digital_admin.service.SkillService;
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
public class SkillController {
    private final SkillService skillService;

    @GetMapping("/skills")
    public ModelAndView viewSkills() {
        return new ModelAndView("admin/skills");
    }

    @GetMapping("/skills/data")
    @ResponseBody
    public ResponseEntity<PageResponse<SkillDTOForView>> getReview(
            @ModelAttribute SkillDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<SkillDTOForView> all = skillService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/skill/add")
    public ResponseEntity<?> saveNewSkill(
            @ModelAttribute @Valid SkillDTOForAdd dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Skill skill = skillService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("New skill with id " + skill.getId() + " was created!");
    }

    @PostMapping("/skill/{id}/edit")
    public ResponseEntity<?> editSkill(@PathVariable(name = "id") Long id,
                                        @ModelAttribute @Valid SkillDTOForAdd dto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        dto.setId(id);
        Skill skill = skillService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Skill with id: " + skill.getId() + " was updated!");
    }

    @GetMapping("/skill/{id}")
    @ResponseBody
    public ResponseEntity<SkillDTOForAdd> getSkillById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(skillService.getByIdForAdd(id), HttpStatus.OK);
    }

    @GetMapping("/skill/{id}/delete")
    public ResponseEntity<String> deleteSkillById(@PathVariable(name = "id") Long id) {
        skillService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Skill with id " + id + " was deleted!");
    }
    @ModelAttribute("isActiveSkills")
    public boolean toActiveSkills(){
        return true;
    }
}
