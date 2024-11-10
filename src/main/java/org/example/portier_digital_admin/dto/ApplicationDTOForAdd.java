package org.example.portier_digital_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTOForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    private String name;
    @NotBlank(message = "{error.field.empty}")
    @Pattern(regexp = "([A-z0-9_.-]{1,})@([A-z0-9_.-]{1,}).([A-z]{2,8})",message = "{error.field.email.not.valid}")
    private String email;
    private String desire;
}
