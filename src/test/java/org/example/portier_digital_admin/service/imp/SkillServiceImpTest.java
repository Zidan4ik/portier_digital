package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Skill;
import org.example.portier_digital_admin.repository.SkillRepository;
import org.example.portier_digital_admin.service.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceImpTest {
    @Mock
    private SkillRepository skillRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private SkillServiceImp skillService;
    private Skill skill;
    private SkillDTOForAdd skillDTOForAdd;
    private SkillDTOForView skillDTOForView;
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        skill = new Skill(1L,"Title", "Description", "/uploads/skills/newImage.jpg");
        skillDTOForAdd = new SkillDTOForAdd(1L, "Title", "Description", null,
                new MockMultipartFile("image-name1", "image1.html", "text/html", "content".getBytes()));
        skillDTOForView = new SkillDTOForView(1L,"Title", "Description");
    }

    @Test
    void SkillServiceImp_GetAll_ReturnAllSkills() {
        List<Skill> skillsList = Collections.singletonList(skill);
        Mockito.when(skillRepository.findAll()).thenReturn(skillsList);
        List<SkillDTOForAdd> skills = skillService.getAll();
        assertNotNull(skills);
        assertEquals(1, skills.size(), "Sizes should be match");
        verify(skillRepository, times(1)).findAll();
    }

    @Test
    void SkillServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Skill> reviewPage = new PageImpl<>(Collections.singletonList(skill));
        when(skillRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(reviewPage);
        PageResponse<SkillDTOForView> response = skillService.getAll(skillDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(skillRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void SkillServiceImp_GetById_ReturnSkill() {
        when(skillRepository.findById(ID)).thenReturn(Optional.of(skill));
        Skill result = skillService.getById(ID);
        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(skillRepository, times(1)).findById(ID);
    }

    @Test
    void SkillServiceImp_GetByIdForAdd_ReturnSkillDTOForAdd() {
        when(skillRepository.findById(ID)).thenReturn(Optional.of(skill));
        SkillDTOForAdd result = skillService.getByIdForAdd(ID);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(skillRepository, times(1)).findById(ID);
    }

    @Test
    void SkillServiceImp_GetById_ThrowsException() {
        when(skillRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> skillService.getById(1L));
    }

    @Test
    void SkillServiceImp_Save_ReturnSkill() {
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        Skill savedSkill = skillService.save(skillDTOForAdd);
        assertNotNull(savedSkill);
        assertEquals(skill.getId(), savedSkill.getId(), "Id's should be match");
        verify(skillRepository, times(1)).save(any(Skill.class));
    }

    @Test
    void SkillServiceImp_DeleteById_WhenSkillIsPresent() throws IOException {
        when(skillRepository.findById(ID)).thenReturn(Optional.of(skill));
        skillService.deleteById(ID);
        verify(skillRepository, times(1)).deleteById(ID);
        verify(imageService, times(1)).deleteByPath(anyString());
    }

    @Test
    void SkillServiceImp_DeleteById_WhenSkillNotFound() {
        when(skillRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> skillService.deleteById(1L));
    }

    @Test
    void SkillServiceImp_SaveFile_WheSkillIsNotExist() {
        when(skillRepository.findById(skillDTOForAdd.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> skillService.saveFile(skillDTOForAdd));
        verify(skillRepository, times(1)).findById(skillDTOForAdd.getId());
    }

    @Test
    void SkillServiceImp_DeleteById_WhenImageIsNotFound() {
        skill.setPathToImage(null);
        when(skillRepository.findById(ID)).thenReturn(Optional.of(skill));
        skillService.deleteById(ID);
        verify(skillRepository, times(1)).deleteById(ID);
    }

    @Test
    void SkillServiceImp_DeleteById_WhenImageIsEmpty() {
        skill.setPathToImage("");
        when(skillRepository.findById(ID)).thenReturn(Optional.of(skill));
        skillService.deleteById(ID);
        verify(skillRepository, times(1)).deleteById(ID);
    }

    @Test
    void SkillServiceImp_SaveFile() {
        when(skillRepository.findById(skillDTOForAdd.getId())).thenReturn(Optional.of(skill));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        Skill savedSkill = skillService.saveFile(skillDTOForAdd);
        assertNotNull(savedSkill);
        verify(skillRepository, times(1)).save(any(Skill.class));
        verify(imageService, times(1)).save(any(), eq("/uploads/skills/newImage.jpg"));
    }

    @Test
    void SkillServiceImp_SaveFile_WhenPathToImageIsNull() {
        skill.setPathToImage(null);
        when(skillRepository.findById(skillDTOForAdd.getId())).thenReturn(Optional.of(skill));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        Skill savedSkill = skillService.saveFile(skillDTOForAdd);
        assertNotNull(savedSkill);
        verify(skillRepository, times(1)).save(any(Skill.class));
    }

    @Test
    void SkillServiceImp_SaveFile_WhenPathsAreNotEqual() {
        skill.setPathToImage("path/to/image1");
        skillDTOForAdd.setPathToImage("path/to/image1");
        when(skillRepository.findById(skillDTOForAdd.getId())).thenReturn(Optional.of(skill));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        Skill savedSkill = skillService.saveFile(skillDTOForAdd);
        assertNotNull(savedSkill);
        verify(skillRepository, times(1)).save(any(Skill.class));
        verify(imageService, times(1)).save(any(), eq("path/to/image1"));
    }

    @Test
    void SkillServiceImp_SaveFile_WhenIdIsNull() {
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        Skill savedSkill = skillService.saveFile(new SkillDTOForAdd());
        assertNotNull(savedSkill);
        verify(skillRepository, times(1)).save(any(Skill.class));
    }
}