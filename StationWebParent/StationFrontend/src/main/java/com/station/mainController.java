package com.station;

import com.station.category.CategoryService;
import com.station.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class mainController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String homePage(Model model) {

        List<Category> EnabledCategoryList = categoryService.categories();
        model.addAttribute("EnabledCategoryList",EnabledCategoryList);
        return "index";
    }

    @GetMapping("/login")
    public String viewLoginPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null|| authentication instanceof AnonymousAuthenticationToken){
            return "login";
        }
        return "redirect:/products";
    }
}
