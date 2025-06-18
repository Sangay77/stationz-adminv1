package com.station.admin.review;

import com.station.admin.FileUploadUtil;
import com.station.common.entity.Product;
import com.station.common.entity.Review;
import com.station.common.exception.ReviewNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    public String listReviewFirstPage(Model model) {
        return getReviewPageWithSort(model, 1, "reviewTime", "asc", null);

    }

    @GetMapping("/reviews/page/{pageNumber}")
    public String getReviewPageWithSort(Model model, @PathVariable("pageNumber") int currentPage, @RequestParam(name = "sortField", defaultValue = "reviewTime") String sortField, @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir, @RequestParam(value = "keyword", required = false) String keyword) {

        Page<Review> page = reviewService.findReviewPageWithSort(sortField, sortDir, currentPage, keyword);
        model.addAttribute("reviews", page.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        return "/review/reviews";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Review ID " + id + " has been deleted successfully.");
        } catch (ReviewNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/reviews";

    }



}
