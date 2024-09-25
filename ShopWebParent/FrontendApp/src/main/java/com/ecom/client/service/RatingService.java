package com.ecom.client.service;

import com.ecom.client.repository.RatingRepository;
import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Product;
import com.ecom.common.entity.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    public boolean ratedByCustomerAndProduct(int customerId, int productId) {
        long count = ratingRepository.countByCustomerIdAndProductId(customerId, productId);
        return count > 0;
    }

    public void saveRating(Rating rating){
        ratingRepository.save(rating);
    }

    public List<Rating> getListRatingByProduct(Product product){
        return ratingRepository.findByProduct(product);
    }

    public Rating getRatingByCustomerAndProduct(Customer customer, Product product){
        return ratingRepository.findByCustomerIdAndProductId(customer.getId(), product.getId());
    }
}
