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

    @Override
    public Experience save(ExperienceDTOForAdd dto) {
        return experienceRepository.save(experienceMapper.toEntityFromAdd(dto));
    }

    @SneakyThrows
    @Override
    public Experience saveFile(ExperienceDTOForAdd dto) {
        if (dto.getId() != null) {
            Experience experienceById = getById(dto.getId());
            if (experienceById.getPathToImage() != null && !experienceById.getPathToImage().equals(dto.getPathToImage())) {
                imageService.deleteByPath(experienceById.getPathToImage());
            }
        }
        if (dto.getFileImage() != null) {
            dto.setPathToImage("/uploads/experiences/" + imageService.generateFileName(dto.getFileImage()));
        }
        Experience experience = save(dto);
        imageService.save(dto.getFileImage(), experience.getPathToImage());
        return experience;
    }

    @Override
    public Experience getById(Long id) {
        return experienceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Experience with id = " + id + " not found")
        );
    }

    @Override
    public ExperienceDTOForAdd getByIdForAdd(Long id) {
        return experienceMapper.toDTOForAdd(getById(id));
    }

    @SneakyThrows
    @Override
    public void delete(Long id) {
        Experience experience = getById(id);
        if(experience.getPathToImage() != null && !experience.getPathToImage().isBlank()){
            imageService.deleteByPath(experience.getPathToImage());
        }
        experienceRepository.deleteById(id);
    }

    @Override
    public PageResponse<ExperienceDTOForView> getAll(ExperienceDTOForView dto, Pageable pageable) {
        Page<Experience> page = experienceRepository.findAll(getSpecification(dto), pageable);
        List<ExperienceDTOForView> content = page.map(experienceMapper::toDTOForView).toList();
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<ExperienceDTOForAdd> getAll() {
        return experienceMapper.toDTOForAdd(experienceRepository.findAll());
    }
}
