package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.ArticleDTOAdd;
import org.example.portier_digital_admin.dto.ArticleDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    List<Article> getAll();

    PageResponse<ArticleDTOForView> getAll(ArticleDTOForView dto, Pageable pageable);

    Article getById(Long id);

    ArticleDTOAdd getByIdForAdd(Long id);

    Article save(ArticleDTOAdd dtoAdd);

    Article saveFile(ArticleDTOAdd dtoAdd);

    void delete(Long id);
}
