package com.ecom.client.rating;

import com.ecom.client.repository.ProductRepository;
import com.ecom.client.repository.RatingRepository;
import com.ecom.client.service.RatingService;
import com.ecom.common.entity.Customer;
import com.ecom.common.entity.Product;
import com.ecom.common.entity.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RatingRepositoryTest {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateRating(){
        Rating rating = new Rating();
        Customer customer = entityManager.find(Customer.class, 4);
        Product product = entityManager.find(Product.class, 23);

        rating.setCustomer(customer);
        rating.setProduct(product);
        rating.setComment("Sản phẩm dùng khá ổn!");
        rating.setCreateTime(new Date());
        rating.setRating(4);
        ratingRepository.save(rating);
//        productRepository.updateCountAndAverageRating(product.getId());
        System.out.println(rating);
    }

    @Test
    public void testCheckProductRated(){
        Customer customer = entityManager.find(Customer.class, 3);
        Product product = entityManager.find(Product.class, 19);

        long check = ratingRepository.countByCustomerIdAndProductId(customer.getId(), product.getId());
        System.out.println(check);
    }

}
