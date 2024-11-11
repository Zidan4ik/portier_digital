package org.example.portier_digital_admin.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.SubscriberDTOForAdd;
import org.example.portier_digital_admin.dto.SubscriberDTOForView;
import org.example.portier_digital_admin.entity.Subscriber;
import org.example.portier_digital_admin.mapper.SubscriberMapper;
import org.example.portier_digital_admin.repository.SubscriberRepository;
import org.example.portier_digital_admin.service.SubscriberService;
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.SubscriberSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImp implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SubscriberMapper subscriberMapper = new SubscriberMapper();
    @Override
    public Subscriber save(SubscriberDTOForAdd dto) {
        return subscriberRepository.save(subscriberMapper.toEntityForAdd(dto));
    }

    @Override
    public List<SubscriberDTOForView> getAll() {
        return subscriberMapper.toDTOForView(subscriberRepository.findAll());
    }

    @Override
    public PageResponse<SubscriberDTOForView> getAll(SubscriberDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all subscribers with pagination and filter criteria!");
        Page<Subscriber> page = subscriberRepository.findAll(getSpecification(dto), pageable);
        List<SubscriberDTOForView> content = page.map(subscriberMapper::toDTOForView).toList();
        LogUtil.logInfo("Fetched subscribers: " + page.getTotalElements() + "!");
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting subscriber with id: " + id);
        subscriberRepository.deleteById(id);
        LogUtil.logInfo("Deleted subscriber with id: " + id + "!");
    }
}
