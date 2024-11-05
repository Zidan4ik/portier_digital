package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Fact;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FactService {
    Fact save(FactDTOForAdd dto);

    Fact getById(Long id);

    FactDTOForAdd getByIdForAdd(Long id);

    void delete(Long id);

    PageResponse<FactDTOForView> getAll(FactDTOForView dto, Pageable pageable);

    List<FactDTOForAdd> getAll();
}
