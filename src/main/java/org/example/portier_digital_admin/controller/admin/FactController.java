package org.example.portier_digital_admin.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Fact;
import org.example.portier_digital_admin.entity.Review;
import org.example.portier_digital_admin.service.FactService;
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
public class FactController {
    private final FactService factService;
    @GetMapping("/facts")
    public ModelAndView viewFacts() {
        return new ModelAndView("facts");
    }
    @GetMapping("/facts/data")
    @ResponseBody
    public ResponseEntity<PageResponse<FactDTOForView>> getFact(
            @ModelAttribute FactDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<FactDTOForView> all = factService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/fact/add")
    public ResponseEntity<?> saveNewFact(
            @ModelAttribute @Valid FactDTOForAdd dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Fact review = factService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("New fact with id " + review.getId() + "was created!");
    }

    @PostMapping("/fact/{id}/edit")
    public ResponseEntity<?> editFact(@PathVariable(name = "id") Long id,
                                        @ModelAttribute @Valid FactDTOForAdd dto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        dto.setId(id);
        Fact fact = factService.save(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Fact with id: " + fact.getId() + "was updated!");
    }

    @GetMapping("/fact/{id}")
    @ResponseBody
    public ResponseEntity<FactDTOForAdd> getFactById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(factService.getByIdForAdd(id), HttpStatus.OK);
    }

    @GetMapping("/fact/{id}/delete")
    public ResponseEntity<String> deleteFactById(@PathVariable(name = "id") Long id) {
        factService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Fact with id " + id + " was deleted!");
    }
}
