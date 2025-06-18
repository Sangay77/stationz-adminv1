package com.station.review;

import com.station.common.entity.Product;
import com.station.common.entity.Review;
import com.station.common.exception.ProductNotFoundException;
import com.station.customer.CustomerService;
import com.station.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    // Default first page request
    @GetMapping("/ratings/{productAlias}")
    public String listByProductFirstPage(@PathVariable("productAlias") String productAlias, Model model) {
        return listByProductByPage(model, productAlias, 1, "reviewTime", "desc");
    }

    // Paginated request
    @GetMapping("/ratings/{productAlias}/page/{pageNum}")
    public String listByProductByPage(
            Model model,
            @PathVariable("productAlias") String productAlias,
            @PathVariable("pageNum") int pageNum,
            @RequestParam(name = "sortField", defaultValue = "reviewTime") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {

        try {
            Product product = productService.getProduct(productAlias);
            Page<Review> page = reviewService.listByProduct(product, pageNum, sortField, sortDir);
            List<Review> listReviews = page.getContent();

            long startCount = (long) (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE + 1;
            long endCount = startCount + ReviewService.REVIEWS_PER_PAGE - 1;
            if (endCount > page.getTotalElements()) {
                endCount = page.getTotalElements();
            }

            model.addAttribute("product", product);
            model.addAttribute("listReviews", listReviews);
            model.addAttribute("showRating", true);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

            model.addAttribute("pageTitle", "Reviews for " + product.getShortName());

            return "reviews/reviews_product";

        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }
}
