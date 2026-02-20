# Module 3: Architecture Decisions -- Solutions

## Exercise 1: Trade-off Analysis

### H2 vs PostgreSQL

| Factor               | H2 (current)                                             | PostgreSQL                                            | Notes                                           |
| -------------------- | -------------------------------------------------------- | ----------------------------------------------------- | ----------------------------------------------- |
| Setup complexity     | None (embedded, starts with app)                         | Moderate (install, configure, connection string)      | H2 requires zero infrastructure                 |
| Data persistence     | None (in-memory, lost on restart)                        | Full (disk-based, survives restarts)                  | H2 has a file mode but it's not commonly used   |
| Team familiarity     | High (no configuration needed)                           | Varies (need DBA skills for production tuning)        | Most Java developers have PostgreSQL experience |
| Production readiness | Not suitable (no concurrent access, no real SQL dialect) | Production-grade (ACID, concurrent users, extensions) | H2 is a development/testing tool                |
| Cost                 | Free (runs in same JVM)                                  | Low ($20-50/month for managed, free self-hosted)      | AWS RDS PostgreSQL starts at ~$15/month         |

### Questions to ask before approving

1. **How many concurrent users do we expect?** H2 doesn't handle concurrent writes. If more than one user accesses the API simultaneously, H2 is not viable.
2. **Is data persistence required?** If losing all orders on restart is acceptable (demo/prototype), H2 is fine. If not, PostgreSQL is necessary.
3. **What's the deployment target?** If deploying to a container or cloud, a separate database service is standard. H2 creates coupling between the application process and data storage.
4. **When is production launch?** If production is 3+ months away, stay on H2 for development speed. Switch to PostgreSQL when you start integration testing.

### Which maps to "simplest thing that works"?

H2 -- right now. The order service is a training exercise with no real users. Adding PostgreSQL introduces infrastructure complexity (installation, connection management, migration tooling) that doesn't serve the current need.

The decision changes when any of these become true: data must survive restarts, multiple instances need the same data, or production deployment is imminent.

## Exercise 2: Reversibility Assessment

| Change                                              | Reversibility | Rationale                                                                                                                                                                     |
| --------------------------------------------------- | ------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1. Add `GET /api/orders/summary` endpoint           | Easy          | Adding an endpoint doesn't change existing ones. Remove it anytime. No clients depend on it yet.                                                                              |
| 2. Split `customerName` into `firstName`/`lastName` | Hard          | Database schema change affects every query, every display, every import/export. Existing data needs migration. Every client that sends or receives customer name must change. |
| 3. Replace Spring Boot with Node.js Express         | Hard          | Rewriting the entire application. Different language, different runtime, different deployment, different team skills. Months of work to reverse.                              |
| 4. Add `notes` field to Order entity                | Easy          | New nullable column, no existing code breaks. Can be removed by dropping the column and removing the field.                                                                   |
| 5. Switch REST to GraphQL                           | Hard          | Changes every client integration. REST and GraphQL have different error handling, pagination, and caching patterns. Existing API consumers all break.                         |

### Validation needed for hard-to-reverse items

**Splitting customerName:**

- How many systems consume customer name data? Each needs updating.
- What about existing data? Is "Alice Chen" always "Alice" + "Chen"? What about "Mary Jane Watson" or single-name customers?
- Do any reports, exports, or integrations depend on the current format?

**Replacing Spring Boot with Express:**

- What business problem does this solve that Spring Boot can't?
- Does the team have Node.js production experience?
- What's the migration plan? Full rewrite or incremental?

**Switching to GraphQL:**

- How many API consumers exist? Each needs a new client library.
- Does the team have GraphQL schema design experience?
- Can we run both REST and GraphQL during transition?

## Exercise 3: Scalability Questions

### What breaks first at 10x users (100 -> 1,000 concurrent)?

1. **Database connections.** H2 can't handle concurrent writes at all. Even PostgreSQL needs connection pooling configured (default pool is typically 10 connections).

2. **GET /api/orders returns all orders.** With 1,000 users creating orders, this endpoint returns an unbounded list. At 10,000+ orders, response size and query time become problems. Pagination is the fix (see `001-pagination.md`).

3. **Single application instance.** One Spring Boot process handles all requests. Under load, thread pool exhaustion causes requests to queue. Multiple instances behind a load balancer is the standard fix.

### Architectural decisions to make early vs. defer

**Make early:**

- **Database selection.** Switching from H2 to PostgreSQL is easier before production data exists. Doing it later requires data migration.
- **API pagination pattern.** Adding pagination after clients depend on unpaginated responses is a breaking change. Design the pagination response format now, even if you don't enforce limits yet.
- **Authentication strategy.** The dashboard ticket says "use existing customer email as identifier (no login system yet)." Deciding between session-based auth, JWT tokens, or OAuth early avoids retrofitting security into every endpoint later.

**Defer:**

- **Caching.** Don't add Redis or application-level caching until you have performance data showing it's needed. Premature caching adds complexity and cache invalidation bugs.
- **Microservice decomposition.** Don't split the order service into separate services (order service, notification service, customer service) until the monolith can't scale. The current codebase is small enough for one service.
- **Message queues.** Don't add Kafka or RabbitMQ for notifications until synchronous email sending is proven to be a bottleneck. Start with the simple approach (call email service directly) and add async processing when you have data showing it's needed.
