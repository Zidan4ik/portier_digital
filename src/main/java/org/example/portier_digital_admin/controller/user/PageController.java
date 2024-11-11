package org.example.portier_digital_admin.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class PageController {
    @GetMapping("/about")
    public ModelAndView viewAbout() {
        return new ModelAndView("public/about");
    }

    @GetMapping("/blogs")
    public ModelAndView viewBlogs() {
        return new ModelAndView("public/blogs");
    }

    @GetMapping("/home")
    public ModelAndView viewHome() {
        return new ModelAndView("public/home");
    }

    @GetMapping("/portfolio")
    public ModelAndView viewPortfolio() {
        return new ModelAndView("public/portfolio");
    }

    @GetMapping("/blog")
    public ModelAndView viewBlog() {
        return new ModelAndView("public/blog");
    }

    @GetMapping("/default-article/{id}")
    public ModelAndView viewDefaultArticle(@PathVariable(name = "id") Long id) {
        return new ModelAndView("public/default/article" + id);
    }
    @GetMapping("/hire-information")
    public ModelAndView viewHireInformation(){
        return new ModelAndView("public/hire-info");
    }
}
