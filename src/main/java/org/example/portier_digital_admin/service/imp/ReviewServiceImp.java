package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.ReviewDTOForAdd;
import org.example.portier_digital_admin.dto.ReviewDTOForView;
import org.example.portier_digital_admin.entity.Review;
import org.example.portier_digital_admin.mapper.ReviewMapper;
import org.example.portier_digital_admin.repository.ReviewRepository;
import org.example.portier_digital_admin.service.ImageService;
import org.example.portier_digital_admin.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.ReviewSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class ReviewServiceImp implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ImageService imageService;
    private final ReviewMapper reviewMapper = new ReviewMapper();

    @Override
    public PageResponse<ReviewDTOForView> getAll(ReviewDTOForView dto, Pageable pageable) {
        Page<Review> page = reviewRepository.findAll(getSpecification(dto), pageable);
        List<ReviewDTOForView> content = page.map(reviewMapper::toDTOForView).toList();
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public Review getById(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Review with id = " + id + " was not found!")
        );
    }

    @Override
    public ReviewDTOForAdd getByIdForAdd(Long id) {
        return reviewMapper.toDTOForAdd(getById(id));
    }

    @Override
    public Review save(ReviewDTOForAdd dtoAdd) {
        return reviewRepository.save(reviewMapper.toEntityFromAdd(dtoAdd));
    }

    @SneakyThrows
    @Override
    public Review saveFile(ReviewDTOForAdd dtoAdd) {
        if (dtoAdd.getId() != null) {
            Review reviewById = getById(dtoAdd.getId());
            if (reviewById.getPathToAvatar() != null && !reviewById.getPathToAvatar().equals(dtoAdd.getPathToImage())) {
                imageService.deleteByPath(reviewById.getPathToAvatar());
            }
        }
        if (dtoAdd.getFileImage() != null) {
            dtoAdd.setPathToImage("/uploads/review/" + imageService.generateFileName(dtoAdd.getFileImage()));
        }
        Review review = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), review.getPathToAvatar());
        return review;
    }

    @SneakyThrows
    @Override
    public void delete(Long id) {
        Review review = getById(id);
        if (review.getPathToAvatar() != null && !review.getPathToAvatar().isBlank()) {
            imageService.deleteByPath(review.getPathToAvatar());
        }
        reviewRepository.deleteById(id);
    }
}
