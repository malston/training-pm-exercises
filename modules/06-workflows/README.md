# Module 6: Working with Developer Workflows

Practice reviewing PRs, matching implementations to tickets, and spotting scope creep.

## Setup

This module uses three branches that simulate open pull requests. Each implements a different backlog ticket with a different quality of execution.

Branches:

- `feature/order-search` -- implements ticket `backlog/features/001-order-search.md`
- `feature/order-notifications` -- implements ticket `backlog/features/003-order-notifications.md`
- `feature/bulk-status` -- implements ticket `backlog/features/004-bulk-status-update.md`

## Exercise 1: Review a Clean PR

Check out the diff for the order-search branch:

```bash
git diff main...feature/order-search
```

Read the corresponding ticket (`backlog/features/001-order-search.md`), then ask Claude:

> "Compare this diff to the acceptance criteria in ticket 001-order-search.md. Does the implementation fully satisfy each criterion? Are there any gaps?"

This PR should be straightforward -- the implementation matches the ticket. Note what a good PR-to-ticket match looks like.

## Exercise 2: Spot Scope Creep

Check out the diff for the notifications branch:

```bash
git diff main...feature/order-notifications
```

Read the corresponding ticket (`backlog/features/003-order-notifications.md`), then ask Claude:

> "Compare this diff to the acceptance criteria in ticket 003-order-notifications.md. Does the implementation match the ticket scope, or has the developer added features beyond what was requested?"

Identify:

- What was requested in the ticket?
- What was actually implemented?
- Which additions were not in scope?
- How would you handle this in a PR review comment?

## Exercise 3: Missing Acceptance Criteria

Check out the diff for the bulk-status branch:

```bash
git diff main...feature/bulk-status
```

Read the corresponding ticket (`backlog/features/004-bulk-status-update.md`), then ask Claude:

> "This PR implements bulk status update, but the ticket has no acceptance criteria. What questions would you ask before approving this PR? What edge cases should be tested?"

Practice writing the acceptance criteria that should have existed before development started. Consider:

- What happens if some IDs are invalid?
- What happens if the list is empty?
- Is there a size limit?
- What response format does the caller expect?
- Are there status transitions that should be blocked?
