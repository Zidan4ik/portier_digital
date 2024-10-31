package org.example.portier_digital_admin.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ArticleDTOAdd;
import org.example.portier_digital_admin.dto.ArticleDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Article;
import org.example.portier_digital_admin.service.ArticleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/articles")
    public ModelAndView viewArticles() {
        return new ModelAndView("articles");
    }

    @GetMapping("/articles/data")
    @ResponseBody
    public ResponseEntity<PageResponse<ArticleDTOForView>> getArticles(
            @ModelAttribute ArticleDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ArticleDTOForView> all = articleService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/article/add")
    public ResponseEntity<?> saveNewArticle(
            @ModelAttribute @Valid ArticleDTOAdd dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Article article = articleService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("New article with id " + article.getId() + "was created!");
    }

    @PostMapping("/article/{id}/edit")
    public ResponseEntity<?> editArticle(@PathVariable(name = "id") Long id,
                                         @ModelAttribute @Valid ArticleDTOAdd dto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        dto.setId(id);
        Article article = articleService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Article with id: " + article.getId() + "was updated!");
    }

    @GetMapping("/article/{id}")
    @ResponseBody
    public ResponseEntity<ArticleDTOAdd> getArticleById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(articleService.getByIdForAdd(id), HttpStatus.OK);
    }

    @GetMapping("/article/{id}/delete")
    public ResponseEntity<String> deleteArticleById(@PathVariable(name = "id") Long id) {
        Article article = articleService.getById(id);
        articleService.delete(article.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Article with id " + id + " was deleted.");
    }
}
