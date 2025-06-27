package com.station.review;

import com.station.common.entity.Customer;
import com.station.common.entity.Product;
import com.station.common.entity.Review;
import com.station.common.exception.ReviewNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    public static final int REVIEWS_PER_PAGE = 3;

    @Autowired
    private ReviewRepository reviewRepository;

    public Page<Review> listByCustomerByPage(Customer customer, String keyword, int pageNum,
                                             String sortField, String sortDir) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        PageRequest pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);

        if (keyword != null) {
            return reviewRepository.findByCustomer(customer.getId(), keyword, pageable);
        }
        return reviewRepository.findByCustomer(customer.getId(), pageable);
    }

    public Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException {
        Review review = reviewRepository.findByCustomerAndId(customer.getId(), reviewId);

        if (review == null)
            throw new ReviewNotFoundException("Customer does not have any reviews with ID " + reviewId);
        return review;
    }

    public Page<Review> list3MostRecentReviewsByProduct(Product product) {
        Sort sort = Sort.by("reviewTime").descending();
        PageRequest pageable = PageRequest.of(0, 3, sort);
        return reviewRepository.findByProduct(product, pageable);
    }

    public Page<Review> listByProduct(Product product, int pageNum, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        PageRequest pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);
        return reviewRepository.findByProduct(product, pageable);
    }


    public boolean didCustomerReviewProduct(Customer customer, Integer productId) {
        return reviewRepository.existsByCustomerIdAndProductId(customer.getId(), productId);
    }

    public boolean canCustomerReviewProduct(Integer customerId, Integer productId) {
        return !reviewRepository.existsByCustomerIdAndProductId(customerId, productId);
    }

}
