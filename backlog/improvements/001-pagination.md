# Add Pagination to Order List

**Type:** Improvement
**Priority:** Medium
**Status:** Open

## Description

The GET /api/orders endpoint returns all orders in a single response. As the order count grows, this will cause performance issues and excessive memory usage. We need pagination.

## Acceptance Criteria

- [ ] GET /api/orders accepts optional `page` (default 0) and `size` (default 20) query parameters
- [ ] Response includes pagination metadata: totalElements, totalPages, currentPage, pageSize
- [ ] Default page size is 20 orders
- [ ] Maximum page size is 100 (requests for more than 100 are capped)
- [ ] Existing clients that don't pass pagination parameters get the first page (backward compatible)
- [ ] Filtering by status still works with pagination

## Technical Notes

Spring Data JPA supports pagination via `Pageable` parameter in repository methods and `Page<T>` return types.

## Out of Scope

- Cursor-based pagination
- Sorting options (separate ticket)
