package com.station.admin.product;

import com.station.common.entity.Category;
import com.station.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE CONCAT(p.id,' ',p.name,' ',p.alias,' ') LIKE %?1%")
    Page<Product> search(String keyword, Pageable pageable);

    Long countById(Integer id);

    Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled=?2 WHERE p.id=?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);
}
