package com.station.admin.review;

import com.station.admin.product.ProductRepository;
import com.station.common.entity.Review;
import com.station.common.exception.ReviewNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    public Page<Review> findReviewPageWithSort(String field, String direction, int pageNumber, String keyword) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, 4, sort);

        if (keyword != null) {
            return reviewRepository.search(keyword, pageable);
        }
        return reviewRepository.findAll(pageable);
    }

    public void delete(Integer id) throws ReviewNotFoundException {
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Could not find Review with id: " + id);
        }
        reviewRepository.deleteById(id);
    }

    public void save(Review reviewInForm) {
        Review reviewInDB = reviewRepository.findById(reviewInForm.getId()).get();
        reviewInDB.setHeadline(reviewInForm.getHeadline());
        reviewInDB.setComment(reviewInForm.getComment());
        reviewRepository.save(reviewInDB);
        productRepository.updateReviewCount(reviewInDB.getProduct().getId());
        productRepository.updateAverageRating(reviewInDB.getProduct().getId());
    }

    public Review get(Integer id) throws ReviewNotFoundException {
        try {
            return reviewRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ReviewNotFoundException("Could not find review with id " + id);
        }
    }

}
