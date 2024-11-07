package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Fact;
import org.example.portier_digital_admin.repository.FactRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactServiceImpTest {
    @Mock
    private FactRepository factRepository;
    @InjectMocks
    private FactServiceImp factService;
    private Fact fact;
    private FactDTOForAdd factDTOForAdd;
    private FactDTOForView factDTOForView;
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        fact = new Fact(1L, "Title", "Content");
        factDTOForAdd = new FactDTOForAdd(1L, "Title", "Content");
        factDTOForView = new FactDTOForView(1L, "Title", "Content");
    }

    @Test
    void FactServiceImp_GetAll_ReturnAllFacts() {
        List<Fact> factsList = Collections.singletonList(fact);
        Mockito.when(factRepository.findAll()).thenReturn(factsList);
        List<FactDTOForAdd> facts = factService.getAll();
        assertNotNull(facts);
        assertEquals(1, facts.size(), "Sizes should be match");
        verify(factRepository, times(1)).findAll();
    }

    @Test
    void FactServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Fact> factPage = new PageImpl<>(Collections.singletonList(fact));
        when(factRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(factPage);
        PageResponse<FactDTOForView> response = factService.getAll(factDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(factRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void FactServiceImp_GetById_ReturnFact() {
        when(factRepository.findById(ID)).thenReturn(Optional.of(fact));
        Fact result = factService.getById(ID);
        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(factRepository, times(1)).findById(ID);
    }

    @Test
    void FactServiceImp_GetByIdForAdd_ReturnFactDTOForAdd() {
        when(factRepository.findById(ID)).thenReturn(Optional.of(fact));
        FactDTOForAdd result = factService.getByIdForAdd(ID);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(factRepository, times(1)).findById(ID);
    }

    @Test
    void FactServiceImp_GetById_ThrowsException() {
        when(factRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> factService.getById(1L));
    }

    @Test
    void FactServiceImp_Save_ReturnFact() {
        when(factRepository.save(any(Fact.class))).thenReturn(fact);
        Fact savedFact = factService.save(factDTOForAdd);
        assertNotNull(savedFact);
        assertEquals(fact.getId(), savedFact.getId(), "Id's should be match");
        verify(factRepository, times(1)).save(any(Fact.class));
    }

    @Test
    void FactServiceImp_DeleteById_WhenFactIsPresent() {
        factService.deleteById(ID);
        verify(factRepository, times(1)).deleteById(ID);
    }
}