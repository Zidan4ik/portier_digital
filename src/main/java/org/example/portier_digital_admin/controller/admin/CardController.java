package org.example.portier_digital_admin.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Card;
import org.example.portier_digital_admin.service.CardService;
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
public class CardController {
    private final CardService cardService;

    @GetMapping("/cards")
    public ModelAndView viewCards() {
        return new ModelAndView("admin/cards");
    }

    @GetMapping("/cards/data")
    @ResponseBody
    public ResponseEntity<PageResponse<CardDTOForView>> getCards(
            @ModelAttribute CardDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CardDTOForView> all = cardService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/card/add")
    public ResponseEntity<?> saveNewCard(
            @ModelAttribute @Valid CardDTOForAdd dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Card card = cardService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("New card with id " + card.getId() + " was created!");
    }

    @PostMapping("/card/{id}/edit")
    public ResponseEntity<?> editCard(@PathVariable(name = "id") Long id,
                                         @ModelAttribute @Valid CardDTOForAdd dto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        dto.setId(id);
        Card card = cardService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Card with id: " + card.getId() + " was updated!");
    }

    @GetMapping("/card/{id}")
    @ResponseBody
    public ResponseEntity<CardDTOForAdd> getCardById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(cardService.getByIdForAdd(id), HttpStatus.OK);
    }

    @GetMapping("/card/{id}/delete")
    public ResponseEntity<String> deleteCardById(@PathVariable(name = "id") Long id) {
        Card card = cardService.getById(id);
        cardService.deleteById(card.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Card with id " + id + " was deleted!");
    }
    @ModelAttribute("isActiveCards")
    public boolean toActiveCards(){
        return true;
    }
}
