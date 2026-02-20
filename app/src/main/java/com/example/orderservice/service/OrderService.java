package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public Optional<Order> updateOrderStatus(Long id, OrderStatus newStatus) {
        return orderRepository.findById(id).map(order -> {
            OrderStatus oldStatus = order.getStatus();
            if (oldStatus == newStatus) {
                return order;
            }
            order.setStatus(newStatus);
            Order saved = orderRepository.save(order);
            notificationService.sendStatusChangeEmail(saved, oldStatus, newStatus);
            notificationService.sendStatusChangeSms(saved, oldStatus, newStatus);
            notificationService.sendSlackNotification(saved, oldStatus, newStatus);
            return saved;
        });
    }

    public boolean cancelOrder(Long id) {
        return orderRepository.findById(id).map(order -> {
            if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
                return false;
            }
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return true;
        }).orElse(false);
    }
}
