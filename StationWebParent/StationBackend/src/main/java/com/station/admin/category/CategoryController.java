package com.station.admin.category;

import com.station.admin.FileUploadUtil;
import com.station.common.entity.Category;
import com.station.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String getFirstPage(Model model) {
        return getPageWithSort(model, 1, "name", "asc", null);
    }

    @GetMapping("categories/new")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
    }

    @PostMapping("categories/save")
    public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {

        boolean isUpdating = (category.getId() != null && category.getId() > 0);

        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            if (!multipartFile.isEmpty()) {
                category.setImage(fileName);
            }

            Category savedCategory = categoryService.saveCategory(category);

            if (!multipartFile.isEmpty()) {
                String uploadDir = "category-image/" + savedCategory.getId();
                FileUploadUtil.cleanDir(uploadDir);
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }

            String message = isUpdating ? "Category successfully updated!" : "Category successfully added!";
            redirectAttributes.addFlashAttribute("message", message);

            redirectAttributes.addFlashAttribute("message", message);

        } catch (IOException | RuntimeException | CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", "Error saving category: " + ex.getMessage());
        }

        return "redirect:/categories";
    }


    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The Categiry ID " + id + "has been deleted successfully");

        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String updateCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) throws CategoryNotFoundException {
        try {
            Category categoryById = categoryService.getCategoryById(id);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
            model.addAttribute("category", categoryById);

            return "categories/category_form";
        } catch (CategoryNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }

    }


//    @GetMapping("/sortonly/page/{pageNum}")
//    public String getCategoriesByPage(Model model,
//                                      @PathVariable(name = "pageNum") int currentPage) {
//
//        Page<Category> page = categoryService.findByPage(currentPage);
//        int totalPage = page.getTotalPages();
//        long totalItems = page.getTotalElements();
//        List<Category> listCategories = page.getContent();
//        model.addAttribute("totalPage", totalPage);
//        model.addAttribute("totalItem", totalItems);
//        model.addAttribute("listCategories", listCategories);
//        model.addAttribute("currentPage", currentPage);
//        return "categories/categories";
//    }

    @GetMapping("/categories/page/{pageNumber}")
    public String getPageWithSort(Model model,
                                  @PathVariable("pageNumber") int currentPage,
                                  @RequestParam(name = "sortField", defaultValue = "name") String sortField,
                                  @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                  @RequestParam(value = "keyword", required = false) String keyword) {

        Page<Category> page = categoryService.findAllWithSort(sortField, sortDir, currentPage, keyword);

        // Pagination data
        model.addAttribute("listCategories", page.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        return "categories/categories";

    }

}
