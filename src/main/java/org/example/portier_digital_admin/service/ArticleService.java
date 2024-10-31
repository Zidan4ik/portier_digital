package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.ArticleDTOAdd;
import org.example.portier_digital_admin.dto.ArticleResponseForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    List<Article> getAll();

    PageResponse<ArticleResponseForView> getAll(ArticleResponseForView dto, Pageable pageable);

    Article getById(Long id);

    ArticleDTOAdd getByIdForAdd(Long id);

    Article save(ArticleDTOAdd dtoAdd);

    Article saveFile(ArticleDTOAdd dtoAdd);

    void delete(Long id);
}
