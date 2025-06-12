package com.station.admin.dashboard;

import com.station.admin.category.CategoryRepository;
import com.station.admin.customer.CustomerRepository;
import com.station.admin.product.ProductRepository;
import com.station.admin.product.ProductService;
import com.station.common.entity.Category;
import com.station.common.entity.Customer;
import com.station.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashBoardService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
