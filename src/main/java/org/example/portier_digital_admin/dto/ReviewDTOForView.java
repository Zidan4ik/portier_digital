package org.example.portier_digital_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTOForView {
    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private String text;
}
