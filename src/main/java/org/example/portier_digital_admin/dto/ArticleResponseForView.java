package org.example.portier_digital_admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseForView {
    private Long id;
    private String title;
    private String description;
    private String pathToImage;
    private MultipartFile imageFile;
}
