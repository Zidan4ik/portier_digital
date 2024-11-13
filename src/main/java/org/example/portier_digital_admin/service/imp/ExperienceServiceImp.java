package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.portier_digital_admin.dto.ExperienceDTOForAdd;
import org.example.portier_digital_admin.dto.ExperienceDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Experience;
import org.example.portier_digital_admin.mapper.ExperienceMapper;
import org.example.portier_digital_admin.repository.ExperienceRepository;
import org.example.portier_digital_admin.service.ExperienceService;
import org.example.portier_digital_admin.service.ImageService;
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.ExperienceSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImp implements ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final ImageService imageService;
    private final ExperienceMapper experienceMapper = new ExperienceMapper();
    @Value("${upload.path}")
    private String path;

    @Override
    public Experience save(ExperienceDTOForAdd dto) {
        LogUtil.logInfo("Saving experience!");
        Experience experience = experienceRepository.save(experienceMapper.toEntityFromAdd(dto));
        LogUtil.logInfo("Experience with id: " + experience.getId() + "was saved! - " + experience);
        return experience;
    }

    @SneakyThrows
    @Override
    public Experience saveFile(ExperienceDTOForAdd dto) {
        LogUtil.logInfo("Saving experience with file for ID: " + dto.getId());
        if (dto.getId() != null) {
            Experience experienceById = getById(dto.getId());
            if (experienceById.getPathToImage() != null && !experienceById.getPathToImage().equals(dto.getPathToImage())) {
                LogUtil.logInfo("Deleting old image at path: " + experienceById.getPathToImage());
                imageService.deleteByPath(experienceById.getPathToImage());
            }
        }
        if (dto.getFileImage() != null) {
            String generatedPath = path + "/experiences/" + imageService.generateFileName(dto.getFileImage());
            dto.setPathToImage(generatedPath);
            LogUtil.logInfo("Generated new path for image: " + generatedPath);
        }
        Experience experience = save(dto);
        imageService.save(dto.getFileImage(), experience.getPathToImage());
        LogUtil.logInfo("Saved experience with id: " + experience.getId() + " - " + experience);
        return experience;
    }

    @Override
    public Experience getById(Long id) {
        LogUtil.logInfo("Fetched experience with ID: " + id);
        Experience experience = experienceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Experience with id = " + id + " not found")
        );
        LogUtil.logInfo("Fetched experience with ID: " + id + " - " + experience);
        return experience;
    }

    @Override
    public ExperienceDTOForAdd getByIdForAdd(Long id) {
        LogUtil.logInfo("Fetched experience with ID: " + id);
        ExperienceDTOForAdd experienceDTO = experienceMapper.toDTOForAdd(getById(id));
        LogUtil.logInfo("Fetched experience with ID: " + id + " - " + experienceDTO);
        return experienceDTO;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting experience with id: " + id);
        Experience experience = getById(id);
        if (experience.getPathToImage() != null && !experience.getPathToImage().isBlank()) {
            LogUtil.logInfo("Deleting image at path: " + experience.getPathToImage());
            imageService.deleteByPath(experience.getPathToImage());
        }
        experienceRepository.deleteById(id);
        LogUtil.logInfo("Deleted experience with id: " + id + "!");
    }

    @Override
    public PageResponse<ExperienceDTOForView> getAll(ExperienceDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all experiences with pagination and filter criteria!");
        Page<Experience> page = experienceRepository.findAll(getSpecification(dto), pageable);
        List<ExperienceDTOForView> content = page.map(experienceMapper::toDTOForView).toList();
        LogUtil.logInfo("Fetched experiences: " + page.getTotalElements() + "!");
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<ExperienceDTOForAdd> getAll() {
        LogUtil.logInfo("Fetching all experiences!");
        List<ExperienceDTOForAdd> experiencesDTO = experienceMapper.toDTOForAdd(experienceRepository.findAll());
        LogUtil.logInfo("Fetched experiences: " + experiencesDTO.size() + "!");
        return experiencesDTO;
    }
}
