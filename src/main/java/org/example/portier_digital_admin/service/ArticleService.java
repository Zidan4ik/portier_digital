package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.ArticleResponseForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    List<Article> getAll();

    PageResponse<ArticleResponseForView> getAll(ArticleResponseForView dto, Pageable pageable);

    Article getById(Long id);

    ArticleResponseForView getByIdForView(Long id);

    Article save(Article article);

    void delete(Long id);
}
