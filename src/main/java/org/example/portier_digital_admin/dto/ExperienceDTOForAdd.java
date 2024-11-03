package org.example.portier_digital_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.portier_digital_admin.validation.annotation.ImageExtension;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ExperienceDTOForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    private String company;
    @NotBlank(message = "{error.field.empty}")
    private String position;
    private String pathToImage;
    @ImageExtension
    private MultipartFile fileImage;
}
