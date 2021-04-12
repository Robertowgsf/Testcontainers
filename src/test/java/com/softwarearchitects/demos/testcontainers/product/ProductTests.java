package com.softwarearchitects.demos.testcontainers.product;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Testcontainers
public class ProductTests {

    @ClassRule
    public static GenericContainer<?> activeMQContainer = new GenericContainer<>("rmohr/activemq:latest").withExposedPorts(61616);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    public ProductRepository productRepository;

    @Test
    void shouldSaveProduct() {
        Product product = new Product(null, "Test 2", new BigDecimal("10.05"));
        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        System.out.println("----------------> TEST 2 - Product Id: " + product.getId());
    }

    @Test
    void shouldSendAndReceiveMessage() {
        String messageToSend = "Hey, I'm a message! >//<";
        String receivedMessage = null;
        String queue = "some-queue";

        jmsTemplate.send("some-queue", session -> session.createTextMessage(messageToSend));
        TextMessage message = (TextMessage) jmsTemplate.receive("some-queue");

        try {
            receivedMessage = message.getText();
        } catch (JMSException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertEquals(messageToSend, receivedMessage);
    }

}
