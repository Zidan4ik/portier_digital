package org.example.portier_digital_admin.service.imp;

import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.ApplicationDTOForAdd;
import org.example.portier_digital_admin.dto.ApplicationDTOForView;
import org.example.portier_digital_admin.entity.Application;
import org.example.portier_digital_admin.repository.ApplicationRepository;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImpTest {
    @Mock
    private ApplicationRepository cardRepository;

    @InjectMocks
    private ApplicationServiceImp cardService;
    private Application card;
    private ApplicationDTOForAdd cardDTOForAdd;
    private ApplicationDTOForView cardDTOForView;
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        card = new Application(1L, "title","email","description");
        cardDTOForAdd = new ApplicationDTOForAdd(1L, "title","email","description");
        cardDTOForView = ApplicationDTOForView.builder().id(1L).email("email@gmail.com").build();
    }

    @Test
    void ApplicationServiceImp_GetAll_ReturnAllApplications() {
        List<Application> cardsList = Collections.singletonList(card);
        Mockito.when(cardRepository.findAll()).thenReturn(cardsList);
        List<ApplicationDTOForView> subscribers = cardService.getAll();
        assertNotNull(subscribers);
        assertEquals(1, subscribers.size(), "Sizes should be match");
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void ApplicationServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Application> cardPage = new PageImpl<>(Collections.singletonList(card));
        when(cardRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(cardPage);
        PageResponse<ApplicationDTOForView> response = cardService.getAll(cardDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(cardRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void ApplicationServiceImp_Save_ReturnApplication() {
        when(cardRepository.save(any(Application.class))).thenReturn(card);
        Application savedApplication = cardService.save(cardDTOForAdd);
        assertNotNull(savedApplication);
        assertEquals(card.getId(), savedApplication.getId(), "Id's should be match");
        verify(cardRepository, times(1)).save(any(Application.class));
    }

    @Test
    void ApplicationServiceImp_DeleteById() {
        cardService.deleteById(ID);
        verify(cardRepository, times(1)).deleteById(ID);
    }
}