# Module 2: Software Design Principles

Recognize good and bad design in your team's codebase and discussions.

## Exercise 1: Separation of Concerns

Read through `app/src/main/java/com/example/orderservice/service/OrderService.java` and `app/src/main/java/com/example/orderservice/controller/OrderController.java`.

Answer these questions:

- What does the controller handle? What does the service handle?
- Why are they separate files instead of one?
- If you needed to add email notifications when an order is cancelled, which file would change? Would you need a third file?

Now look at `backlog/features/003-order-notifications.md`. The ticket asks for email notifications. Check the `feature/order-notifications` branch -- the developer added email, SMS, and Slack. Which design principle does the scope creep violate?

## Exercise 2: YAGNI in the Backlog

Review the full backlog (`backlog/features/`, `backlog/bugs/`, `backlog/improvements/`). For each ticket, classify it:

- **Need now:** Solves a concrete problem users have today
- **Might need later:** Could be useful but no evidence of demand yet
- **Over-engineering:** Adds complexity without clear benefit

Which tickets would you defer? Which would you push back on entirely?

## Exercise 3: Naming Review

Look at the class and method names in the order service. For each, ask: does this name tell you what the code does without reading the implementation?

- `OrderService.cancelOrder()` -- clear or vague?
- `OrderController.updateOrderStatus()` -- does the name match what it does?
- `OrderStatus.PENDING` vs `OrderStatus.CONFIRMED` -- do the status names describe the business state?

If a developer named something `NewOrderService` or `ImprovedValidator`, what question would you ask?
