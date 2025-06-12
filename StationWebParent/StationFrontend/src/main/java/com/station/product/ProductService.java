package com.station.product;

import com.station.common.entity.Product;
import com.station.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE = 5;

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> listByCategory(int pageNum, Integer categoryId) {
        String categoryIdMatch = "_" + String.valueOf(categoryId) + "_";
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
        return productRepository.listByCategory(categoryId, categoryIdMatch, pageable);
    }

    public Product getProduct(String alias) throws ProductNotFoundException {
        Product product = productRepository.findByAlias(alias);
        if (product == null) {
            throw new ProductNotFoundException("Could not find any product with alias: " + alias);
        }
        return product;
    }
}
