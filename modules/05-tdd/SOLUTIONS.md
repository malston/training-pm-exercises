# Module 5: Writing Testable Acceptance Criteria -- Solutions

## Exercise 1: Identify Vague Criteria

### Testability assessment

| Ticket                          | Testable? | Issues                                                              |
| ------------------------------- | --------- | ------------------------------------------------------------------- |
| 001-order-search                | Yes       | Specific endpoint, clear behavior for each case, edge cases covered |
| 002-order-history-export        | No        | No format specified, no endpoint defined, no performance threshold  |
| 002-missing-validation (bug)    | No        | "breaks stuff" -- which stuff? No expected behavior defined         |
| 002-better-errors (improvement) | No        | "more helpful," "better," "clear" are subjective, not measurable    |

### What's missing from each vague ticket

**002-order-history-export:**

- **Format:** CSV, JSON, PDF? Each has different implementation
- **Scope:** "Users can export their orders" -- which users? All orders or filtered? Does "their" mean per-customer or per-session?
- **Performance:** "Should be fast" -- 1 second? 5 seconds? For how many records?
- **Edge cases:** What if a customer has zero orders? What if they have 100,000?
- **Endpoint:** No URL pattern. `GET /api/orders/export`? `POST /api/exports`?

**002-missing-validation (bug):**

- **Which fields:** Quantity? Unit price? Both? What about zero values?
- **What breaks:** Database errors? Calculation errors? Display errors?
- **Expected response:** What HTTP status? What error message format?
- **Boundary values:** Is 0 valid? Is 0.01 valid? Is there a maximum?

**002-better-errors (improvement):**

- **Which errors:** All of them? Specific ones causing customer confusion?
- **Definition of "helpful":** Does it mean including the field name? A suggestion for fixing? An error code?
- **Current state:** What do errors look like today? We need a baseline to measure against.

## Exercise 2: Rewrite with Given/When/Then

### 002-order-history-export rewrite

```
Acceptance Criteria:

- Given a customer with 3 orders,
  When they request GET /api/orders/export?email=alice@example.com&format=csv,
  Then the response is a CSV file with a header row and 3 data rows.

- Given a customer with 0 orders,
  When they request an export,
  Then the response is a CSV file with only a header row.

- Given a customer with 500 orders,
  When they request an export,
  Then the response is returned within 2 seconds.

- Given an export request with an unsupported format parameter,
  When the request is processed,
  Then the API returns 400 with message "Supported formats: csv, json".

- Given an export request without the email parameter,
  When the request is processed,
  Then the API returns 400 with message "Email parameter is required".
```

### 002-missing-validation (bug) rewrite

```
Description:
POST /api/orders accepts negative values for quantity and unitPrice,
resulting in orders with negative totals in the database.

Acceptance Criteria:

- Given a POST request with quantity = -1,
  When the request is processed,
  Then the API returns 400 with a validation error for the quantity field.

- Given a POST request with unitPrice = -5.00,
  When the request is processed,
  Then the API returns 400 with a validation error for the unitPrice field.

- Given a POST request with quantity = 0,
  When the request is processed,
  Then the API returns 400 (zero is not a valid quantity).

- Given a POST request with valid positive quantity and unitPrice,
  When the request is processed,
  Then the order is created successfully (existing behavior unchanged).
```

### 002-better-errors (improvement) rewrite

```
Acceptance Criteria:

- Given a GET request for /api/orders/999 (non-existent order),
  When the request is processed,
  Then the API returns 404 with body {"error": "Order not found", "orderId": 999}.

- Given a POST request with missing required field "customerName",
  When the request is processed,
  Then the API returns 400 with body {"error": "Validation failed",
  "fields": [{"field": "customerName", "message": "must not be blank"}]}.

- Given a PATCH request to /api/orders/{id}/status with invalid status "UNKNOWN",
  When the request is processed,
  Then the API returns 400 with body {"error": "Invalid status",
  "validValues": ["PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"]}.

- Given a POST /api/orders/{id}/cancel for a shipped order,
  When the request is processed,
  Then the API returns 409 with body {"error": "Orders that have been shipped cannot be cancelled"}.
```

### Missing edge cases identified during rewrite

- **Export:** What if the email parameter contains special characters? What about CSV injection (cells starting with `=`)?
- **Validation:** What about extremely large quantities (Integer.MAX_VALUE)? What about extremely precise prices (0.001)?
- **Errors:** What about concurrent modification (two requests cancel the same order simultaneously)?

## Exercise 3: Validate Against the Code

### Using 001-order-search to test criterion-to-code mapping

**Criterion 1:** `GET /api/orders?email=alice@example.com returns only orders for that email`

Can Claude write a test? Yes:

```
Given orders for alice@example.com and bob@example.com exist,
When GET /api/orders?email=alice@example.com is called,
Then only alice's orders are returned.
```

**Criterion 2:** `Search is case-insensitive`

Can Claude write a test? Yes:

```
Given an order for alice@example.com exists,
When GET /api/orders?email=Alice@Example.com is called,
Then the order is returned.
```

**Criterion 3:** `Returns empty list (not error) when no orders match`

Can Claude write a test? Yes:

```
Given no orders for unknown@example.com exist,
When GET /api/orders?email=unknown@example.com is called,
Then the response is 200 with an empty list [].
```

**Criterion 4:** `Existing /api/orders endpoint continues to work without the email parameter`

Can Claude write a test? Yes:

```
Given orders exist in the system,
When GET /api/orders is called without the email parameter,
Then all orders are returned (backward compatible).
```

**Ambiguity found:** None in this ticket. Each criterion maps to exactly one test scenario. This is what well-written acceptance criteria look like -- a developer (or Claude) can read each criterion and immediately write a test for it.
