package org.example.portier_digital_admin.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ArticleDTOForAdd;
import org.example.portier_digital_admin.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class ArticleUserController {
    private final ArticleService articleService;

    @GetMapping("/articles-data")
    public ResponseEntity<List<ArticleDTOForAdd>> getArticles() {
        return new ResponseEntity<>(articleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/article/{id}")
    public ModelAndView showArticle(@PathVariable String id) {
        return new ModelAndView("public/blog");
    }

    @GetMapping("/article/{id}/data")
    public ResponseEntity<ArticleDTOForAdd> getArticle(@PathVariable String id) {
        return new ResponseEntity<>(articleService.getByIdForAdd(Long.parseLong(id)), HttpStatus.OK);
    }
}