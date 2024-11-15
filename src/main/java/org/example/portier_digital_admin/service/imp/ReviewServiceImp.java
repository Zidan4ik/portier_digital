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
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${upload.path}")
    private String path;

    @Override
    public PageResponse<ReviewDTOForView> getAll(ReviewDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all reviews with pagination and filter criteria!");
        Page<Review> page = reviewRepository.findAll(getSpecification(dto), pageable);
        List<ReviewDTOForView> content = page.map(reviewMapper::toDTOForView).toList();
        LogUtil.logInfo("Fetched reviews: " + page.getTotalElements() + "!");
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<ReviewDTOForAdd> getAll() {
        LogUtil.logInfo("Fetching all reviews!");
        List<ReviewDTOForAdd> reviewsDTO = reviewMapper.toDTOForAdd(reviewRepository.findAll());
        LogUtil.logInfo("Fetched reviews: " + reviewsDTO.size() + "!");
        return reviewsDTO;
    }

    @Override
    public Review getById(Long id) {
        LogUtil.logInfo("Fetched review with ID: " + id);
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Review with id = " + id + " was not found!")
        );
        LogUtil.logInfo("Fetched review with ID: " + id + " - " + review);
        return review;
    }

    @Override
    public ReviewDTOForAdd getByIdForAdd(Long id) {
        LogUtil.logInfo("Fetched review with ID: " + id);
        ReviewDTOForAdd reviewDTO = reviewMapper.toDTOForAdd(getById(id));
        LogUtil.logInfo("Fetched review with ID: " + id + " - " + reviewDTO);
        return reviewDTO;
    }

    @Override
    public Review save(ReviewDTOForAdd dtoAdd) {
        LogUtil.logInfo("Saving review!");
        Review review = reviewRepository.save(reviewMapper.toEntityFromAdd(dtoAdd));
        LogUtil.logInfo("Review with id: " + review.getId() + "was saved! - " + review);
        return review;
    }

    @SneakyThrows
    @Override
    public Review saveFile(ReviewDTOForAdd dtoAdd) {
        LogUtil.logInfo("Saving review with file for ID: " + dtoAdd.getId());
        if (dtoAdd.getId() != null) {
            Review reviewById = getById(dtoAdd.getId());
            if (dtoAdd.getFileImage() != null && (reviewById.getPathToAvatar() != null && !reviewById.getPathToAvatar().equals(dtoAdd.getPathToImage()))) {
                LogUtil.logInfo("Deleting old image at path: " + reviewById.getPathToAvatar());
                imageService.deleteByPath(reviewById.getPathToAvatar());
            }
        }
        if (dtoAdd.getFileImage() != null) {
            String generatedPath = path + "/review/" + imageService.generateFileName(dtoAdd.getFileImage());
            dtoAdd.setPathToImage(generatedPath);
            LogUtil.logInfo("Generated new path for image: " + generatedPath);
        } else {

        }
        Review review = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), review.getPathToAvatar());
        LogUtil.logInfo("Saved review with id: " + review.getId() + " - " + review);
        return review;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting review with id: " + id);
        Review review = getById(id);
        if (review.getPathToAvatar() != null && !review.getPathToAvatar().isBlank()) {
            LogUtil.logInfo("Deleting image at path: " + review.getPathToAvatar());
            imageService.deleteByPath(review.getPathToAvatar());
        }
        reviewRepository.deleteById(id);
        LogUtil.logInfo("Deleted review with id: " + id + "!");
    }
}
