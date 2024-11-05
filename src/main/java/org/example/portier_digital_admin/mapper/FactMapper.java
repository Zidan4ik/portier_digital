package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.FactDTOForAdd;
import org.example.portier_digital_admin.dto.FactDTOForView;
import org.example.portier_digital_admin.entity.Fact;

import java.util.List;

public class FactMapper {
    public Fact toEntityFromAdd(FactDTOForAdd dto) {
        Fact entity = new Fact();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public FactDTOForAdd toDTOForAdd(Fact entity) {
        FactDTOForAdd dto = new FactDTOForAdd();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public FactDTOForView toDTOForView(Fact entity) {
        FactDTOForView dto = new FactDTOForView();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public List<FactDTOForAdd> toDTOForAdd(List<Fact> facts) {
        return facts.stream()
                .map(this::toDTOForAdd)
                .toList();
    }
}
