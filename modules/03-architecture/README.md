# Module 3: Architecture Decisions

Practice evaluating technical trade-offs without needing to write code.

## Exercise 1: Trade-off Analysis

The order service currently uses an in-memory H2 database (see `app/src/main/resources/application.properties`). The team proposes switching to PostgreSQL for production.

Fill out this trade-off table:

| Factor               | H2 (current) | PostgreSQL (proposed) |
| -------------------- | ------------ | --------------------- |
| Setup complexity     |              |                       |
| Data persistence     |              |                       |
| Team familiarity     |              |                       |
| Production readiness |              |                       |
| Cost                 |              |                       |

What questions would you ask the team before approving this change? Which of these questions maps to "what's the simplest thing that could work?"

## Exercise 2: Reversibility Assessment

Review these proposed changes for the order service. Rate each as easy, moderate, or hard to reverse:

1. Adding a new REST endpoint (`GET /api/orders/summary`)
2. Changing the `Order` entity to split `customerName` into `firstName` and `lastName`
3. Replacing the Spring Boot backend with a Node.js Express server
4. Adding a `notes` field to the `Order` entity
5. Switching from REST to GraphQL for the API

For the hard-to-reverse items, what would you want the team to validate before proceeding?

## Exercise 3: Scalability Questions

Read `backlog/features/005-customer-dashboard.md`. The feature includes real-time order tracking, order history with filtering, and account management.

Ask the "10x users" question: if the order service goes from 100 to 1,000 concurrent users, which parts of this feature would break first? What architectural decisions would you push the team to make early vs. defer?
