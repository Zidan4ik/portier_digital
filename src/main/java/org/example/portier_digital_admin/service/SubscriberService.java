package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.SubscriberDTOForAdd;
import org.example.portier_digital_admin.dto.SubscriberDTOForView;
import org.example.portier_digital_admin.entity.Subscriber;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriberService {
    Subscriber save(SubscriberDTOForAdd dto);
    List<SubscriberDTOForView> getAll();
    PageResponse<SubscriberDTOForView> getAll(SubscriberDTOForView dto, Pageable pageable);
    void deleteById(Long id);
}
