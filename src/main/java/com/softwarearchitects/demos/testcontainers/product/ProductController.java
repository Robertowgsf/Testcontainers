package com.softwarearchitects.demos.testcontainers.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    private final JmsTemplate jmsTemplate;

    @PostMapping
    public Product save(@RequestBody Product product) throws JsonProcessingException {
        Product savedProduct = productRepository.save(product);
        String savedProductJson = new ObjectMapper().writeValueAsString(savedProduct);
        this.jmsTemplate.send("product-created", session -> session.createTextMessage(savedProductJson));
        return savedProduct;
    }

    @GetMapping
    public Optional<Product> get(Long id) {
        return productRepository.findById(id);
    }
}
