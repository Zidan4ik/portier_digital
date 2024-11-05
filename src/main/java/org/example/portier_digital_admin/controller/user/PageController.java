package org.example.portier_digital_admin.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
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
    public ModelAndView viewBlog(){
        return new ModelAndView("public/blog");
    }
}
