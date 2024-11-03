package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.ArticleDTOForAdd;
import org.example.portier_digital_admin.dto.CardDTOForAdd;
import org.example.portier_digital_admin.dto.CardDTOForView;
import org.example.portier_digital_admin.entity.Card;

public class CardMapper {
    public CardDTOForView toDTOForView(Card card) {
        CardDTOForView dto = new CardDTOForView();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        return dto;
    }

    public Card toEntityForAdd(CardDTOForAdd dtoAdd) {
        Card entity = new Card();
        entity.setId(dtoAdd.getId());
        entity.setTitle(dtoAdd.getTitle());
        entity.setDescription(dtoAdd.getDescription());
        entity.setPathToImage(dtoAdd.getPathToImage());
        return entity;
    }

    public CardDTOForAdd toDTOForAdd(Card card) {
        CardDTOForAdd dto = new CardDTOForAdd();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        dto.setPathToImage(card.getPathToImage());
        return dto;
    }
}
