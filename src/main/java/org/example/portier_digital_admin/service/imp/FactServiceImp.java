package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.FactDTOForAdd;
import org.example.portier_digital_admin.dto.FactDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Fact;
import org.example.portier_digital_admin.mapper.FactMapper;
import org.example.portier_digital_admin.repository.FactRepository;
import org.example.portier_digital_admin.service.FactService;
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.FactSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class FactServiceImp implements FactService {
    private final FactRepository factRepository;
    private final FactMapper factMapper = new FactMapper();

    @Override
    public Fact save(FactDTOForAdd dto) {
        LogUtil.logInfo("Saving fact!");
        Fact fact = factRepository.save(factMapper.toEntityFromAdd(dto));
        LogUtil.logInfo("Fact with id: " + fact.getId() + "was saved! - " + fact);
        return fact;
    }


    @Override
    public Fact getById(Long id) {
        LogUtil.logInfo("Fetched fact with ID: " + id);
        Fact fact = factRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Fact with id = " + id + " was not found!")
        );
        LogUtil.logInfo("Fetched fact with ID: " + id + " - " + fact);
        return fact;
    }

    @Override
    public FactDTOForAdd getByIdForAdd(Long id) {
        LogUtil.logInfo("Fetched fact with ID: " + id);
        FactDTOForAdd factDTO = factMapper.toDTOForAdd(getById(id));
        LogUtil.logInfo("Fetched fact with ID: " + id + " - " + factDTO);
        return factDTO;
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting fact with id: " + id);
        factRepository.deleteById(id);
        LogUtil.logInfo("Deleted fact with id: " + id + "!");
    }

    @Override
    public PageResponse<FactDTOForView> getAll(FactDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all facts with pagination and filter criteria!");
        Page<Fact> page = factRepository.findAll(getSpecification(dto), pageable);
        List<FactDTOForView> content = page.map(factMapper::toDTOForView).toList();
        LogUtil.logInfo("Fetched facts: " + page.getTotalElements() + "!");
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<FactDTOForAdd> getAll() {
        LogUtil.logInfo("Fetching all facts with pagination and filter criteria!");
        List<FactDTOForAdd> factsDTO = factMapper.toDTOForAdd(factRepository.findAll());
        LogUtil.logInfo("Fetched facts: " + factsDTO.size() + "!");
        return factsDTO;
    }
}
