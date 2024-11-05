package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.ExperienceDTOForAdd;
import org.example.portier_digital_admin.dto.ExperienceDTOForView;
import org.example.portier_digital_admin.entity.Experience;

import java.util.List;

public class ExperienceMapper {
    public Experience toEntityFromAdd(ExperienceDTOForAdd dto) {
        Experience entity = new Experience();
        entity.setId(dto.getId());
        entity.setPosition(dto.getPosition());
        entity.setCompany(dto.getCompany());
        entity.setPathToImage(dto.getPathToImage());
        return entity;
    }

    public ExperienceDTOForAdd toDTOForAdd(Experience entity) {
        ExperienceDTOForAdd dto = new ExperienceDTOForAdd();
        dto.setId(entity.getId());
        dto.setCompany(entity.getCompany());
        dto.setPosition(entity.getPosition());
        dto.setPathToImage(entity.getPathToImage());
        return dto;
    }

    public ExperienceDTOForView toDTOForView(Experience entity) {
        ExperienceDTOForView dto = new ExperienceDTOForView();
        dto.setId(entity.getId());
        dto.setCompany(entity.getCompany());
        dto.setPosition(entity.getPosition());
        return dto;
    }
    public List<ExperienceDTOForAdd> toDTOForAdd(List<Experience> experiences){
        return experiences.stream()
                .map(this::toDTOForAdd)
                .toList();
    }
}
