package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Skill;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SkillService {
    Skill save(SkillDTOForAdd dto);

    Skill saveFile(SkillDTOForAdd dto);

    Skill getById(Long id);

    SkillDTOForAdd getByIdForAdd(Long id);

    void delete(Long id);

    PageResponse<SkillDTOForView> getAll(SkillDTOForView dto, Pageable pageable);

    List<SkillDTOForAdd> getAll();
}
