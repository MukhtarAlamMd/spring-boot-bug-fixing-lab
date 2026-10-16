
package com.practice.buglab.controller;

import com.practice.buglab.dto.CreateOrderRequest;
import com.practice.buglab.model.CustomerOrder;
import com.practice.buglab.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrder create(
            @Valid @RequestBody CreateOrderRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public CustomerOrder getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<CustomerOrder> getOrders(
            @RequestParam(required = false) String email) {

        if (email == null || email.isBlank()) {
            return service.getAll();
        }

        return service.getByEmail(email);
    }

    @PatchMapping("/{id}/cancel")
    public CustomerOrder cancel(@PathVariable Long id) {
        return service.cancel(id);
    }

    @PatchMapping("/{id}/ship")
    public CustomerOrder ship(@PathVariable Long id) {
        return service.markShipped(id);
    }
}
