package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.ArticleDTOAdd;
import org.example.portier_digital_admin.dto.ArticleDTOForView;
import org.example.portier_digital_admin.entity.Article;

import java.util.List;

public class ArticleMapper {
    public ArticleDTOForView toResponseForArticleData(Article article) {
        ArticleDTOForView dto = new ArticleDTOForView();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        return dto;
    }

    public Article toEntity(ArticleDTOAdd dtoAdd) {
        Article entity = new Article();
        entity.setId(dtoAdd.getId());
        entity.setTitle(dtoAdd.getTitle());
        entity.setDescription(dtoAdd.getDescription());
        entity.setPathToImage(dtoAdd.getPathToImage());
        return entity;
    }

    public ArticleDTOAdd toDTOAdd(Article article) {
        ArticleDTOAdd dto = new ArticleDTOAdd();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setPathToImage(article.getPathToImage());
        return dto;
    }

    public List<ArticleDTOForView> toResponseForArticles(List<Article> articles) {
        return articles.stream()
                .map(this::toResponseForArticleData)
                .toList();
    }
}
