package org.example.portier_digital_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    private String firstName;
    @NotBlank(message = "{error.field.empty}")
    private String email;
    @Size(min = 4,max = 30,message = "@{error.field.size.between}")
    private String password;
    private String passwordRepeat;
}
