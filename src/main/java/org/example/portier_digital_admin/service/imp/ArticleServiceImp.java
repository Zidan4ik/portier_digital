package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ArticleResponseForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.mapper.ArticleMapper;
import org.example.portier_digital_admin.repository.ArticleRepository;
import org.example.portier_digital_admin.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.ArticleSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class ArticleServiceImp implements ArticleService {
    private final ArticleRepository articleRepository;
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
    public ArticleResponseForView getByIdForView(Long id) {
        return articleMapper.toResponseForArticleData(getById(id));
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
