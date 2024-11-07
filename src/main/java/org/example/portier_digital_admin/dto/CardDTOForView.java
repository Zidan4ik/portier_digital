package org.example.portier_digital_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTOForView {
    private Long id;
    private String title;
    private String description;
}
