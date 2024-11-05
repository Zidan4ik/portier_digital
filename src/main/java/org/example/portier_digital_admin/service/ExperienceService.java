package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Experience;
import org.springframework.data.domain.Pageable;


public interface ExperienceService {
    Experience save(ExperienceDTOForAdd dto);

    Experience saveFile(ExperienceDTOForAdd dto);

    Experience getById(Long id);

    ExperienceDTOForAdd getByIdForAdd(Long id);

    void delete(Long id);

    PageResponse<ExperienceDTOForView> getAll(ExperienceDTOForView dto, Pageable pageable);
}