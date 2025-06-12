package com.station.product;

import com.station.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled=true AND (p.category.id=?1) ORDER BY p.name ASC")
    Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);

    Product findByAlias(String alias);

    Product findByName(String name);
}
