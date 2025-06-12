package com.station.admin.category;

import com.station.common.entity.Category;
import com.station.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    public static final int CATEGORY_PER_PAGE = 4;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    public void delete(Integer id) throws CategoryNotFoundException {
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new CategoryNotFoundException("Could not find Category with id: " + id);

        }
        categoryRepository.deleteById(id);
    }

    public Category getCategoryById(Integer id) throws CategoryNotFoundException {
        try {
            return categoryRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find Category with id: " + id);
        }
    }

    public Category saveCategory(Category category) throws CategoryNotFoundException {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.getId() != null) {
            // Update logic
            Category existingCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + category.getId()));

            if (category.getName() != null) {
                existingCategory.setName(category.getName());
            }
            if (category.getAlias() != null) {
                existingCategory.setAlias(category.getAlias());
            }
            if (category.getImage() != null) {
                existingCategory.setImage(category.getImage());
            }

            return categoryRepository.save(existingCategory);
        } else {
            // New category creation
            return categoryRepository.save(category);
        }
    }

    public Page<Category> findByPage(int pageNUmber) {
        Pageable pageable = PageRequest.of(pageNUmber - 1, 3);
        return categoryRepository.findAll(pageable);
    }

    public Page<Category> findAllWithSort(String field, String direction, int pageNumber, String keyword) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, 4, sort);

        if (keyword != null) {
            return categoryRepository.search(keyword, pageable);
        }
        return categoryRepository.findAll(pageable);
    }
}
