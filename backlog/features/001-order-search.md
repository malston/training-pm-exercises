# Search Orders by Customer

**Type:** Feature
**Priority:** High
**Status:** Open

## Description

As a support agent, I need to search orders by customer email address so I can quickly find a customer's order history when they contact support.

## Acceptance Criteria

- [ ] GET /api/orders?email=alice@example.com returns only orders for that email
- [ ] Search is case-insensitive (Alice@Example.com matches alice@example.com)
- [ ] Returns empty list (not error) when no orders match
- [ ] Existing /api/orders endpoint continues to work without the email parameter

## Technical Notes

The OrderRepository already has a `findByCustomerEmail` method. This feature needs a controller parameter and service method wiring.

## Out of Scope

- Full-text search across all fields
- Pagination of results
