package org.example.portier_digital_admin.controller.admin;


import org.example.portier_digital_admin.dto.ReviewDTOForAdd;
import org.example.portier_digital_admin.dto.ReviewDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Review;
import org.example.portier_digital_admin.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {
    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void ReviewCardController_ViewReviews() throws Exception {
        mockMvc.perform(get("/admin/reviews"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reviews"));
    }

    @Test
    void ReviewCardController_GetReviews() throws Exception {
        ReviewDTOForView reviewDTOForView = new ReviewDTOForView();
        PageResponse<ReviewDTOForView> pageResponse = new PageResponse<>(List.of(reviewDTOForView), null);

        when(reviewService.getAll(any(), any())).thenReturn(pageResponse);

        mockMvc.perform(get("/admin/reviews/data")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(reviewService, times(1)).getAll(any(), any());
    }

    @Test
    void ReviewCardController_SaveNewReview_Success() throws Exception {
        Review review = new Review(1L, "Test FirstName", "Test LastName", "Test Text", "Test Position", null);

        when(reviewService.saveFile(any(ReviewDTOForAdd.class))).thenReturn(review);

        mockMvc.perform(post("/admin/review/add")
                        .param("id", "1")
                        .param("firstName", "Roma")
                        .param("lastName", "Pravnyk")
                        .param("position", "Junior Java Developer")
                        .param("text", "I'm 20 year old"))
                .andExpect(status().isCreated())
                .andExpect(content().string("New review with id 1 was created!"));

        verify(reviewService, times(1)).saveFile(any(ReviewDTOForAdd.class));
    }

    @Test
    void ReviewCardController_SaveNewReview_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/review/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ReviewCardController_EditReview_Success() throws Exception {
        Review review = new Review(1L, "Test Title", "Test Description", null, null, null);

        when(reviewService.saveFile(any(ReviewDTOForAdd.class))).thenReturn(review);

        mockMvc.perform(post("/admin/review/1/edit")
                        .param("id", "1")
                        .param("firstName", "Roma")
                        .param("lastName", "Pravnyk")
                        .param("position", "Junior Java Developer")
                        .param("text", "I'm 20 year old"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review with id: 1 was updated!"));

        verify(reviewService, times(1)).saveFile(any(ReviewDTOForAdd.class));
    }

    @Test
    void ReviewCardController_EditReview_BindingErrors() throws Exception {
        mockMvc.perform(post("/admin/review/1/edit"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ReviewCardController_GetReviewById() throws Exception {
        ReviewDTOForAdd dto = new ReviewDTOForAdd(1L, "Test Title", "Test Description", null, null, null, null);
        when(reviewService.getByIdForAdd(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/admin/review/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(reviewService, times(1)).getByIdForAdd(anyLong());
    }

    @Test
    void ReviewCardController_DeleteReviewById() throws Exception {
        mockMvc.perform(get("/admin/review/1/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Review with id 1 was deleted!"));

        verify(reviewService, times(1)).deleteById(anyLong());
    }
}