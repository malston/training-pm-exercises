# Before/After: Rewriting Acceptance Criteria

## Example 1: Export Feature

### Before (vague)

```
Acceptance Criteria:
- Users can export their orders
- The export should work well
- It should be fast
```

**Problems:** What format? Which users? What's "well"? What's "fast"?

### After (testable)

```
Acceptance Criteria:
- Given a customer with 3 orders,
  When they request GET /api/orders/export?email=alice@example.com&format=csv,
  Then the response is a CSV file with 3 rows plus a header row.

- Given a customer with 0 orders,
  When they request an export,
  Then the response is a CSV file with only a header row.

- Given a customer with 500 orders,
  When they request an export,
  Then the response is returned within 2 seconds.

- Given an export request with an unsupported format,
  When the request is processed,
  Then the API returns 400 with message "Supported formats: csv, json".
```

## Example 2: Validation Bug

### Before (vague)

```
the api lets you create orders with negative numbers and it breaks stuff
```

**Problems:** Which numbers? What breaks? What should happen instead?

### After (testable)

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

## Example 3: Error Handling

### Before (vague)

```
Acceptance Criteria:
- Errors should be more helpful
- The API should handle edge cases better
- Error messages should be clear
```

**Problems:** Which errors? Which edge cases? What does "clear" mean?

### After (testable)

```
Acceptance Criteria:
- Given a GET request for /api/orders/999 (non-existent order),
  When the request is processed,
  Then the API returns 404 with body {"error": "Order not found", "orderId": 999}.

- Given a POST request with missing required field "customerName",
  When the request is processed,
  Then the API returns 400 with body {"error": "Validation failed",
  "fields": [{"field": "customerName", "message": "must not be blank"}]}.

- Given a PATCH request with invalid status value "UNKNOWN",
  When the request is processed,
  Then the API returns 400 with body {"error": "Invalid status",
  "validValues": ["PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"]}.

- Given any unhandled server error,
  When the request is processed,
  Then the API returns 500 with body {"error": "Internal server error"}
  and the exception is logged with a correlation ID.
```
