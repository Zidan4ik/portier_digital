package org.example.portier_digital_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDTOForView {
    private Long id;
    private String company;
    private String position;
}
