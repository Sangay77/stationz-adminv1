package com.station.category;

import com.station.common.entity.Category;
import com.station.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> categories() {
        return categoryRepository.findAllEnabled();
    }

    public Category getCategory(String alias) throws CategoryNotFoundException {

        Category category = categoryRepository.findByAliasEnabled(alias);
        if (category == null) {
            throw new CategoryNotFoundException("Category not found with alias name: " + alias);
        }
        return category;
    }
}
