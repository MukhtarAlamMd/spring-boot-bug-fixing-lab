package com.practice.buglab;

import com.practice.buglab.model.CustomerOrder;
import com.practice.buglab.model.OrderStatus;
import com.practice.buglab.repository.OrderRepository;
import com.practice.buglab.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService orderService;
    @Test
    void getByIdShouldReturnOrderWhenItExists() {
        CustomerOrder order = new CustomerOrder();
        order.setCustomerEmail("john.doe@example.com");
        order.setProductName("Wireless Mouse");
        order.setQuantity(3);
        order.setUnitPrice(new BigDecimal("24.99"));
        order.setStatus(OrderStatus.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        CustomerOrder result = orderService.getById(1L);

        assertNotNull(result);
        assertEquals("Wireless Mouse", result.getProductName());
        assertEquals(OrderStatus.PENDING, result.getStatus()); // This assertion fails
        assertEquals("Wireless Mouse", result.getProductName());

        verify(repository).findById(1L);
    }

    @Test
    void getByIdShouldThrowNotFoundWhenOrderDoesNotExist() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> orderService.getById(999L)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(repository).findById(999L);
    }

    @Test
    void cancelShouldChangePendingOrderToCancelled() {
        CustomerOrder order = new CustomerOrder();
       // order.setId(1L);
        order.setStatus(OrderStatus.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(order));
        when(repository.save(any(CustomerOrder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CustomerOrder result = orderService.cancel(1L);

        assertEquals(OrderStatus.CANCELLED, result.getStatus());
        verify(repository).save(order);
    }


    @Test
    void cancelShouldThrowConflictWhenOrderIsAlreadyShipped() {
        CustomerOrder order = new CustomerOrder();
        order.setStatus(OrderStatus.SHIPPED);

        when(repository.findById(1L)).thenReturn(Optional.of(order));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> orderService.cancel(1L)
        );

        assertEquals(409, exception.getStatusCode().value());
        verify(repository, never()).save(any(CustomerOrder.class));
    }

    @Test
    void cancelShouldThrowNotFoundWhenOrderDoesNotExist() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> orderService.cancel(999L)
        );

        assertEquals(404, exception.getStatusCode().value());
        verify(repository, never()).save(any(CustomerOrder.class));
    }

}
