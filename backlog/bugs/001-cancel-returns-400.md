# Cancel Endpoint Returns 400 for Shipped Orders

**Type:** Bug
**Priority:** Medium
**Status:** Open

## Description

When trying to cancel a shipped order, the API returns 400 Bad Request with no explanation. Customers see a generic error in the UI and call support confused about why their cancellation failed.

## Steps to Reproduce

1. Create an order (POST /api/orders)
2. Update status to SHIPPED (PATCH /api/orders/{id}/status)
3. Try to cancel (POST /api/orders/{id}/cancel)
4. Response: 400 with empty body

## Expected Behavior

The API should return a 409 Conflict with a clear error message explaining that shipped orders cannot be cancelled and providing alternatives (e.g., "Please contact support to arrange a return").

## Acceptance Criteria

- [ ] POST /api/orders/{id}/cancel for a shipped order returns 409 Conflict
- [ ] Response body includes error message: "Orders that have been shipped cannot be cancelled"
- [ ] POST /api/orders/{id}/cancel for a delivered order returns 409 with: "Delivered orders cannot be cancelled"
- [ ] POST /api/orders/{id}/cancel for a pending or confirmed order still works (returns 204)
