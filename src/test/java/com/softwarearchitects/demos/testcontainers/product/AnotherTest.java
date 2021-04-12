package com.softwarearchitects.demos.testcontainers.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class AnotherTest {

    @Autowired
    public ProductRepository productRepository;

    @Test
    void shouldAlsoSaveProduct() {
        Product product = new Product(null, "Test 1", new BigDecimal("10.05"));
        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        System.out.println("----------------> TEST 1 - Product Id: " + product.getId());
    }

}
