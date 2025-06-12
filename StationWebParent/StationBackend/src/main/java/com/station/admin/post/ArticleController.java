package com.station.admin.post;

import com.station.common.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles")
    public String getAllArticles(Model model) {
        model.addAttribute("articles", articleService.getAllArticles());
        model.addAttribute("newArticle", new Article()); // For the form
        return "post/article";
    }

    @PostMapping("/article/save")
    public String createArticle(@ModelAttribute("newArticle") Article article) {
        articleService.saveArticle(article);
        return "redirect:/articles"; // Redirect to avoid resubmission
    }

    @GetMapping("/articles/{id}")
    @ResponseBody
    public Article getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping("/article/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "post/edit_article";
    }

}
