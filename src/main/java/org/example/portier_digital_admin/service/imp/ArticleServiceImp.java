package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ArticleDTOAdd;
import org.example.portier_digital_admin.dto.ArticleResponseForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.mapper.ArticleMapper;
import org.example.portier_digital_admin.repository.ArticleRepository;
import org.example.portier_digital_admin.service.ArticleService;
import org.example.portier_digital_admin.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

import static org.example.portier_digital_admin.service.specifications.ArticleSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class ArticleServiceImp implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ImageService imageService;
    private final ArticleMapper articleMapper = new ArticleMapper();

    @Override
    public List<Article> getAll() {
        return articleRepository.findAll();
    }

    @Override
    public PageResponse<ArticleResponseForView> getAll(ArticleResponseForView dto, Pageable pageable) {
        Page<Article> page = articleRepository.findAll(getSpecification(dto), pageable);
        List<ArticleResponseForView> content = page.map(articleMapper::toResponseForArticleData).toList();
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
    public ArticleDTOAdd getByIdForAdd(Long id) {
        return articleMapper.toDTOAdd(getById(id));
    }

    @Override
    public Article save(ArticleDTOAdd dtoAdd) {
        return articleRepository.save(articleMapper.toEntity(dtoAdd));
    }

    @Override
    public Article saveFile(ArticleDTOAdd dtoAdd) {
        if (dtoAdd.getFileImage() != null) {
            dtoAdd.setPathToImage("./uploads/articles/" + imageService.generateFileName(dtoAdd.getFileImage()));
        }
        Article article = save(dtoAdd);
        imageService.save(dtoAdd.getFileImage(), article.getPathToImage());
        return article;
    }

    @Override
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
