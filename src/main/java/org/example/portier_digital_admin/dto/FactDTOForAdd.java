package org.example.portier_digital_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactDTOForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    private String title;
    @NotBlank(message = "{error.field.empty}")
    private String description;
}
