package org.example.portier_digital_admin.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ReviewDTOForAdd;
import org.example.portier_digital_admin.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewUserController {
    private final ReviewService reviewService;

    @GetMapping("/reviews-data")
    public ResponseEntity<List<ReviewDTOForAdd>> getReviews() {
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }
}
