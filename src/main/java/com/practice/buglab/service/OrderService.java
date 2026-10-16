package com.practice.buglab.service;

import com.practice.buglab.dto.CreateOrderRequest;
import com.practice.buglab.model.CustomerOrder;
import com.practice.buglab.model.OrderStatus;
import com.practice.buglab.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CustomerOrder create(CreateOrderRequest request) {
        // BUG 1: no request validation; nulls, invalid email, zero/negative quantity or price pass through.
        CustomerOrder order = new CustomerOrder(
                request.getCustomerEmail(),
                request.getProductName(),
                request.getQuantity(),
                request.getUnitPrice(),
                OrderStatus.PENDING,
                Instant.now()
                //null // BUG 2: creation timestamp is never set.
        );
        return repository.save(order);
    }

    public List<CustomerOrder> getAll() {
        return repository.findAll();
    }


    public CustomerOrder getById(Long id) {
        // BUG 3: missing ID returns null instead of a controlled 404.
        return repository.findById(id).orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order is not found with id: "+id ));
    }


    public List<CustomerOrder> getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email must not be blank"
            );
        }

        return repository.findByCustomerEmail(
                email.trim().toLowerCase(java.util.Locale.ROOT)
        );
    }



    @Transactional
    public CustomerOrder cancel(Long id) {
        CustomerOrder order = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order is not found with id: " + id
                ));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending orders can be cancelled"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        return repository.save(order);
    }


    @Transactional
    public CustomerOrder markShipped(Long id) {
        CustomerOrder order = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Order is not found with id: " + id
                ));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending orders can be shipped"
            );
        }

        order.setStatus(OrderStatus.SHIPPED);
        return repository.save(order);
    }

}
