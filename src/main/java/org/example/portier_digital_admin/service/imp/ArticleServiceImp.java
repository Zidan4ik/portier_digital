package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.portier_digital_admin.dto.ArticleDTOForAdd;
import org.example.portier_digital_admin.dto.ArticleDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.mapper.ArticleMapper;
import org.example.portier_digital_admin.repository.ArticleRepository;
import org.example.portier_digital_admin.service.ArticleService;
import org.example.portier_digital_admin.service.ImageService;
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.ArticleSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class ArticleServiceImp implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ImageService imageService;
    private final ArticleMapper articleMapper = new ArticleMapper();

    @Override
    public PageResponse<ArticleDTOForView> getAll(ArticleDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all articles with pagination and filter criteria!");
        Page<Article> page = articleRepository.findAll(getSpecification(dto), pageable);
        List<ArticleDTOForView> content = page.map(articleMapper::toResponseForArticleData).toList();
        LogUtil.logInfo("Fetched articles: " + page.getTotalElements() + "!");
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public List<ArticleDTOForAdd> getAll() {
        LogUtil.logInfo("Retrieving all articles without pagination!");
        List<ArticleDTOForAdd> articlesDTO = articleMapper.toDTOAdd(articleRepository.findAll());
        LogUtil.logInfo("Fetched articles: " + articlesDTO.size() + "!");
        return articlesDTO;
    }

    @Override
    public Article getById(Long id) {
        LogUtil.logInfo("Fetched article with ID: " + id);
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Article with id = " + id + " not found")
        );
        LogUtil.logInfo("Fetched article with ID: " + id + " - " + article);
        return article;
    }

    @Override
    public ArticleDTOForAdd getByIdForAdd(Long id) {
        LogUtil.logInfo("Fetched article with id: " + id);
        ArticleDTOForAdd articleDTO = articleMapper.toDTOAdd(getById(id));
        LogUtil.logInfo("Fetched article with id: " + id + " - " + articleDTO);
        return articleDTO;
    }

    @Override
    public Article save(ArticleDTOForAdd dtoAdd) {
        LogUtil.logInfo("Saving article!");
        Article article = articleRepository.save(articleMapper.toEntity(dtoAdd));
        LogUtil.logInfo("Article with id: " + article.getId() + "was saved! - " + article);
        return article;
    }

    @SneakyThrows
    @Override
    public Article saveFile(ArticleDTOForAdd dtoAdd) {
        LogUtil.logInfo("Saving article with file for ID: " + dtoAdd.getId());
        if (dtoAdd.getId() != null) {
            Article articleById = getById(dtoAdd.getId());
            if (articleById.getPathToImage() != null && !articleById.getPathToImage().equals(dtoAdd.getPathToImage())) {
                LogUtil.logInfo("Deleting old image at path: " + articleById.getPathToImage());
                imageService.deleteByPath(articleById.getPathToImage());
            }
        }
        if (dtoAdd.getFileImage() != null) {
            String generatedPath = "/uploads/articles/" + imageService.generateFileName(dtoAdd.getFileImage());
            dtoAdd.setPathToImage(generatedPath);
            LogUtil.logInfo("Generated new path for image: " + generatedPath);
        }
        Article article = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), article.getPathToImage());
        LogUtil.logInfo("Saved article with id: " + article.getId() + " - " + article);
        return article;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting article with id: " + id);
        Article article = getById(id);
        if (article.getPathToImage() != null && !article.getPathToImage().isBlank()) {
            LogUtil.logInfo("Deleting image at path: " + article.getPathToImage());
            imageService.deleteByPath(getById(id).getPathToImage());
        }
        articleRepository.deleteById(id);
        LogUtil.logInfo("Deleted article with id: " + id + "!");
    }
}
