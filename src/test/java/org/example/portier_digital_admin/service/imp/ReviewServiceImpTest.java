package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.ReviewDTOForAdd;
import org.example.portier_digital_admin.dto.ReviewDTOForView;
import org.example.portier_digital_admin.entity.Review;
import org.example.portier_digital_admin.repository.ReviewRepository;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImpTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ReviewServiceImp reviewService;
    private Review review;
    private ReviewDTOForAdd reviewDTOForAdd;
    private ReviewDTOForView reviewDTOForView;
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        review = new Review(1L, "FirstName", "LastName", "Text", "Position", "/uploads/reviews/newImage.jpg");
        reviewDTOForAdd = new ReviewDTOForAdd(1L, "FirstName", "LastName", "Text", "Position", null,
                new MockMultipartFile("image-name1", "image1.html", "text/html", "content".getBytes()));
        reviewDTOForView = new ReviewDTOForView(1L, "FirstName", "LastName", "Text", "Position");
    }

    @Test
    void ReviewServiceImp_GetAll_ReturnAllReviews() {
        List<Review> reviewsList = Collections.singletonList(review);
        Mockito.when(reviewRepository.findAll()).thenReturn(reviewsList);
        List<ReviewDTOForAdd> reviews = reviewService.getAll();
        assertNotNull(reviews);
        assertEquals(1, reviews.size(), "Sizes should be match");
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void ReviewServiceImp_GetAll_ReturnRageResponse() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Review> reviewPage = new PageImpl<>(Collections.singletonList(review));
        when(reviewRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(reviewPage);
        PageResponse<ReviewDTOForView> response = reviewService.getAll(reviewDTOForView, pageable);
        assertNotNull(response);
        assertEquals(1, response.getMetadata().getTotalElements());
        verify(reviewRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void ReviewServiceImp_GetById_ReturnReview() {
        when(reviewRepository.findById(ID)).thenReturn(Optional.of(review));
        Review result = reviewService.getById(ID);
        assertNotNull(result);
        assertEquals(ID, result.getId());
        verify(reviewRepository, times(1)).findById(ID);
    }

    @Test
    void ReviewServiceImp_GetByIdForAdd_ReturnReviewDTOForAdd() {
        when(reviewRepository.findById(ID)).thenReturn(Optional.of(review));
        ReviewDTOForAdd result = reviewService.getByIdForAdd(ID);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(reviewRepository, times(1)).findById(ID);
    }

    @Test
    void ReviewServiceImp_GetById_ThrowsException() {
        when(reviewRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reviewService.getById(1L));
    }

    @Test
    void ReviewServiceImp_Save_ReturnReview() {
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        Review savedReview = reviewService.save(reviewDTOForAdd);
        assertNotNull(savedReview);
        assertEquals(review.getId(), savedReview.getId(), "Id's should be match");
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void ReviewServiceImp_DeleteById_WhenReviewIsPresent() throws IOException {
        when(reviewRepository.findById(ID)).thenReturn(Optional.of(review));
        reviewService.deleteById(ID);
        verify(reviewRepository, times(1)).deleteById(ID);
        verify(imageService, times(1)).deleteByPath(anyString());
    }

    @Test
    void ReviewServiceImp_DeleteById_WhenReviewNotFound() {
        when(reviewRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reviewService.deleteById(1L));
    }

    @Test
    void ReviewServiceImp_SaveFile_WheReviewIsNotExist() {
        when(reviewRepository.findById(reviewDTOForAdd.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reviewService.saveFile(reviewDTOForAdd));
        verify(reviewRepository, times(1)).findById(reviewDTOForAdd.getId());
    }

    @Test
    void ReviewServiceImp_DeleteById_WhenImageIsNotFound() {
        review.setPathToAvatar(null);
        when(reviewRepository.findById(ID)).thenReturn(Optional.of(review));
        reviewService.deleteById(ID);
        verify(reviewRepository, times(1)).deleteById(ID);
    }

    @Test
    void ReviewServiceImp_DeleteById_WhenImageIsEmpty() {
        review.setPathToAvatar("");
        when(reviewRepository.findById(ID)).thenReturn(Optional.of(review));
        reviewService.deleteById(ID);
        verify(reviewRepository, times(1)).deleteById(ID);
    }

    @Test
    void ReviewServiceImp_SaveFile() {
        when(reviewRepository.findById(reviewDTOForAdd.getId())).thenReturn(Optional.of(review));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        Review savedReview = reviewService.saveFile(reviewDTOForAdd);
        assertNotNull(savedReview);
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(imageService, times(1)).save(any(), eq("/uploads/reviews/newImage.jpg"));
    }

    @Test
    void ReviewServiceImp_SaveFile_WhenPathToImageIsNull() {
        review.setPathToAvatar(null);
        when(reviewRepository.findById(reviewDTOForAdd.getId())).thenReturn(Optional.of(review));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        Review savedReview = reviewService.saveFile(reviewDTOForAdd);
        assertNotNull(savedReview);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void ReviewServiceImp_SaveFile_WhenPathsAreNotEqual() {
        review.setPathToAvatar("path/to/image1");
        reviewDTOForAdd.setPathToImage("path/to/image1");
        when(reviewRepository.findById(reviewDTOForAdd.getId())).thenReturn(Optional.of(review));
        when(imageService.generateFileName(any())).thenReturn("newImage.jpg");
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        Review savedReview = reviewService.saveFile(reviewDTOForAdd);
        assertNotNull(savedReview);
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(imageService, times(1)).save(any(), eq("path/to/image1"));
    }

    @Test
    void ReviewServiceImp_SaveFile_WhenIdIsNull() {
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        Review savedReview = reviewService.saveFile(new ReviewDTOForAdd());
        assertNotNull(savedReview);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }
}