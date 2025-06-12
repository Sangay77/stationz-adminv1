package com.station.admin.product;


import com.station.admin.FileUploadUtil;
import com.station.admin.category.CategoryService;
import com.station.common.entity.Category;
import com.station.common.entity.Product;
import com.station.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage(Model model) {

        return getPageWithSort(model, 1, "name", "asc", null);

    }

    @GetMapping("/products/page/{pageNumber}")
    public String getPageWithSort(Model model,
                                  @PathVariable("pageNumber") int currentPage,
                                  @RequestParam(name = "sortField", defaultValue = "name") String sortField,
                                  @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                  @RequestParam(value = "keyword", required = false) String keyword) {

        Page<Product> page = productService.findAllWithSort(sortField, sortDir, currentPage, keyword);
        model.addAttribute("listProducts", page.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);

        return "products/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            String productExtraImagesDir = "product-images/" + id + "/extras";
            String productImagesDir = "product-images/" + id;
            FileUploadUtil.cleanDir(productExtraImagesDir);
            FileUploadUtil.cleanDir(productImagesDir);
            redirectAttributes.addFlashAttribute("message", "The Product ID " + id + "has been deleted successfully");

        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/products";
    }


    @GetMapping("products/new")
    public String newProduct(Model model) {
        List<Category> categoryList = categoryService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);
        model.addAttribute("listCategories", categoryList);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Create New Product");
        return "products/product_form";
    }


    @GetMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) throws ProductNotFoundException {
        try {
            Product productById = productService.getProductById(id);
            List<Category> categoryList = categoryService.listAll();
            model.addAttribute("listCategories", categoryList);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            model.addAttribute("product", productById);
            return "products/product_form";
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/products";
        }

    }

    @GetMapping("/products/details/{id}")
    public String viewProductDetail(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) throws ProductNotFoundException {
        try {
            Product productById = productService.getProductById(id);
            model.addAttribute("product", productById);
            return "products/product_detail_modal";
        } catch (ProductNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/products";
        }

    }


    @PostMapping("products/save")
    public String saveProduct(Product product,
                              @RequestParam("fileImage") MultipartFile mainMultipartFile,
                              @RequestParam(name = "extraImages", required = false) MultipartFile[] extraMultipartFiles,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              RedirectAttributes redirectAttributes) throws IOException, ProductNotFoundException {

        // Handle existing product (update case)
        if (product.getId() != null) {
            Product existingProduct = productService.getProductById(product.getId());

            // Preserve existing images if no new ones are uploaded
            if (mainMultipartFile.isEmpty()) {
                product.setMainImage(existingProduct.getMainImage());
            }
            if (extraMultipartFiles == null || extraMultipartFiles.length == 0) {
                product.setImages(existingProduct.getImages());
            }
        }

        // Process new uploads
        setMainImageName(mainMultipartFile, product);
        setExtraImageNames(extraMultipartFiles, product);
        setProductDetails(detailNames, detailValues, product);

        Product savedProduct = productService.save(product);
        saveUploadedImages(mainMultipartFile, extraMultipartFiles, savedProduct);

        redirectAttributes.addFlashAttribute("message",
                product.getId() != null ? "Product updated successfully" : "Product created successfully");

        return "redirect:/products";

    }

    private void setProductDetails(String[] detailNames, String[] detailValues, Product product) {

        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];

            if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }

    }


//    private void saveUploadedImages(MultipartFile mainMultipartFile,
//                                    MultipartFile[] extraMultipartFiles,
//                                    Product product) throws IOException {
//
//        // Create base directory if not exists
//        String productImageDir = "product-images/" + product.getId();
//        FileUploadUtil.cleanDir(productImageDir);  // Cleans existing directory
//
//        // Save main image
//        if (!mainMultipartFile.isEmpty()) {
//            String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
//            FileUploadUtil.saveFile(productImageDir, fileName, mainMultipartFile);
//        }
//
//        // Save extra images if they exist
//        if (extraMultipartFiles != null && extraMultipartFiles.length > 0) {
//            String extraImageDir = productImageDir + "/extras";
//
//            for (MultipartFile multipartFile : extraMultipartFiles) {
//                if (multipartFile != null && !multipartFile.isEmpty()) {
//                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//                    FileUploadUtil.saveFile(extraImageDir, fileName, multipartFile);
//                }
//            }
//        }
//    }

    private void saveUploadedImages(MultipartFile mainMultipartFile,
                                    MultipartFile[] extraMultipartFiles,
                                    Product product) throws IOException {

        String productImageDir = "product-images/" + product.getId();

        // Only clean if new images are uploaded
        if (!mainMultipartFile.isEmpty()) {
            FileUploadUtil.cleanDir(productImageDir);  // Clean directory only if new main image
        }

        if (!mainMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
            FileUploadUtil.saveFile(productImageDir, fileName, mainMultipartFile);
        }

        if (extraMultipartFiles != null && extraMultipartFiles.length > 0) {
            String extraImageDir = productImageDir + "/extras";

            // Only clean extras dir if new extra images are uploaded
            boolean hasNewExtraImages = false;
            for (MultipartFile multipartFile : extraMultipartFiles) {
                if (multipartFile != null && !multipartFile.isEmpty()) {
                    hasNewExtraImages = true;
                    break;
                }
            }
            if (hasNewExtraImages) {
                FileUploadUtil.cleanDir(extraImageDir);
            }

            for (MultipartFile multipartFile : extraMultipartFiles) {
                if (multipartFile != null && !multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    FileUploadUtil.saveFile(extraImageDir, fileName, multipartFile);
                }
            }
        }
    }


    private void setExtraImageNames(MultipartFile[] extraMultipartFiles, Product product) {
        if (extraMultipartFiles != null && extraMultipartFiles.length > 0) {
            for (MultipartFile multipartFile : extraMultipartFiles) {
                if (multipartFile != null && !multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    product.addExtraImage(fileName);
                }
            }
        }
    }

    private void setMainImageName(MultipartFile mainMultipartFile, Product product) {
        if (mainMultipartFile != null && !mainMultipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateUserEnabledStatus(
            @PathVariable("id") Integer id,
            @PathVariable("status") boolean enabled,
            RedirectAttributes redirectAttributes) {
        productService.updateUserEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";
        String message = String.format("Product ID %d has been %s", id, status);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }
}
