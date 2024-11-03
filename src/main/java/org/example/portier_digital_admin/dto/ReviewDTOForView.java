package org.example.portier_digital_admin.dto;

import lombok.Data;

@Data
public class ReviewDTOForView {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private String text;
}
