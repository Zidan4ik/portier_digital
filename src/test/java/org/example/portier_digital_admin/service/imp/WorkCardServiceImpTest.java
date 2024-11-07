package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.entity.WorkCard;
import org.example.portier_digital_admin.repository.WorkCardRepository;
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
class WorkCardServiceImpTest {
    @Mock
    private WorkCardRepository workCardRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private WorkCardServiceImp workCardService;
    private WorkCard workCard;
    private WorkCardDTOForAdd workCardDTOForAdd;
    private WorkCardDTOForView workCardDTOForView;
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        workCard = new WorkCard(1L, "Title", "/uploads/work-cards/newImage.jpg");
        workCardDTOForAdd = new WorkCardDTOForAdd(1L, "Title", null,
                new MockMultipartFile("image-name1", "image1.html", "text/html", "content".getBytes()));
        workCardDTOForView = new WorkCardDTOForView(1L, "Title");
    }

    @Test
    void WorkCardServiceImp_GetAll_ReturnAllWorkCards() {
        List<WorkCard> skillsList = Collections.singletonList(workCard);
        Mockito.when(workCardRepository.findAll()).thenReturn(skillsList);
        List<WorkCardDTOForAdd> workCards = workCardService.getAll();
        assertNotNull(workCards);
        assertEquals(1, workCards.size(), "Sizes should be match");
        verify(workCardRepository, times(1)).findAll();
    }

    @Test
    void WorkCardServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<WorkCard> reviewPage = new PageImpl<>(Collections.singletonList(workCard));
        when(workCardRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(reviewPage);
        PageResponse<WorkCardDTOForView> response = workCardService.getAll(workCardDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(workCardRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void WorkCardServiceImp_GetById_ReturnWorkCard() {
        when(workCardRepository.findById(ID)).thenReturn(Optional.of(workCard));
        WorkCard result = workCardService.getById(ID);
        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(workCardRepository, times(1)).findById(ID);
    }

    @Test
    void WorkCardServiceImp_GetByIdForAdd_ReturnWorkCardDTOForAdd() {
        when(workCardRepository.findById(ID)).thenReturn(Optional.of(workCard));
        WorkCardDTOForAdd result = workCardService.getByIdForAdd(ID);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(workCardRepository, times(1)).findById(ID);
    }

    @Test
    void WorkCardServiceImp_GetById_ThrowsException() {
        when(workCardRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> workCardService.getById(1L));
    }

    @Test
    void WorkCardServiceImp_Save_ReturnWorkCard() {
        when(workCardRepository.save(any(WorkCard.class))).thenReturn(workCard);
        WorkCard savedWorkCard = workCardService.save(workCardDTOForAdd);
        assertNotNull(savedWorkCard);
        assertEquals(workCard.getId(), savedWorkCard.getId(), "Id's should be match");
        verify(workCardRepository, times(1)).save(any(WorkCard.class));
    }

    @Test
    void WorkCardServiceImp_DeleteById_WhenWorkCardIsPresent() throws IOException {
        when(workCardRepository.findById(ID)).thenReturn(Optional.of(workCard));
        workCardService.deleteById(ID);
        verify(workCardRepository, times(1)).deleteById(ID);
        verify(imageService, times(1)).deleteByPath(anyString());
    }

    @Test
    void WorkCardServiceImp_DeleteById_WhenWorkCardNotFound() {
        when(workCardRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> workCardService.deleteById(1L));
    }

    @Test
    void WorkCardServiceImp_SaveFile_WheWorkCardIsNotExist() {
        when(workCardRepository.findById(workCardDTOForAdd.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> workCardService.saveFile(workCardDTOForAdd));
        verify(workCardRepository, times(1)).findById(workCardDTOForAdd.getId());
    }

    @Test
    void WorkCardServiceImp_DeleteById_WhenImageIsNotFound() {
        workCard.setPathToImage(null);
        when(workCardRepository.findById(ID)).thenReturn(Optional.of(workCard));
        workCardService.deleteById(ID);
        verify(workCardRepository, times(1)).deleteById(ID);
    }

    @Test
    void WorkCardServiceImp_DeleteById_WhenImageIsEmpty() {
        workCard.setPathToImage("");
        when(workCardRepository.findById(ID)).thenReturn(Optional.of(workCard));
        workCardService.deleteById(ID);
        verify(workCardRepository, times(1)).deleteById(ID);
    }

    @Test
    void WorkCardServiceImp_SaveFile() {
        when(workCardRepository.findById(workCardDTOForAdd.getId())).thenReturn(Optional.of(workCard));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(workCardRepository.save(any(WorkCard.class))).thenReturn(workCard);
        WorkCard savedWorkCard = workCardService.saveFile(workCardDTOForAdd);
        assertNotNull(savedWorkCard);
        verify(workCardRepository, times(1)).save(any(WorkCard.class));
        verify(imageService, times(1)).save(any(), eq("/uploads/work-cards/newImage.jpg"));
    }

    @Test
    void WorkCardServiceImp_SaveFile_WhenPathToImageIsNull() {
        workCard.setPathToImage(null);
        when(workCardRepository.findById(workCardDTOForAdd.getId())).thenReturn(Optional.of(workCard));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(workCardRepository.save(any(WorkCard.class))).thenReturn(workCard);
        WorkCard savedWorkCard = workCardService.saveFile(workCardDTOForAdd);
        assertNotNull(savedWorkCard);
        verify(workCardRepository, times(1)).save(any(WorkCard.class));
    }

    @Test
    void WorkCardServiceImp_SaveFile_WhenPathsAreNotEqual() {
        workCard.setPathToImage("path/to/image1");
        workCardDTOForAdd.setPathToImage("path/to/image1");
        when(workCardRepository.findById(workCardDTOForAdd.getId())).thenReturn(Optional.of(workCard));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(workCardRepository.save(any(WorkCard.class))).thenReturn(workCard);
        WorkCard savedWorkCard = workCardService.saveFile(workCardDTOForAdd);
        assertNotNull(savedWorkCard);
        verify(workCardRepository, times(1)).save(any(WorkCard.class));
        verify(imageService, times(1)).save(any(), eq("path/to/image1"));
    }

    @Test
    void WorkCardServiceImp_SaveFile_WhenIdIsNull() {
        when(workCardRepository.save(any(WorkCard.class))).thenReturn(workCard);
        WorkCard savedWorkCard = workCardService.saveFile(new WorkCardDTOForAdd());
        assertNotNull(savedWorkCard);
        verify(workCardRepository, times(1)).save(any(WorkCard.class));
    }
}