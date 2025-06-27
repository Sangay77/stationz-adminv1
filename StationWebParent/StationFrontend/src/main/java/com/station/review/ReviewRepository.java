package com.station.review;

import com.station.common.entity.Product;
import com.station.common.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r WHERE r.customer.id=?1")
    Page<Review> findByCustomer(Integer customerId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.customer.id = ?1 AND (" +
            "LOWER(r.headline) LIKE LOWER(CONCAT('%', ?2, '%')) OR " +
            "LOWER(r.comment) LIKE LOWER(CONCAT('%', ?2, '%')) OR " +
            "LOWER(r.product.name) LIKE LOWER(CONCAT('%', ?2, '%')))")
    Page<Review> findByCustomer(Integer customerId, String keyword, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.customer.id=?1 AND r.id=?2")
    Review findByCustomerAndId(Integer customerId, Integer reviewId);

    Page<Review> findByProduct(Product product, Pageable pageable);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.customer.id = ?1 AND r.product.id = ?2")
    boolean existsByCustomerIdAndProductId(Integer customerId, Integer productId);


}
