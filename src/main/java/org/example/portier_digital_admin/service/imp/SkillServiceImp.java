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

    @Override
    public Skill save(SkillDTOForAdd dto) {
        return skillRepository.save(skillMapper.toEntityFromAdd(dto));
    }

    @SneakyThrows
    @Override
    public Skill saveFile(SkillDTOForAdd dto) {
        if (dto.getId() != null) {
            Skill skillById = getById(dto.getId());
            if (skillById.getPathToImage() != null && !skillById.getPathToImage().equals(dto.getPathToImage())) {
                imageService.deleteByPath(skillById.getPathToImage());
            }
        }
        if (dto.getFileImage() != null) {
            dto.setPathToImage("/uploads/skills/" + imageService.generateFileName(dto.getFileImage()));
        }
        Skill skill = save(dto);
        imageService.save(dto.getFileImage(), skill.getPathToImage());
        return skill;
    }

    @Override
    public Skill getById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Skill with id = " + id + " was not found!"));
    }

    @Override
    public SkillDTOForAdd getByIdForAdd(Long id) {
        return skillMapper.toDTOForAdd(getById(id));
    }

    @Override
    public void delete(Long id) {
        skillRepository.deleteById(id);
    }

    @Override
    public PageResponse<SkillDTOForView> getAll(SkillDTOForView dto, Pageable pageable) {
        Page<Skill> page = skillRepository.findAll(getSpecification(dto), pageable);
        List<SkillDTOForView> content = page.map(skillMapper::toDTOForView).toList();
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<SkillDTOForAdd> getAll() {
        return skillMapper.toDTOForAdd(skillRepository.findAll());
    }
}
