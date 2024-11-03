package org.example.portier_digital_admin.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.service.WorkCardService;
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
public class WorkCardController {
    private final WorkCardService workCardService;
    @GetMapping("/work-cards")
    public ModelAndView viewWorkCards() {
            return new ModelAndView("work-cards");
    }

    @GetMapping("/work-cards/data")
    @ResponseBody
    public ResponseEntity<PageResponse<WorkCardDTOForView>> getWorkCards(
            @ModelAttribute WorkCardDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<WorkCardDTOForView> all = workCardService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/work-card/add")
    public ResponseEntity<?> saveNewWorkCard(
            @ModelAttribute @Valid WorkCardDTOForAdd dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        WorkCard workCard = workCardService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("New work card with id " + workCard.getId() + "was created!");
    }

    @PostMapping("/work-card/{id}/edit")
    public ResponseEntity<?> editWorkCard(@PathVariable(name = "id") Long id,
                                      @ModelAttribute @Valid WorkCardDTOForAdd dto,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        dto.setId(id);
        WorkCard workCard = workCardService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Work card with id: " + workCard.getId() + "was updated!");
    }

    @GetMapping("/work-card/{id}")
    @ResponseBody
    public ResponseEntity<WorkCardDTOForAdd> getWorkCardById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(workCardService.getByIdForAdd(id), HttpStatus.OK);
    }

    @GetMapping("/work-card/{id}/delete")
    public ResponseEntity<String> deleteWorkCardById(@PathVariable(name = "id") Long id) {
        WorkCard workCard = workCardService.getById(id);
        workCardService.delete(workCard.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Work card with id " + id + " was deleted.");
    }
}
