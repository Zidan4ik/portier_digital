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
        Page<Article> page = articleRepository.findAll(getSpecification(dto), pageable);
        List<ArticleDTOForView> content = page.map(articleMapper::toResponseForArticleData).toList();
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }


    @Override
    public Article getById(Long id) {
        return articleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Article with id = " + id + " not found")
        );
    }

    @Override
    public ArticleDTOForAdd getByIdForAdd(Long id) {
        return articleMapper.toDTOAdd(getById(id));
    }

    @Override
    public Article save(ArticleDTOForAdd dtoAdd) {
        return articleRepository.save(articleMapper.toEntity(dtoAdd));
    }

    @SneakyThrows
    @Override
    public Article saveFile(ArticleDTOForAdd dtoAdd) {
        if (dtoAdd.getId() != null) {
            Article articleById = getById(dtoAdd.getId());
            if (articleById.getPathToImage() != null && !articleById.getPathToImage().equals(dtoAdd.getPathToImage())) {
                imageService.deleteByPath(articleById.getPathToImage());
            }
        }

        if (dtoAdd.getFileImage() != null) {
            dtoAdd.setPathToImage("/uploads/articles/" + imageService.generateFileName(dtoAdd.getFileImage()));
        }
        Article article = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), article.getPathToImage());
        return article;
    }

    @SneakyThrows
    @Override
    public void delete(Long id) {
        Article article = getById(id);
        if (article.getPathToImage() != null && !article.getPathToImage().isBlank()) {
            imageService.deleteByPath(getById(id).getPathToImage());
        }
        articleRepository.deleteById(id);
    }
}
