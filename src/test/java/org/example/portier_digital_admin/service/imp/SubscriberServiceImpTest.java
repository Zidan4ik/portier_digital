package org.example.portier_digital_admin.service.imp;

import org.example.portier_digital_admin.dto.SubscriberDTOForAdd;
import org.example.portier_digital_admin.dto.SubscriberDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Subscriber;
import org.example.portier_digital_admin.repository.SubscriberRepository;
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

@ExtendWith(MockitoExtension.class)
class SubscriberServiceImpTest {
    @Mock
    private SubscriberRepository subscriberRepository;

    @InjectMocks
    private SubscriberServiceImp subscriberService;
    private Subscriber subscriber;
    private SubscriberDTOForAdd subscriberDTOForAdd;
    private SubscriberDTOForView subscriberDTOForView;
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        subscriber = new Subscriber(1L, "title");
        subscriberDTOForAdd = new SubscriberDTOForAdd(1L, "email");
        subscriberDTOForView = SubscriberDTOForView.builder().id(1L).email("email@gmail.com").build();
    }

    @Test
    void SubscriberServiceImp_GetAll_ReturnAllSubscribers() {
        List<Subscriber> subscribersList = Collections.singletonList(subscriber);
        Mockito.when(subscriberRepository.findAll()).thenReturn(subscribersList);
        List<SubscriberDTOForView> subscribers = subscriberService.getAll();
        assertNotNull(subscribers);
        assertEquals(1, subscribers.size(), "Sizes should be match");
        verify(subscriberRepository, times(1)).findAll();
    }

    @Test
    void SubscriberServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Subscriber> subscriberPage = new PageImpl<>(Collections.singletonList(subscriber));
        when(subscriberRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(subscriberPage);
        PageResponse<SubscriberDTOForView> response = subscriberService.getAll(subscriberDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(subscriberRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void SubscriberServiceImp_Save_ReturnSubscriber() {
        when(subscriberRepository.save(any(Subscriber.class))).thenReturn(subscriber);
        Subscriber savedSubscriber = subscriberService.save(subscriberDTOForAdd);
        assertNotNull(savedSubscriber);
        assertEquals(subscriber.getId(), savedSubscriber.getId(), "Id's should be match");
        verify(subscriberRepository, times(1)).save(any(Subscriber.class));
    }

    @Test
    void SubscriberServiceImp_DeleteById() {
        subscriberService.deleteById(ID);
        verify(subscriberRepository, times(1)).deleteById(ID);
    }
}