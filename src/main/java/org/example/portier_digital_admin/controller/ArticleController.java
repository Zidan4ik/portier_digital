package org.example.portier_digital_admin.controller;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ArticleResponseForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.mapper.ArticleMapper;
import org.example.portier_digital_admin.service.ArticleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    @GetMapping("/articles")
    public ModelAndView viewArticles() {
        return new ModelAndView("articles");
    }

    @GetMapping("/articles/data")
    @ResponseBody
    public ResponseEntity<PageResponse<ArticleResponseForView>> getArticles(
            @ModelAttribute ArticleResponseForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ArticleResponseForView> all = articleService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
