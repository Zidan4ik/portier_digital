package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.WorkCard;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface WorkCardService {

    PageResponse<WorkCardDTOForView> getAll(WorkCardDTOForView dto, Pageable pageable);

    List<WorkCardDTOForAdd> getAll();

    WorkCard getById(Long id);

    WorkCardDTOForAdd getByIdForAdd(Long id);

    WorkCard save(WorkCardDTOForAdd dtoAdd);

    WorkCard saveFile(WorkCardDTOForAdd dtoAdd);

    void deleteById(Long id);
}
