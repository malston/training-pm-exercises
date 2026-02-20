# Customer Dashboard

**Type:** Feature
**Priority:** High
**Status:** Open

## Description

Build a customer-facing dashboard where customers can view their order history, track active orders, and manage their account. This replaces the current support-heavy workflow where customers email support to check order status.

## User Story

As a customer, I want a self-service dashboard so I can check my order status, view my history, and update my contact information without calling support.

## Requirements

### Order History View

- Display all orders for the logged-in customer, sorted by date (most recent first)
- Show order ID, product name, quantity, total, status, and date
- Filter orders by status (pending, shipped, delivered, cancelled)
- Search orders by product name
- Paginate at 10 orders per page

### Active Order Tracking

- For orders with status SHIPPED, display a progress indicator: Order Placed -> Confirmed -> Shipped -> Delivered
- Show estimated delivery date (calculate as 5 business days from ship date)
- Allow customers to mark an order as "received" (transitions to DELIVERED)

### Account Management

- View current profile: name, email
- Update email address (requires email verification)
- View order statistics: total orders, total spent, most ordered product

### Order Actions

- Cancel pending or confirmed orders (existing endpoint)
- Reorder: create a duplicate order from a previous order
- Download order receipt as PDF

## Technical Constraints

- Must work with the existing Order API
- Authentication: use the existing customer email as identifier (no login system yet)
- Frontend: can be a simple HTML page served by Spring Boot, or a REST API consumed by a separate frontend

## Success Metrics

- 50% reduction in "where's my order" support tickets within 30 days
- 70% of customers use the dashboard at least once per month
- Customer satisfaction score improves from 3.2 to 4.0

## Out of Scope

- Payment processing
- Order modification (changing quantity/product after order)
- Multi-language support
- Mobile app (web only)
