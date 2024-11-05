package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.ArticleDTOForAdd;
import org.example.portier_digital_admin.dto.ArticleDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    PageResponse<ArticleDTOForView> getAll(ArticleDTOForView dto, Pageable pageable);

    List<ArticleDTOForAdd> getAll();

    Article getById(Long id);

    ArticleDTOForAdd getByIdForAdd(Long id);

    Article save(ArticleDTOForAdd dtoAdd);

    Article saveFile(ArticleDTOForAdd dtoAdd);

    void delete(Long id);
}
