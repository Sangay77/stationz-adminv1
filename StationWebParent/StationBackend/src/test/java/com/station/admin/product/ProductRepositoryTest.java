package com.station.admin.product;

import com.station.common.entity.Category;
import com.station.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void createProduct(){

        Category category = entityManager.find(Category.class, 1);

        Product newProduct=new Product();
        newProduct.setName("T-Shirt");
        newProduct.setAlias("shirt");
        newProduct.setCreatedTime(new Date());
        newProduct.setShortDescription("Hi this is short");
        newProduct.setFullDescription("Full desc");
        newProduct.setPrice(258);
        newProduct.setCategory(category);

        Product saved=productRepository.save(newProduct);

        assertThat(saved.getId()).isGreaterThan(0);

    }

    @Test
    public void addDetails(){
        Integer productId=18;
        Product product = productRepository.findById(productId).get();
        product.addDetail("Device Memory","128 GB");
        product.addDetail("Device CPU","56 CORE");
        product.addDetail("OS","UBUNTU 10");

        productRepository.save(product);

    }

}