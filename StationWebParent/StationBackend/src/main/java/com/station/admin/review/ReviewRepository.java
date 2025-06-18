package com.station.admin.review;

import com.station.common.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE " +
            "LOWER(r.headline) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
            "LOWER(r.comment) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
            "LOWER(r.product.name) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
            "LOWER(CONCAT(r.customer.firstName, ' ', r.customer.lastName)) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<Review> search(String keyword, Pageable pageable);
}
