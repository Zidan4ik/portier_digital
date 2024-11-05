package org.example.portier_digital_admin.mapper;


import org.example.portier_digital_admin.dto.SkillDTOForAdd;
import org.example.portier_digital_admin.dto.SkillDTOForView;
import org.example.portier_digital_admin.entity.Skill;

import java.util.List;

public class SkillMapper {
    public Skill toEntityFromAdd(SkillDTOForAdd dto) {
        Skill entity = new Skill();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPathToImage(dto.getPathToImage());
        return entity;
    }

    public SkillDTOForAdd toDTOForAdd(Skill entity) {
        SkillDTOForAdd dto = new SkillDTOForAdd();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPathToImage(entity.getPathToImage());
        return dto;
    }

    public SkillDTOForView toDTOForView(Skill entity) {
        SkillDTOForView dto = new SkillDTOForView();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public List<SkillDTOForAdd> toDTOForAdd(List<Skill> facts){
        return facts.stream()
                .map(this::toDTOForAdd)
                .toList();
    }
}
