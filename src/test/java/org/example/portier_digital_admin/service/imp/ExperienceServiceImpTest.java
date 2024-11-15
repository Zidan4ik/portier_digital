package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Experience;
import org.example.portier_digital_admin.entity.Experience;
import org.example.portier_digital_admin.repository.ExperienceRepository;
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
class ExperienceServiceImpTest {
    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ExperienceServiceImp experienceService;
    private Experience experience;
    private ExperienceDTOForAdd experienceDTOForAdd;
    private ExperienceDTOForView experienceDTOForView;
    private static final Long ID = 1L;
    @BeforeEach
    void setUp() {
        experience = new Experience(1L, "Title", "Content", "/uploads/experiences/newImage.jpg");
        experienceDTOForAdd = new ExperienceDTOForAdd(1L, "Title", "Content", null,
                new MockMultipartFile("image-name1", "image1.html", "text/html", "content".getBytes()));
        experienceDTOForView = new ExperienceDTOForView(1L, "Title", "Content");
    }

    @Test
    void ExperienceServiceImp_GetAll_ReturnAllExperiences() {
        List<Experience> experiencesList = Collections.singletonList(experience);
        Mockito.when(experienceRepository.findAll()).thenReturn(experiencesList);
        List<ExperienceDTOForAdd> experiences = experienceService.getAll();
        assertNotNull(experiences);
        assertEquals(1, experiences.size(), "Sizes should be match");
        verify(experienceRepository, times(1)).findAll();
    }

    @Test
    void ExperienceServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Experience> experiencePage = new PageImpl<>(Collections.singletonList(experience));
        when(experienceRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(experiencePage);
        PageResponse<ExperienceDTOForView> response = experienceService.getAll(experienceDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(experienceRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void ExperienceServiceImp_GetById_ReturnExperience() {
        when(experienceRepository.findById(ID)).thenReturn(Optional.of(experience));
        Experience result = experienceService.getById(ID);
        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(experienceRepository, times(1)).findById(ID);
    }

    @Test
    void ExperienceServiceImp_GetByIdForAdd_ReturnExperienceDTOForAdd() {
        when(experienceRepository.findById(ID)).thenReturn(Optional.of(experience));
        ExperienceDTOForAdd result = experienceService.getByIdForAdd(ID);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(experienceRepository, times(1)).findById(ID);
    }

    @Test
    void ExperienceServiceImp_GetById_ThrowsException() {
        when(experienceRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> experienceService.getById(1L));
    }

    @Test
    void ExperienceServiceImp_Save_ReturnExperience() {
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);
        Experience savedExperience = experienceService.save(experienceDTOForAdd);
        assertNotNull(savedExperience);
        assertEquals(experience.getId(), savedExperience.getId(), "Id's should be match");
        verify(experienceRepository, times(1)).save(any(Experience.class));
    }

    @Test
    void ExperienceServiceImp_DeleteById_WhenExperienceIsPresent() throws IOException {
        when(experienceRepository.findById(ID)).thenReturn(Optional.of(experience));
        experienceService.deleteById(ID);
        verify(experienceRepository, times(1)).deleteById(ID);
        verify(imageService, times(1)).deleteByPath(anyString());
    }

    @Test
    void ExperienceServiceImp_DeleteById_WhenExperienceNotFound() {
        when(experienceRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> experienceService.deleteById(1L));
    }

    @Test
    void ExperienceServiceImp_SaveFile_WheExperienceIsNotExist() {
        when(experienceRepository.findById(experienceDTOForAdd.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> experienceService.saveFile(experienceDTOForAdd));
        verify(experienceRepository, times(1)).findById(experienceDTOForAdd.getId());
    }

    @Test
    void ExperienceServiceImp_DeleteById_WhenImageIsNotFound() {
        experience.setPathToImage(null);
        when(experienceRepository.findById(ID)).thenReturn(Optional.of(experience));
        experienceService.deleteById(ID);
        verify(experienceRepository, times(1)).deleteById(ID);
    }

    @Test
    void ExperienceServiceImp_DeleteById_WhenImageIsEmpty() {
        experience.setPathToImage("");
        when(experienceRepository.findById(ID)).thenReturn(Optional.of(experience));
        experienceService.deleteById(ID);
        verify(experienceRepository, times(1)).deleteById(ID);
    }

    @Test
    void ExperienceServiceImp_SaveFile() {
        when(experienceRepository.findById(experienceDTOForAdd.getId())).thenReturn(Optional.of(experience));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);
        Experience savedExperience = experienceService.saveFile(experienceDTOForAdd);
        assertNotNull(savedExperience);
        verify(experienceRepository, times(1)).save(any(Experience.class));
        verify(imageService, times(1)).save(any(), eq("/uploads/experiences/newImage.jpg"));
    }

    @Test
    void ExperienceServiceImp_SaveFile_WhenPathToImageIsNull() {
        experience.setPathToImage(null);
        when(experienceRepository.findById(experienceDTOForAdd.getId())).thenReturn(Optional.of(experience));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);
        Experience savedExperience = experienceService.saveFile(experienceDTOForAdd);
        assertNotNull(savedExperience);
        verify(experienceRepository, times(1)).save(any(Experience.class));
    }

    @Test
    void ExperienceServiceImp_SaveFile_WhenPathsAreNotEqual() {
        experience.setPathToImage("path/to/image1");
        experienceDTOForAdd.setPathToImage("path/to/image1");
        when(experienceRepository.findById(experienceDTOForAdd.getId())).thenReturn(Optional.of(experience));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);
        Experience savedExperience = experienceService.saveFile(experienceDTOForAdd);
        assertNotNull(savedExperience);
        verify(experienceRepository, times(1)).save(any(Experience.class));
        verify(imageService, times(1)).save(any(), eq("path/to/image1"));
    }

    @Test
    void ExperienceServiceImp_SaveFile_WhenIdIsNull() {
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);
        Experience savedExperience = experienceService.saveFile(new ExperienceDTOForAdd());
        assertNotNull(savedExperience);
        verify(experienceRepository, times(1)).save(any(Experience.class));
    }
    @Test
    void ExperienceServiceImp_SaveFile_WhenFileIsNull() throws IOException {
        ExperienceDTOForAdd dto = new ExperienceDTOForAdd(1L,null,null,null,null);
        Experience existingExperience = new Experience(1L,null,"/base/path/experiences/old-image.jpg",null);
        Experience savedExperience = new Experience(1L,null,null,null);

        Mockito.when(experienceRepository.findById(1L)).thenReturn(Optional.of(existingExperience));
        Mockito.when(experienceRepository.save(Mockito.any())).thenReturn(savedExperience);

        experienceService.saveFile(dto);

        Mockito.verify(imageService, Mockito.never()).deleteByPath(Mockito.anyString());
        Mockito.verify(experienceRepository).save(Mockito.any(Experience.class));
    }
}