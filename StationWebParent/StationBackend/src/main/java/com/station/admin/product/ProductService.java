package com.station.admin.product;

import com.station.common.entity.Product;
import com.station.common.entity.User;
import com.station.common.exception.ProductNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> findAllWithSort(String field, String direction, int pageNumber, String keyword) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, 4, sort);

        if (keyword != null) {
            return productRepository.search(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public void delete(Integer id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Could not find Product with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product saveProduct(Product product) throws ProductNotFoundException {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (product.getId() != null) {
            // Update logic
            Product existingProduct = productRepository.findById(product.getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + product.getId()));

            if (product.getName() != null) {
                existingProduct.setName(product.getName());
            }
            if (product.getAlias() != null) {
                existingProduct.setAlias(product.getAlias());
            }
            if (product.getMainImage() != null) {
                existingProduct.setMainImage(product.getMainImage());
            }
            if (product.getCategory() != null) {
                existingProduct.setCategory(product.getCategory());
            }

            if (product.getImages() != null) {
                existingProduct.setImages(product.getImages());
            }

            return productRepository.save(existingProduct);
        } else {
            // New Product creation
            return productRepository.save(product);
        }
    }

    public Product getProductById(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find Product with id: " + id);
        }
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }
        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replace(" ", "_");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replace(" ", "_"));
        }
        product.setUpdatedTime(new Date());
        return productRepository.save(product);
    }


    public String checkUnique(Integer id, String name) {

        boolean isCreatingNew = (id == null || id == 0);
        Product productByName = productRepository.findByName(name);

        if (isCreatingNew) {
            if (productByName != null) return "Duplicate";
        } else {
            if (productByName != null && productByName.getId() != id) {
                return "Duplicate";
            }
        }
        return "OK";
    }


    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }

}


