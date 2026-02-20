# Module 6: Working with Developer Workflows -- Solutions

## Exercise 1: Review a Clean PR

### feature/order-search vs 001-order-search.md

**Criterion-by-criterion check:**

| Acceptance Criterion                                    | Implemented?              | Evidence                                                                                                                                                                                                      |
| ------------------------------------------------------- | ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `GET /api/orders?email=X` returns orders for that email | Yes                       | `OrderController.getAllOrders()` adds `@RequestParam(required = false) String email` and calls `orderService.getOrdersByCustomerEmail(email)`                                                                 |
| Search is case-insensitive                              | Depends on implementation | `OrderRepository.findByCustomerEmail()` does an exact match by default. If the database collation is case-insensitive (H2 default), it works. For PostgreSQL, it would need `findByCustomerEmailIgnoreCase()` |
| Returns empty list (not error) when no match            | Yes                       | `findByCustomerEmail` returns an empty `List<Order>`, which is wrapped in `ResponseEntity.ok()` -- returns 200 with `[]`                                                                                      |
| Existing endpoint works without email parameter         | Yes                       | `email` is `required = false`. When null, the existing status-filter or all-orders path executes unchanged                                                                                                    |

**Gaps identified:**

1. **Case sensitivity is not explicitly handled in code.** The `findByCustomerEmail` method does an exact match. It works on H2 because H2's default string comparison is case-insensitive, but this would fail on PostgreSQL. A production-ready implementation should use `findByCustomerEmailIgnoreCase()`.

2. **No tests.** The feature branch doesn't include any tests. While the code change is small, each acceptance criterion should have a corresponding test to verify the behavior and prevent regressions.

3. **No input validation.** What if `email` is an empty string? The code would pass `""` to `findByCustomerEmail`, which returns an empty list -- technically correct but potentially confusing.

**Overall:** This is a clean, focused PR that matches the ticket scope. The code change is minimal (adding one parameter and one conditional branch). The main feedback would be: add tests and verify case-insensitive behavior explicitly.

## Exercise 2: Spot Scope Creep

### feature/order-notifications vs 003-order-notifications.md

**What was requested:**

- Email notifications on four status transitions (CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- Emails sent to `customerEmail` field
- Email failures logged but don't block status change
- No email if status doesn't change (idempotent)

**What was implemented:**

| Component                                     | Requested? | Analysis                                                                                                                                         |
| --------------------------------------------- | ---------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| `NotificationService.sendStatusChangeEmail()` | Yes        | Matches the ticket -- sends email notification on status change                                                                                  |
| `NotificationService.sendStatusChangeSms()`   | No         | Ticket explicitly lists "SMS notifications" as out of scope                                                                                      |
| `NotificationService.sendSlackNotification()` | No         | Not mentioned in ticket at all, and "Customer email preferences or opt-out" is out of scope                                                      |
| Idempotency check (`oldStatus == newStatus`)  | Yes        | Returns early if status unchanged, preventing duplicate notifications                                                                            |
| Email failure handling                        | Partially  | Email is called synchronously in `updateOrderStatus` -- if it throws, the status change could fail. Ticket says "do not block the status change" |

**Out-of-scope additions:**

1. `sendStatusChangeSms()` -- the "Out of Scope" section explicitly says "SMS notifications"
2. `sendSlackNotification()` -- not mentioned anywhere in the ticket

**How to handle in PR review:**

```
Review comment on NotificationService.java:

Lines 30-39 (sendStatusChangeSms, sendSlackNotification):
These methods aren't in the ticket scope. The ticket's "Out of Scope"
section explicitly excludes SMS notifications. Please remove these
methods. If we decide to add SMS and Slack later, we should create
separate tickets with their own acceptance criteria and testing.

Line in OrderService.java (updateOrderStatus):
The notification calls are synchronous. If sendStatusChangeEmail()
throws an exception, the status change has already been saved but
the remaining notification calls won't execute. The ticket says
"Email failures are logged but do not block the status change."
Consider wrapping the email call in a try/catch.
```

## Exercise 3: Missing Acceptance Criteria

### feature/bulk-status vs 004-bulk-status-update.md

The ticket has only: "Make it possible to update multiple orders at once. The warehouse team needs this for when a shipment goes out."

No acceptance criteria at all.

### Acceptance criteria that should have existed

```
Acceptance Criteria:

- Given a request with valid order IDs [1, 2, 3] and status "SHIPPED",
  When PATCH /api/orders/bulk-status is called,
  Then all three orders are updated to SHIPPED and returned in the response.

- Given a request that includes a non-existent order ID,
  When the request is processed,
  Then existing orders are updated and the response indicates which IDs
  were not found.

- Given a request with an empty list of IDs,
  When the request is processed,
  Then the API returns 400 with message "At least one order ID is required".

- Given a request with more than 100 order IDs,
  When the request is processed,
  Then the API returns 400 with message "Maximum 100 orders per batch".

- Given a request with an invalid status value,
  When the request is processed,
  Then the API returns 400 with the list of valid status values.

- Given an order that cannot transition to the requested status
  (e.g., cancelling a delivered order),
  When the request is processed,
  Then that order is skipped and the response indicates why.

- Given a valid bulk update request,
  When the request is processed,
  Then the response includes a summary: {"updated": 3, "failed": 1,
  "errors": [{"orderId": 4, "reason": "Order not found"}]}.
```

### Issues in the current implementation

Looking at the `feature/bulk-status` branch code:

1. **No request validation.** Uses `Map<String, Object>` with unchecked casts. If `ids` is missing or `status` is invalid, it throws a runtime exception (500 Internal Server Error).

2. **No size limits.** A request with 10,000 IDs would attempt to update all of them, potentially timing out or causing database contention.

3. **Silent failures.** Orders that don't exist are silently filtered out (`Optional::isPresent`). The caller gets no indication that some updates failed.

4. **No error reporting.** The response is just a list of updated orders. If you send 10 IDs and get back 7 orders, you have to figure out which 3 failed and why.

5. **No input typing.** Using `Map<String, Object>` instead of a typed request DTO means no compile-time safety and no Bean Validation support. The existing `createOrder` endpoint uses `@Valid @RequestBody Order` -- this endpoint should follow the same pattern with a `BulkStatusUpdateRequest` class.

6. **Business rule bypass.** The implementation calls `updateOrderStatus` for each ID, which allows any status transition. Should the warehouse be able to set orders to CANCELLED? Probably not -- the bulk update for "shipment goes out" should only allow transitions to SHIPPED.

These issues demonstrate why acceptance criteria matter: without them, the developer makes assumptions about every edge case, and the PM has no basis for reviewing whether the implementation meets the business need.
