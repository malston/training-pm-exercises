package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendStatusChangeEmail(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        String subject = switch (newStatus) {
            case CONFIRMED -> "Order Confirmed: #" + order.getId();
            case SHIPPED -> "Your Order Has Shipped: #" + order.getId();
            case DELIVERED -> "Order Delivered: #" + order.getId();
            case CANCELLED -> "Order Cancelled: #" + order.getId();
            default -> "Order Update: #" + order.getId();
        };

        log.info("[EMAIL] To: {}, Subject: {}, Body: Your order for {} x{} has been updated from {} to {}",
                order.getCustomerEmail(), subject, order.getProductName(),
                order.getQuantity(), oldStatus, newStatus);
    }

    // SCOPE CREEP: SMS notifications were not in the original ticket
    public void sendStatusChangeSms(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        String message = String.format("Order #%d update: %s -> %s", order.getId(), oldStatus, newStatus);
        log.info("[SMS] To: {}, Message: {}", order.getCustomerName(), message);
    }

    // SCOPE CREEP: Slack webhook integration was not in the original ticket
    public void sendSlackNotification(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        log.info("[SLACK] #orders channel: Order #{} changed from {} to {} (customer: {})",
                order.getId(), oldStatus, newStatus, order.getCustomerName());
    }
}
