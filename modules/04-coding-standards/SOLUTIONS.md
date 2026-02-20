# Module 4: Coding Standards -- Solutions

## Exercise 1: Read the CLAUDE.md

### Conventions enforced by CLAUDE.md

The project's CLAUDE.md specifies:

1. **Directory structure:** Controllers in `controller/`, services in `service/`, entities in `model/`, tests mirror source
2. **Dependency injection:** Constructor injection, not field injection
3. **Return types:** Controllers return `ResponseEntity`
4. **Database:** H2 in-memory for development
5. **Backlog format:** Numbered markdown files following the ticket template

### Would Claude know project conventions?

Partially. CLAUDE.md tells Claude the file organization, injection pattern, and return type convention. Claude would follow these when generating code.

However, CLAUDE.md doesn't cover:

- Naming conventions for methods (e.g., `getOrderById` vs `findOrder`)
- Error handling patterns (when to return 400 vs 404 vs 409)
- Test conventions (integration tests vs unit tests, test naming)
- Validation approach (Bean Validation annotations vs manual checks)

### Rules to add

```markdown
### Error Handling

- Return 404 for resource-not-found, 400 for invalid input, 409 for business rule violations
- Include an error message in the response body -- never return an empty error response
- Log exceptions at the service layer, not the controller layer

### Testing

- Write integration tests using @SpringBootTest with the H2 database
- Test naming: methodName_condition_expectedResult (e.g., cancelOrder_shippedOrder_returnsFalse)
- Each controller endpoint needs at least one happy-path and one error-path test
```

## Exercise 2: Consistency Check

### Branch comparison

| Aspect               | feature/order-search                                           | feature/order-notifications                             | feature/bulk-status                                               |
| -------------------- | -------------------------------------------------------------- | ------------------------------------------------------- | ----------------------------------------------------------------- |
| Follows ticket scope | Yes -- only adds email parameter                               | No -- adds SMS and Slack beyond scope                   | Unclear -- no acceptance criteria on ticket                       |
| Code patterns        | Follows existing controller pattern (optional `@RequestParam`) | Adds constructor injection for new service (consistent) | Uses raw `Map<String, Object>` instead of a typed request body    |
| Naming               | `email` parameter matches ticket language                      | Method names match actions (`sendStatusChangeEmail`)    | Variable names are clear but no request DTO                       |
| Tests                | None added (should have tests)                                 | None added (should have tests)                          | None added (should have tests)                                    |
| Error handling       | Relies on existing null-check flow                             | Email failures logged but not handled in controller     | No error handling for invalid IDs, invalid status, or empty lists |

### Most consistent: `feature/order-search`

It adds a single parameter to an existing endpoint, follows the established pattern, and matches the ticket scope exactly. The only gap is missing tests.

### Most deviation: `feature/order-notifications`

While the code quality is decent (proper separation with `NotificationService`), it violates the ticket's explicit "Out of Scope" section by adding SMS and Slack integrations. Code quality doesn't matter if you're building the wrong thing.

### Runner-up for deviation: `feature/bulk-status`

Uses `Map<String, Object>` with unchecked casts instead of a typed request body, which is inconsistent with the `@Valid @RequestBody Order` pattern used in `createOrder`. No input validation, no error handling for missing IDs, and the ticket has no acceptance criteria to validate against.

## Exercise 3: Standards as Investment

### Current state of the order service

The order service has 5 endpoints:

1. `GET /api/orders` (with optional status filter)
2. `GET /api/orders/{id}`
3. `POST /api/orders`
4. `PATCH /api/orders/{id}/status`
5. `POST /api/orders/{id}/cancel`

**Consistency level:** Moderate. All endpoints follow the same controller-service-repository pattern and use `ResponseEntity`. But error handling is inconsistent -- `createOrder` returns 201, `cancelOrder` returns 400 with empty body (no error message), and `getOrderById` returns 404 with empty body.

### Would "add pagination to all endpoints" be consistent without docs?

No. Without documented conventions for pagination, Claude would make assumptions:

- Should `GET /api/orders/{id}` be paginated? (No -- it returns a single order.)
- What response format? Spring's `Page<T>` includes metadata automatically, but should we use a custom wrapper?
- What default page size? 10? 20? 50?
- What's the max page size?

With a documented standard like "`GET` list endpoints use Spring's `Pageable` with default size 20, max 100, and return `Page<T>` wrapper," Claude produces consistent pagination across all list endpoints.

### Cost of inconsistency over 6 months

Each inconsistency creates a decision point for every developer (including Claude):

- "Which error pattern should I follow?" -- developer studies 3 different patterns, picks one, may pick the wrong one
- "Should I add tests?" -- some branches have them, some don't, so it seems optional
- "How do I handle request bodies?" -- typed DTO or raw Map? Both exist in the codebase

At 5 developers making 3 such decisions per week, that's 15 wasted decision points per week. Each takes 5-15 minutes of reading existing code to understand the pattern (or guessing wrong and needing a review cycle).

Over 6 months: ~390 wasted decisions, each taking 5-15 minutes = 32-97 hours of developer time spent on decisions that documented standards would eliminate.

The investment in standards pays for itself within the first month.
