# Exercise: Feature Decomposition

## Goal

Break the Customer Dashboard feature (`backlog/features/005-customer-dashboard.md`) into independently verifiable tasks using the template in `templates/decomposition-template.md`.

## Instructions

1. Read the full feature spec in `backlog/features/005-customer-dashboard.md`

2. Ask Claude to help identify natural boundaries:

   > "Read the customer dashboard feature spec. What are the natural groupings of work? Which pieces can be built and shipped independently?"

3. Create your decomposition using the template. Aim for 6-10 tasks.

4. For each task, write acceptance criteria in Given/When/Then format.

5. Ask Claude to validate your decomposition:

   > "Here's my decomposition of the customer dashboard feature. For each task, can you confirm: (1) it's independently testable, (2) the acceptance criteria are specific enough to write tests from, and (3) the dependencies are correctly identified?"

## Evaluation Criteria

A good decomposition will:

- Have no task that takes more than 2 days
- Have each task produce something that works and can be demonstrated
- Have clear dependencies (task B needs task A, but task C is independent)
- Have acceptance criteria that a developer could implement without asking questions
- Cover all requirements from the original spec
- Not include tasks for requirements that are out of scope

## Common Mistakes

- Making tasks too large ("implement the dashboard" is not a task)
- Making tasks too small ("create the Order entity" when it already exists)
- Missing tasks (the spec mentions PDF receipts -- did your decomposition include it?)
- Circular dependencies (A needs B, B needs A)
- Vague acceptance criteria ("dashboard should work well")
