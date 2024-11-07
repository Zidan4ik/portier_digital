package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.portier_digital_admin.dto.*;
import org.example.portier_digital_admin.entity.Card;
import org.example.portier_digital_admin.repository.CardRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImpTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private CardServiceImp cardService;
    private Card card;
    private CardDTOForAdd cardDTOForAdd;
    private CardDTOForView cardDTOForView;
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        card = new Card(1L, "Title", "Content", "/uploads/cards/newImage.jpg");
        cardDTOForAdd = new CardDTOForAdd(1L, "Title", "Content", null,
                new MockMultipartFile("image-name1", "image1.html", "text/html", "content".getBytes()));
        cardDTOForView = new CardDTOForView(1L, "Title", "Content");
    }

    @Test
    void CardServiceImp_GetAll_ReturnAllCards() {
        List<Card> cardsList = Collections.singletonList(card);
        Mockito.when(cardRepository.findAll()).thenReturn(cardsList);
        List<CardDTOForAdd> cards = cardService.getAll();
        assertNotNull(cards);
        assertEquals(1, cards.size(), "Sizes should be match");
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void CardServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Card> cardPage = new PageImpl<>(Collections.singletonList(card));
        when(cardRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(cardPage);
        PageResponse<CardDTOForView> response = cardService.getAll(cardDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(cardRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void CardServiceImp_GetById_ReturnCard() {
        when(cardRepository.findById(ID)).thenReturn(Optional.of(card));
        Card result = cardService.getById(ID);
        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(cardRepository, times(1)).findById(ID);
    }

    @Test
    void CardServiceImp_GetByIdForAdd_ReturnCardDTOForAdd() {
        when(cardRepository.findById(ID)).thenReturn(Optional.of(card));
        CardDTOForAdd result = cardService.getByIdForAdd(ID);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cardRepository, times(1)).findById(ID);
    }

    @Test
    void CardServiceImp_GetById_ThrowsException() {
        when(cardRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> cardService.getById(1L));
    }

    @Test
    void CardServiceImp_Save_ReturnCard() {
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        Card savedCard = cardService.save(cardDTOForAdd);
        assertNotNull(savedCard);
        assertEquals(card.getId(), savedCard.getId(), "Id's should be match");
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void CardServiceImp_DeleteById_WhenCardIsPresent() throws IOException {
        when(cardRepository.findById(ID)).thenReturn(Optional.of(card));
        cardService.deleteById(ID);
        verify(cardRepository, times(1)).deleteById(ID);
        verify(imageService, times(1)).deleteByPath(anyString());
    }

    @Test
    void CardServiceImp_DeleteById_WhenCardNotFound() {
        when(cardRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> cardService.deleteById(1L));
    }

    @Test
    void CardServiceImp_SaveFile_WheCardIsNotExist() {
        when(cardRepository.findById(cardDTOForAdd.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> cardService.saveFile(cardDTOForAdd));
        verify(cardRepository, times(1)).findById(cardDTOForAdd.getId());
    }

    @Test
    void CardServiceImp_DeleteById_WhenImageIsNotFound() {
        card.setPathToImage(null);
        when(cardRepository.findById(ID)).thenReturn(Optional.of(card));
        cardService.deleteById(ID);
        verify(cardRepository, times(1)).deleteById(ID);
    }

    @Test
    void CardServiceImp_DeleteById_WhenImageIsEmpty() {
        card.setPathToImage("");
        when(cardRepository.findById(ID)).thenReturn(Optional.of(card));
        cardService.deleteById(ID);
        verify(cardRepository, times(1)).deleteById(ID);
    }

    @Test
    void CardServiceImp_SaveFile() {
        when(cardRepository.findById(cardDTOForAdd.getId())).thenReturn(Optional.of(card));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        Card savedCard = cardService.saveFile(cardDTOForAdd);
        assertNotNull(savedCard);
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(imageService, times(1)).save(any(), eq("/uploads/cards/newImage.jpg"));
    }

    @Test
    void CardServiceImp_SaveFile_WhenPathToImageIsNull() {
        card.setPathToImage(null);
        when(cardRepository.findById(cardDTOForAdd.getId())).thenReturn(Optional.of(card));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        Card savedCard = cardService.saveFile(cardDTOForAdd);
        assertNotNull(savedCard);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void CardServiceImp_SaveFile_WhenPathsAreNotEqual() {
        card.setPathToImage("path/to/image1");
        cardDTOForAdd.setPathToImage("path/to/image1");
        when(cardRepository.findById(cardDTOForAdd.getId())).thenReturn(Optional.of(card));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        Card savedCard = cardService.saveFile(cardDTOForAdd);
        assertNotNull(savedCard);
        verify(cardRepository, times(1)).save(any(Card.class));
        verify(imageService, times(1)).save(any(), eq("path/to/image1"));
    }

    @Test
    void CardServiceImp_SaveFile_WhenIdIsNull() {
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        Card savedCard = cardService.saveFile(new CardDTOForAdd());
        assertNotNull(savedCard);
        verify(cardRepository, times(1)).save(any(Card.class));
    }
}