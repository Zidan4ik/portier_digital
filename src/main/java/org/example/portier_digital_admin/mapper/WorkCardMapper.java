package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Card;
import org.example.portier_digital_admin.entity.WorkCard;

import java.util.List;

public class WorkCardMapper {
    public WorkCardDTOForView toDTOForView(WorkCard workCard) {
        WorkCardDTOForView dto = new WorkCardDTOForView();
        dto.setId(workCard.getId());
        dto.setTitle(workCard.getTitle());
        return dto;
    }

    public WorkCard toEntityForAdd(WorkCardDTOForAdd dtoAdd) {
        WorkCard entity = new WorkCard();
        entity.setId(dtoAdd.getId());
        entity.setTitle(dtoAdd.getTitle());
        entity.setPathToImage(dtoAdd.getPathToImage());
        return entity;
    }

    public WorkCardDTOForAdd toDTOAdd(WorkCard workCard) {
        WorkCardDTOForAdd dto = new WorkCardDTOForAdd();
        dto.setId(workCard.getId());
        dto.setTitle(workCard.getTitle());
        dto.setPathToImage(workCard.getPathToImage());
        return dto;
    }

    public List<WorkCardDTOForAdd> toDTOAdd(List<WorkCard> workCards){
        return workCards.stream()
                .map(this::toDTOAdd)
                .toList();
    }
}
