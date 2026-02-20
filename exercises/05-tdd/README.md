# Module 5: Writing Testable Acceptance Criteria

Practice rewriting vague requirements into testable Given/When/Then format.

## Setup

Review the backlog tickets in `backlog/` before starting. Some are well-written, some are not.

## Exercise 1: Identify Vague Criteria

Read these tickets and identify which have testable acceptance criteria and which don't:

- `backlog/features/001-order-search.md` (well-written)
- `backlog/features/002-order-history-export.md` (vague)
- `backlog/bugs/002-missing-validation.md` (vague)
- `backlog/improvements/002-better-errors.md` (vague)

For each vague ticket, list what's missing: what format? What edge cases? What error scenarios? What does "better" mean?

## Exercise 2: Rewrite with Given/When/Then

Take the vague tickets from Exercise 1 and rewrite them with testable acceptance criteria. Use this format:

```
Given [a specific starting state],
When [a specific action happens],
Then [a specific, observable outcome].
```

Ask Claude to help evaluate your rewrites:

> "Here's a ticket I rewrote with Given/When/Then acceptance criteria. Are these testable? Could a developer write automated tests directly from these criteria? What edge cases am I missing?"

See `exercises/05-tdd/examples.md` for before/after examples.

## Exercise 3: Validate Against the Code

Pick a well-written ticket (`backlog/features/001-order-search.md`) and ask Claude:

> "Read this ticket and the OrderController code. Can you write tests that verify each acceptance criterion? If any criterion is ambiguous, tell me what's unclear."

This shows whether your acceptance criteria are precise enough for a developer (or AI) to implement without asking questions.
