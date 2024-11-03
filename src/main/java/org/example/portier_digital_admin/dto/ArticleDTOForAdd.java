package org.example.portier_digital_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.portier_digital_admin.validation.annotation.ImageExtension;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleDTOForAdd {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    private String title;
    @NotBlank(message = "{error.field.empty}")
    private String description;
    private String pathToImage;
    @ImageExtension
    private MultipartFile fileImage;
}
