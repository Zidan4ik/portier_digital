package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.entity.Card;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardService {
    List<Card> getAll();

    PageResponse<CardDTOForView> getAll(CardDTOForView dto, Pageable pageable);

    Card getById(Long id);

    CardDTOForAdd getByIdForAdd(Long id);

    Card save(CardDTOForAdd dtoAdd);

    Card saveFile(CardDTOForAdd dtoAdd);

    void delete(Long id);
}
