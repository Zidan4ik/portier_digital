package org.example.portier_digital_admin.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.ReviewDTOForAdd;
import org.example.portier_digital_admin.dto.ReviewDTOForView;
import org.example.portier_digital_admin.entity.Review;
import org.example.portier_digital_admin.service.ReviewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/reviews")
    public ModelAndView viewReviews() {
        return new ModelAndView("reviews");
    }
    @GetMapping("/reviews/data")
    @ResponseBody
    public ResponseEntity<PageResponse<ReviewDTOForView>> getReview(
            @ModelAttribute ReviewDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ReviewDTOForView> all = reviewService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/review/add")
    public ResponseEntity<?> saveNewReview(
            @ModelAttribute @Valid ReviewDTOForAdd dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Review review = reviewService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("New review with id " + review.getId() + "was created!");
    }

    @PostMapping("/review/{id}/edit")
    public ResponseEntity<?> editReview(@PathVariable(name = "id") Long id,
                                            @ModelAttribute @Valid ReviewDTOForAdd dto,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        dto.setId(id);
        Review review = reviewService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Review with id: " + review.getId() + "was updated!");
    }

    @GetMapping("/review/{id}")
    @ResponseBody
    public ResponseEntity<ReviewDTOForAdd> getReviewById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(reviewService.getByIdForAdd(id), HttpStatus.OK);
    }

    @GetMapping("/review/{id}/delete")
    public ResponseEntity<String> deleteReviewById(@PathVariable(name = "id") Long id) {
        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Review with id " + id + " was deleted.");
    }
}
