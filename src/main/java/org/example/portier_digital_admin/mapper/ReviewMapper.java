package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.ReviewDTOForAdd;
import org.example.portier_digital_admin.dto.ReviewDTOForView;
import org.example.portier_digital_admin.entity.Review;

import java.util.List;

public class ReviewMapper {
    public ReviewDTOForView toDTOForView(Review review) {
        ReviewDTOForView dto = new ReviewDTOForView();
        dto.setId(review.getId());
        dto.setFirstName(review.getFirstName());
        dto.setLastName(review.getLastName());
        dto.setPosition(review.getPosition());
        dto.setText(review.getText());
        return dto;
    }

    public Review toEntityFromAdd(ReviewDTOForAdd dtoAdd) {
        Review entity = new Review();
        entity.setId(dtoAdd.getId());
        entity.setFirstName(dtoAdd.getFirstName());
        entity.setLastName(dtoAdd.getLastName());
        entity.setPosition(dtoAdd.getPosition());
        entity.setText(dtoAdd.getText());
        entity.setPathToAvatar(dtoAdd.getPathToImage());
        return entity;
    }

    public ReviewDTOForAdd toDTOForAdd(Review review) {
        ReviewDTOForAdd dto = new ReviewDTOForAdd();
        dto.setId(review.getId());
        dto.setFirstName(review.getFirstName());
        dto.setLastName(review.getLastName());
        dto.setPosition(review.getPosition());
        dto.setText(review.getText());
        dto.setPathToImage(review.getPathToAvatar());
        return dto;
    }
    public List<ReviewDTOForAdd> toDTOForAdd(List<Review> reviews){
        return reviews.stream()
                .map(this::toDTOForAdd)
                .toList();
    }
}
