package com.ecom.client.customer;

import com.ecom.client.repository.CustomerRepository;
import com.ecom.common.entity.AuthenticationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testUpdateAuthenticationType(){
        Integer id  = 3;
        customerRepository.updateCustomerAuthenticationType(id, AuthenticationType.DATABASE);
    }
}
