package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.mapper.WorkCardMapper;
import org.example.portier_digital_admin.repository.WorkCardRepository;
import org.example.portier_digital_admin.service.ImageService;
import org.example.portier_digital_admin.service.WorkCardService;
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${upload.path}")
    private String path;

    @Override
    public PageResponse<WorkCardDTOForView> getAll(WorkCardDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all work cards with pagination and filter criteria!");
        Page<WorkCard> page = workCardRepository.findAll(getSpecification(dto), pageable);
        List<WorkCardDTOForView> workCards = page.map(workCardMapper::toDTOForView).toList();
        LogUtil.logInfo("Fetched work cards: " + page.getTotalElements() + "!");
        return new PageResponse<>(workCards, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<WorkCardDTOForAdd> getAll() {
        LogUtil.logInfo("Fetching all work cards!");
        List<WorkCard> workCards = workCardRepository.findAll();
        LogUtil.logInfo("Fetched work cards: " + workCards.size() + "!");
        return workCardMapper.toDTOAdd(workCards);
    }

    @Override
    public WorkCard getById(Long id) {
        LogUtil.logInfo("Fetched work card with ID: " + id);
        WorkCard workCard = workCardRepository.findById(id).orElseThrow(
                () -> new
                        EntityNotFoundException("Work card with id = " + id + " not found")
        );
        LogUtil.logInfo("Fetched work card with ID: " + id + " - " + workCard);
        return workCard;
    }

    @Override
    public WorkCardDTOForAdd getByIdForAdd(Long id) {
        LogUtil.logInfo("Fetched work card with ID: " + id);
        WorkCardDTOForAdd workCard = workCardMapper.toDTOAdd(getById(id));
        LogUtil.logInfo("Fetched work card with ID: " + id + " - " + workCard);
        return workCard;
    }

    @Override
    public WorkCard save(WorkCardDTOForAdd dtoAdd) {
        LogUtil.logInfo("Saving work card!");
        WorkCard workCard = workCardRepository.save(workCardMapper.toEntityForAdd(dtoAdd));
        LogUtil.logInfo("Work card with id: " + workCard.getId() + "was saved! - " + workCard);
        return workCard;
    }

    @SneakyThrows
    @Override
    public WorkCard saveFile(WorkCardDTOForAdd dtoAdd) {
        LogUtil.logInfo("Saving work card with file for ID: " + dtoAdd.getId());
        if (dtoAdd.getId() != null) {
            WorkCard workCardById = getById(dtoAdd.getId());
            if (workCardById.getPathToImage() != null && !workCardById.getPathToImage().equals(dtoAdd.getPathToImage())) {
                LogUtil.logInfo("Deleting old image at path: " + workCardById.getPathToImage());
                imageService.deleteByPath(workCardById.getPathToImage());
            }
        }
        if (dtoAdd.getFileImage() != null) {
            String generatedPath = path + "/work-cards/" + imageService.generateFileName(dtoAdd.getFileImage());
            dtoAdd.setPathToImage(generatedPath);
            LogUtil.logInfo("Generated new path for image: " + generatedPath);
        }
        WorkCard workCard = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), workCard.getPathToImage());
        LogUtil.logInfo("Saved work card with id: " + workCard.getId() + " - " + workCard);
        return workCard;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting work card with id: " + id);
        WorkCard workCard = getById(id);
        if (workCard.getPathToImage() != null && !workCard.getPathToImage().isBlank()) {
            LogUtil.logInfo("Deleting image at path: " + workCard.getPathToImage());
            imageService.deleteByPath(workCard.getPathToImage());
        }
        workCardRepository.deleteById(id);
        LogUtil.logInfo("Deleted work card with id: " + id + "!");
    }
}
