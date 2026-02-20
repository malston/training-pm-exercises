# Module 2: Software Design Principles -- Solutions

## Exercise 1: Separation of Concerns

### Controller vs Service responsibilities

**OrderController handles:**

- HTTP request/response mapping (status codes, path variables, request bodies)
- Input validation (`@Valid` annotation)
- Routing requests to the correct service method
- Translating service results into HTTP responses (200, 201, 204, 400, 404)

**OrderService handles:**

- Business logic (cancellation rules, status transitions)
- Data access coordination (calling repository methods)
- Domain operations (setting initial status on new orders)

**Why separate:** The controller knows about HTTP; the service knows about business rules. If the service returned HTTP status codes or the controller checked business rules, changes to one concern would force changes in the other. For example, adding a GraphQL interface would require a new controller but reuse the same service.

### Where does the notification feature go?

For `003-order-notifications.md` (email on status change):

- **OrderController** -- no changes needed. The controller already calls `updateOrderStatus`. Notifications are a side effect of a status change, not an HTTP concern.
- **OrderService** -- minimal changes. After saving the status change, call the notification service. The `updateOrderStatus` method is the right place because it's where the business event (status changed) occurs.
- **New class needed:** `NotificationService` -- separates notification logic (email formatting, delivery) from order logic. If notification requirements change (add SMS, change templates), only the notification service changes.

### Scope creep on feature/order-notifications

The `feature/order-notifications` branch added:

1. `sendStatusChangeEmail()` -- requested in ticket
2. `sendStatusChangeSms()` -- **not requested, explicitly out of scope**
3. `sendSlackNotification()` -- **not requested, explicitly out of scope**

The ticket's "Out of Scope" section explicitly states: "SMS notifications" and "Customer email preferences or opt-out."

**Design principle violated:** YAGNI (You Aren't Gonna Need It). The developer built features nobody asked for. The SMS and Slack methods add code to maintain, test, and debug without any business need driving them.

**PR review action:** Request removal of `sendStatusChangeSms()` and `sendSlackNotification()`. The developer should follow the ticket scope. If SMS/Slack notifications are valuable, they should be separate tickets with their own acceptance criteria.

## Exercise 2: YAGNI in the Backlog

### Classification

| Ticket                       | Classification                | Rationale                                                                                   |
| ---------------------------- | ----------------------------- | ------------------------------------------------------------------------------------------- |
| 001-order-search             | Need now                      | High priority, support team needs it, small scope                                           |
| 002-order-history-export     | Might need later              | "Customers keep asking" but vague requirements suggest it hasn't been fully thought through |
| 003-order-notifications      | Need now                      | High priority, reduces 40% of support volume, clear acceptance criteria                     |
| 004-bulk-status-update       | Might need later              | Low priority, warehouse workflow -- worth understanding actual volume first                 |
| 005-customer-dashboard       | Over-engineering (as written) | Too large as a single feature -- needs decomposition into smaller deliverables              |
| 001-cancel-returns-400 (bug) | Need now                      | Medium priority, directly causes customer confusion and support calls                       |
| 002-missing-validation (bug) | Need now                      | High priority, data integrity issue that "breaks stuff"                                     |
| 001-pagination               | Might need later              | Medium priority, only matters at scale -- how many orders exist today?                      |
| 002-better-errors            | Over-engineering (as written) | Vague criteria mean scope will expand indefinitely; needs specific error scenarios first    |

### Recommendations

**Defer:**

- 004-bulk-status-update -- ask the warehouse team how often they do bulk updates and how many orders at a time. If it's 5 orders a week, a manual process is fine.
- 001-pagination -- how many orders are in the system? If fewer than 1,000, pagination adds complexity without benefit.

**Push back:**

- 005-customer-dashboard -- this is 4+ features bundled as one. Decompose it. Start with the highest-impact piece (Order History, since it supports the "where's my order?" reduction goal).
- 002-better-errors -- reject as written. "Errors should be more helpful" isn't actionable. Ask the PM to identify the 3 most common error scenarios customers encounter and write specific criteria for each.

## Exercise 3: Naming Review

### Name evaluation

| Name                                  | Clear? | Analysis                                                                                                                                                                     |
| ------------------------------------- | ------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `OrderService.cancelOrder()`          | Yes    | Verb + noun, describes the business action. Returns boolean (success/failure) which could be clearer -- an exception or result type would explain _why_ cancellation failed. |
| `OrderController.updateOrderStatus()` | Yes    | Accurately describes what it does. The PATCH method and `/status` path reinforce the partial-update semantics.                                                               |
| `OrderStatus` enum values             | Mostly | PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED all describe business states a customer would understand. No jargon or implementation-specific values.                     |

### Problem names: "NewOrderService" or "ImprovedValidator"

Questions to ask:

1. **What does it do?** The name should describe the service's responsibility, not when it was written. "New" relative to what? In six months, is it still "new"?
2. **What was wrong with the old one?** If the old service is being replaced, delete it and give this one the correct name. If both coexist, name them by what differentiates their behavior.
3. **What does "improved" mean?** Improved how? Faster? More accurate? More complete? The name should reflect the actual characteristic.

A service named `OrderValidationService` tells you what it does. A service named `ImprovedOrderValidator` tells you someone thought the old one was bad -- but not what this one does differently.
