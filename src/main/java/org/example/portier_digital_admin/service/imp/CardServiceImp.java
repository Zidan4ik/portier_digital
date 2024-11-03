package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.portier_digital_admin.dto.CardDTOForAdd;
import org.example.portier_digital_admin.dto.CardDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Card;
import org.example.portier_digital_admin.mapper.CardMapper;
import org.example.portier_digital_admin.repository.CardRepository;
import org.example.portier_digital_admin.service.CardService;
import org.example.portier_digital_admin.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.CardSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class CardServiceImp implements CardService {
    private final CardRepository cardRepository;
    private final ImageService imageService;
    private final CardMapper cardMapper = new CardMapper();

    @Override
    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    @Override
    public PageResponse<CardDTOForView> getAll(CardDTOForView dto, Pageable pageable) {
        Page<Card> page = cardRepository.findAll(getSpecification(dto), pageable);
        List<CardDTOForView> cards = page.map(cardMapper::toDTOForView).toList();

        return new PageResponse<>(cards, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()));
    }

    @Override
    public Card getById(Long id) {
        return cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card with id = " + id + " not found")
        );
    }

    @Override
    public CardDTOForAdd getByIdForAdd(Long id) {
        return cardMapper.toDTOForAdd(getById(id));
    }

    @SneakyThrows
    @Override
    public Card save(CardDTOForAdd dtoAdd) {
        return cardRepository.save(cardMapper.toEntityForAdd(dtoAdd));
    }

    @SneakyThrows
    @Override
    public Card saveFile(CardDTOForAdd dtoAdd) {
        if (dtoAdd.getId() != null) {
            Card cardById = getById(dtoAdd.getId());
            if (cardById.getPathToImage() != null && !cardById.getPathToImage().equals(dtoAdd.getPathToImage())) {
                imageService.deleteByPath(cardById.getPathToImage());
            }
        }

        if (dtoAdd.getFileImage() != null) {
            dtoAdd.setPathToImage("/uploads/cards/" + imageService.generateFileName(dtoAdd.getFileImage()));
        }
        Card card = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), card.getPathToImage());
        return card;
    }

    @SneakyThrows
    @Override
    public void delete(Long id) {
        Card card = getById(id);
        if(card.getPathToImage() != null && !card.getPathToImage().isBlank()){
            imageService.deleteByPath(getById(id).getPathToImage());
        }
        cardRepository.deleteById(id);
    }
}
