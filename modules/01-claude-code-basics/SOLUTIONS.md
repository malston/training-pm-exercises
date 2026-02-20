# Module 1: How Claude Code Works -- Solutions

## Exercise 1: Watch a Developer Session

### Example observations

1. **Claude reads files before suggesting changes.** It doesn't assume what's in a file based on the filename or project structure -- it opens and reads the actual content first. This matters because file names can be misleading and code evolves.

2. **Claude asks for clarification when a request is ambiguous.** When a developer says "fix the tests," Claude asks which tests or reads test output to determine which ones are failing. It doesn't guess.

3. **Claude re-reads files after making changes.** After editing a file, it often re-reads it to verify the edit was applied correctly. This is a verification loop -- the same pattern developers use when they check their work.

## Exercise 2: Spot the Context Limit

### Context analysis

The order service has roughly:

- `Order.java`: ~60 lines
- `OrderStatus.java`: ~10 lines
- `OrderRepository.java`: ~15 lines
- `OrderService.java`: ~55 lines
- `OrderController.java`: ~65 lines
- `pom.xml`: ~80 lines
- Test files: ~100+ lines
- Configuration files: ~30 lines

Total: ~415+ lines of source code, which is about 2,500-3,500 tokens. This is a small project. The context window (200K tokens) could hold it many times over.

The issue isn't the current codebase -- it's that feature `005-customer-dashboard.md` describes four major components (Order History, Active Order Tracking, Account Management, Order Actions) that would require significant implementation across multiple files. Implementing it all in one session would mean Claude is holding the full spec, all existing code, all the code it's writing, and its conversation history simultaneously.

### Task decomposition for customer dashboard

| Task                        | Scope                                                                        | Why standalone                                      |
| --------------------------- | ---------------------------------------------------------------------------- | --------------------------------------------------- |
| 1. Order History API        | `GET /api/orders?email=X` with pagination, status filter, product search     | Builds on existing endpoint, independently testable |
| 2. Active Order Tracking    | Ship date tracking, estimated delivery calculation, "mark received" endpoint | New business logic, independently testable          |
| 3. Account Management       | Profile view/update endpoints, order statistics aggregation                  | Separate domain (customer profile vs. orders)       |
| 4. Order Actions            | Reorder endpoint, PDF receipt generation                                     | Each action is independent                          |
| 5. Frontend (if applicable) | HTML page or API documentation for frontend team                             | Depends on Tasks 1-4 being done                     |

Each task can be completed in a single Claude session, tested independently, and committed separately. The key insight: Claude works best when each session has a clear, bounded goal.

## Exercise 3: Requirements Quality Matters

### Ticket comparison

| Aspect          | 001-order-search (well-written)                                 | 002-order-history-export (vague)      |
| --------------- | --------------------------------------------------------------- | ------------------------------------- |
| User story      | "As a support agent, I need to search orders by customer email" | "We need to be able to export orders" |
| Endpoint        | `GET /api/orders?email=alice@example.com`                       | Not specified                         |
| Behavior spec   | Case-insensitive, empty list on no match, backward compatible   | "should work well"                    |
| Edge cases      | Explicitly handles no-match case                                | None mentioned                        |
| Out of scope    | Full-text search, pagination                                    | Not defined                           |
| Technical hints | "OrderRepository already has findByCustomerEmail"               | None                                  |

### What's missing from 002

1. **Format:** CSV? JSON? PDF? Excel? The developer would have to guess or ask.
2. **Scope:** All orders or per-customer? Date range filtering? The word "their" implies per-customer but doesn't specify the mechanism.
3. **Performance:** "Should be fast" is not a measurable criterion. Fast for 10 orders? 10,000?
4. **Endpoint design:** No URL pattern specified. Claude would invent one.
5. **Error handling:** What happens if export fails? What about customers with no orders?

### Could Claude implement each correctly?

**001:** Yes. The acceptance criteria are specific enough that Claude can write the controller parameter, wire the service method, and write tests for each criterion.

**002:** It would produce something, but not necessarily what the PM wants. Claude would make assumptions about format (probably JSON since it's a REST API), scope (probably all orders), and performance (no optimization). The result might technically "work" but miss the actual business need.
