package com.station.product;

import com.station.category.CategoryService;
import com.station.common.entity.Category;
import com.station.common.entity.Customer;
import com.station.common.entity.Product;
import com.station.common.entity.Review;
import com.station.common.exception.CategoryNotFoundException;
import com.station.common.exception.ProductNotFoundException;
import com.station.customer.CustomerService;
import com.station.review.ReviewService;
import com.station.utility.Utility;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private CustomerService customerService;


    @GetMapping("/products")
    public String homePage(Model model) {

        List<Category> EnabledCategoryList = categoryService.categories();
        model.addAttribute("EnabledCategoryList", EnabledCategoryList);
        return "product/products";
    }


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
        model.addAttribute("showRating", true);
        return "product/products_by_category";
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias,
                                    Model model, HttpServletRequest httpServletRequest) throws CategoryNotFoundException {

        try {
            Product product = productService.getProduct(alias);
            Category category = product.getCategory();
            Page<Review> reviews = reviewService.list3MostRecentReviewsByProduct(product);

            Customer customer = getAuthenticatedCustomer(httpServletRequest);
            boolean customerReviewed = reviewService.didCustomerReviewProduct(customer, product.getId());

            if (customerReviewed) {
                model.addAttribute("customerReviewed", customerReviewed);
            } else {
                boolean customerCanReview = reviewService.canCustomerReviewProduct(customer.getId(), product.getId());
                model.addAttribute("customerCanReview", customerCanReview);

            }

            model.addAttribute("category", category);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getName());
            model.addAttribute("showRating", true);
            model.addAttribute("reviews", reviews);
            return "product/product_details";

        } catch (ProductNotFoundException ex) {
            return "error/404";
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest httpServletRequest) {
        String email = Utility.getEMailOfAuthenticatedToken(httpServletRequest);
        return customerService.getCustomerByEmail(email);
    }


}
