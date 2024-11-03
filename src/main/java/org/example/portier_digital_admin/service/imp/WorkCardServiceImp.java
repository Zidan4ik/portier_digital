package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Experience;
import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.mapper.WorkCardMapper;
import org.example.portier_digital_admin.repository.WorkCardRepository;
import org.example.portier_digital_admin.service.ImageService;
import org.example.portier_digital_admin.service.WorkCardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.WorkCardSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class WorkCardServiceImp implements WorkCardService {
    private final WorkCardRepository workCardRepository;
    private final ImageService imageService;
    private final WorkCardMapper workCardMapper = new WorkCardMapper();

    @Override
    public PageResponse<WorkCardDTOForView> getAll(WorkCardDTOForView dto, Pageable pageable) {
        Page<WorkCard> page = workCardRepository.findAll(getSpecification(dto), pageable);
        List<WorkCardDTOForView> workCards = page.map(workCardMapper::toDTOForView).toList();
        return new PageResponse<>(workCards,new PageResponse.Metadata(
                page.getNumber(),page.getSize(),page.getTotalElements(),page.getTotalPages()
        ));
    }

    @Override
    public WorkCard getById(Long id) {
        return workCardRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Work card with id = " + id + " not found")
        );
    }

    @Override
    public WorkCardDTOForAdd getByIdForAdd(Long id) {
        return workCardMapper.toDTOAdd(getById(id));
    }

    @Override
    public WorkCard save(WorkCardDTOForAdd dtoAdd) {
        return workCardRepository.save(workCardMapper.toEntityForAdd(dtoAdd));
    }

    @SneakyThrows
    @Override
    public WorkCard saveFile(WorkCardDTOForAdd dtoAdd) {
        if (dtoAdd.getId() != null) {
            WorkCard workCardById = getById(dtoAdd.getId());
            if (workCardById.getPathToImage() != null && !workCardById.getPathToImage().equals(dtoAdd.getPathToImage())) {
                imageService.deleteByPath(workCardById.getPathToImage());
            }
        }
        if (dtoAdd.getFileImage() != null) {
            dtoAdd.setPathToImage("/uploads/work-cards/" + imageService.generateFileName(dtoAdd.getFileImage()));
        }
        WorkCard experience = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), experience.getPathToImage());
        return experience;
    }

    @SneakyThrows
    @Override
    public void delete(Long id) {
        WorkCard workCard = getById(id);
        if(workCard.getPathToImage() != null && !workCard.getPathToImage().isBlank()){
            imageService.deleteByPath(workCard.getPathToImage());
        }
        workCardRepository.deleteById(id);
    }
}
