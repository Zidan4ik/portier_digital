package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.ApplicationDTOForAdd;
import org.example.portier_digital_admin.dto.ApplicationDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Application;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationService {
    Application save(ApplicationDTOForAdd dto);
    List<ApplicationDTOForView> getAll();
    PageResponse<ApplicationDTOForView> getAll(ApplicationDTOForView dto, Pageable pageable);
    void deleteById(Long id);
}
