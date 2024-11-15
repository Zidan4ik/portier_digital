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
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${upload.path}")
    private String path;

    @Override
    public List<CardDTOForAdd> getAll() {
        LogUtil.logInfo("Fetching all cards!");
        List<CardDTOForAdd> cards = cardMapper.toDTOForAdd(cardRepository.findAll());
        LogUtil.logInfo("Fetched cards: " + cards.size() + "!");
        return cards;
    }

    @Override
    public PageResponse<CardDTOForView> getAll(CardDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all cards with pagination and filter criteria!");
        Page<Card> page = cardRepository.findAll(getSpecification(dto), pageable);
        List<CardDTOForView> cards = page.map(cardMapper::toDTOForView).toList();
        LogUtil.logInfo("Fetched cards: " + page.getTotalElements() + "!");
        return new PageResponse<>(cards, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()));
    }

    @Override
    public Card getById(Long id) {
        LogUtil.logInfo("Fetched card with ID: " + id);
        Card card = cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card with id = " + id + " not found")
        );
        LogUtil.logInfo("Fetched card with ID: " + id + " - " + card);
        return card;
    }

    @Override
    public CardDTOForAdd getByIdForAdd(Long id) {
        LogUtil.logInfo("Fetched card with ID: " + id);
        CardDTOForAdd cardDTO = cardMapper.toDTOForAdd(getById(id));
        LogUtil.logInfo("Fetched card with ID: " + id + " - " + cardDTO);
        return cardDTO;
    }

    @Override
    public Card save(CardDTOForAdd dtoAdd) {
        LogUtil.logInfo("Saving card!");
        Card card = cardRepository.save(cardMapper.toEntityForAdd(dtoAdd));
        LogUtil.logInfo("Card with id: " + card.getId() + "was saved! - " + card);
        return card;
    }

    @SneakyThrows
    @Override
    public Card saveFile(CardDTOForAdd dtoAdd) {
        LogUtil.logInfo("Saving card with file for ID: " + dtoAdd.getId());
        if (dtoAdd.getId() != null) {
            Card cardById = getById(dtoAdd.getId());
            if (dtoAdd.getFileImage() != null && (cardById.getPathToImage() != null && !cardById.getPathToImage().equals(dtoAdd.getPathToImage()))) {
                LogUtil.logInfo("Deleting old image at path: " + cardById.getPathToImage());
                imageService.deleteByPath(cardById.getPathToImage());
            }
        }

        if (dtoAdd.getFileImage() != null) {
            String generatedPath = path + "/cards/" + imageService.generateFileName(dtoAdd.getFileImage());
            dtoAdd.setPathToImage(generatedPath);
            LogUtil.logInfo("Generated new path for image: " + generatedPath);
        }
        Card card = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), card.getPathToImage());
        LogUtil.logInfo("Saved card with id: " + card.getId() + " - " + card);
        return card;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting card with id: " + id);
        Card card = getById(id);
        if (card.getPathToImage() != null && !card.getPathToImage().isBlank()) {
            LogUtil.logInfo("Deleting image at path: " + card.getPathToImage());
            imageService.deleteByPath(getById(id).getPathToImage());
        }
        cardRepository.deleteById(id);
        LogUtil.logInfo("Deleted card with id: " + id + "!");
    }
}
