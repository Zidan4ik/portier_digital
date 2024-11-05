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
        return factRepository.save(factMapper.toEntityFromAdd(dto));
    }


    @Override
    public Fact getById(Long id) {
        return factRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Fact with id = " + id + " was not found!")
        );
    }

    @Override
    public FactDTOForAdd getByIdForAdd(Long id) {
        return factMapper.toDTOForAdd(getById(id));
    }

    @Override
    public void delete(Long id) {
        factRepository.deleteById(id);
    }

    @Override
    public PageResponse<FactDTOForView> getAll(FactDTOForView dto, Pageable pageable) {
        Page<Fact> page = factRepository.findAll(getSpecification(dto), pageable);
        List<FactDTOForView> content = page.map(factMapper::toDTOForView).toList();
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<FactDTOForAdd> getAll() {
        return factMapper.toDTOForAdd(factRepository.findAll());
    }
}
