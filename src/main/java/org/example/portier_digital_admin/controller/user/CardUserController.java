package org.example.portier_digital_admin.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.entity.Card;
import org.example.portier_digital_admin.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CardUserController {
    private final CardService cardService;
    @GetMapping("/cards-data")
    @ResponseBody
    public ResponseEntity<List<Card>> getCards(){
        List<Card> cards = cardService.getAll();
        return new ResponseEntity<>(cards,HttpStatus.OK);
    }
}