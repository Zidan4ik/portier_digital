package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    PageResponse<ReviewDTOForView> getAll(ReviewDTOForView dto, Pageable pageable);

    List<ReviewDTOForAdd> getAll();

    Review getById(Long id);

    ReviewDTOForAdd getByIdForAdd(Long id);

    Review save(ReviewDTOForAdd dtoAdd);

    Review saveFile(ReviewDTOForAdd dtoAdd);

    void delete(Long id);
}
