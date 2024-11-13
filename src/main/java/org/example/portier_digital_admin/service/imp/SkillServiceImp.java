package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.SkillDTOForAdd;
import org.example.portier_digital_admin.dto.SkillDTOForView;
import org.example.portier_digital_admin.entity.Skill;
import org.example.portier_digital_admin.mapper.SkillMapper;
import org.example.portier_digital_admin.repository.SkillRepository;
import org.example.portier_digital_admin.service.ImageService;
import org.example.portier_digital_admin.service.SkillService;
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.SkillSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class SkillServiceImp implements SkillService {
    private final SkillRepository skillRepository;
    private final ImageService imageService;
    private final SkillMapper skillMapper = new SkillMapper();
    @Value("${upload.path}")
    private String path;

    @Override
    public Skill save(SkillDTOForAdd dto) {
        LogUtil.logInfo("Saving skill!");
        Skill skill = skillRepository.save(skillMapper.toEntityFromAdd(dto));
        LogUtil.logInfo("Skill with id: " + skill.getId() + "was saved! - " + skill);
        return skill;
    }

    @SneakyThrows
    @Override
    public Skill saveFile(SkillDTOForAdd dto) {
        LogUtil.logInfo("Saving skill with file for ID: " + dto.getId());
        if (dto.getId() != null) {
            Skill skillById = getById(dto.getId());
            if (skillById.getPathToImage() != null && !skillById.getPathToImage().equals(dto.getPathToImage())) {
                LogUtil.logInfo("Deleting old image at path: " + skillById.getPathToImage());
                imageService.deleteByPath(skillById.getPathToImage());
            }
        }
        if (dto.getFileImage() != null) {
            String generatePath = path + "/skills/" + imageService.generateFileName(dto.getFileImage());
            dto.setPathToImage(generatePath);
            LogUtil.logInfo("Generated new path for image: " + generatePath);
        }
        Skill skill = save(dto);
        imageService.save(dto.getFileImage(), skill.getPathToImage());
        LogUtil.logInfo("Saved skill with id: " + skill.getId() + " - " + skill);
        return skill;
    }

    @Override
    public Skill getById(Long id) {
        LogUtil.logInfo("Fetched skill with ID: " + id);
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Skill with id = " + id + " was not found!"));
        LogUtil.logInfo("Fetched skill with ID: " + id + " - " + skill);
        return skill;
    }

    @Override
    public SkillDTOForAdd getByIdForAdd(Long id) {
        LogUtil.logInfo("Fetched skill with ID: " + id);
        SkillDTOForAdd skillDTO = skillMapper.toDTOForAdd(getById(id));
        LogUtil.logInfo("Fetched skill with ID: " + id + " - " + skillDTO);
        return skillDTO;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting skill with id: " + id);
        Skill skill = getById(id);
        if (skill.getPathToImage() != null && !skill.getPathToImage().isBlank()) {
            LogUtil.logInfo("Deleting image at path: " + skill.getPathToImage());
            imageService.deleteByPath(skill.getPathToImage());
        }
        skillRepository.deleteById(id);
        LogUtil.logInfo("Deleted skill with id: " + id + "!");
    }

    @Override
    public PageResponse<SkillDTOForView> getAll(SkillDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all skills with pagination and filter criteria!");
        Page<Skill> page = skillRepository.findAll(getSpecification(dto), pageable);
        List<SkillDTOForView> content = page.map(skillMapper::toDTOForView).toList();
        LogUtil.logInfo("Fetched skills: " + page.getTotalElements() + "!");
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<SkillDTOForAdd> getAll() {
        LogUtil.logInfo("Fetching all skills!");
        List<Skill> skills = skillRepository.findAll();
        LogUtil.logInfo("Fetched skills: " + skills.size() + "!");
        return skillMapper.toDTOForAdd(skills);
    }
}
