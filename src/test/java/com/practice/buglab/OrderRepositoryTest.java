
package com.practice.buglab;

import com.practice.buglab.model.CustomerOrder;
import com.practice.buglab.model.OrderStatus;
import com.practice.buglab.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repository;

    private CustomerOrder createOrder() {
        CustomerOrder order = new CustomerOrder();
        order.setCustomerEmail("buyer@example.com");
        order.setProductName("USB-C Cable");
        order.setQuantity(2);
        order.setUnitPrice(new BigDecimal("9.99"));
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    @Test
    void saveShouldGenerateId() {
        CustomerOrder saved = repository.save(createOrder());

        assertNotNull(saved.getId());
        assertEquals("USB-C Cable", saved.getProductName());
    }

    @Test
    void findByIdShouldReturnSavedOrder() {
        CustomerOrder saved = repository.save(createOrder());

        Optional<CustomerOrder> result =
                repository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("buyer@example.com",
                result.get().getCustomerEmail());
    }

    @Test
    void findByIdShouldReturnEmptyForMissingOrder() {
        Optional<CustomerOrder> result = repository.findById(999999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteShouldRemoveOrder() {
        CustomerOrder saved = repository.save(createOrder());
        Long id = saved.getId();

        repository.deleteById(id);
        repository.flush();

        assertFalse(repository.findById(id).isPresent());
    }
}
