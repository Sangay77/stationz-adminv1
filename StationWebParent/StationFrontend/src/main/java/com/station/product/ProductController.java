package com.station.product;

import com.station.category.CategoryService;
import com.station.common.entity.Category;
import com.station.common.entity.Product;
import com.station.common.exception.CategoryNotFoundException;
import com.station.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;


    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model
    ) throws CategoryNotFoundException {
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                                     @PathVariable("pageNum") int currentPage,
                                     Model model) throws CategoryNotFoundException {

        Category category = categoryService.getCategory(alias);


        Page<Product> pageProducts = productService.listByCategory(currentPage, category.getId());
        List<Product> page = pageProducts.getContent();

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("listProducts", page);
        model.addAttribute("category", category);

        return "product/products_by_category";
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) throws CategoryNotFoundException {

        try {
            Product product = productService.getProduct(alias);
            Category category = product.getCategory();
            model.addAttribute("category", category);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle",product.getName());

            return "product/product_details";

        } catch (ProductNotFoundException ex) {
            return "error/404";
        }
    }


}
