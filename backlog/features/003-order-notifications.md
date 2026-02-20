# Email Notifications for Order Status Changes

**Type:** Feature
**Priority:** High
**Status:** Open

## Description

Customers should receive email notifications when their order status changes. This reduces support tickets asking "where's my order?" which currently account for 40% of support volume.

## Acceptance Criteria

- [ ] When order status changes from PENDING to CONFIRMED, customer receives a confirmation email
- [ ] When order status changes to SHIPPED, customer receives a shipping notification with order details
- [ ] When order status changes to DELIVERED, customer receives a delivery confirmation
- [ ] When order is CANCELLED, customer receives a cancellation notice
- [ ] Emails are sent to the customerEmail field on the order
- [ ] Email failures are logged but do not block the status change
- [ ] No email is sent if the status doesn't actually change (idempotent)

## Technical Notes

Consider using Spring Events to decouple status changes from email sending. For the exercise, a mock email service (logging to console) is sufficient.

## Out of Scope

- SMS notifications
- Customer email preferences or opt-out
- Email templates with HTML formatting
