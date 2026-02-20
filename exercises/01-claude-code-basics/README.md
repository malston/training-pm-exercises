# Module 1: How Claude Code Works

Understand what Claude Code does so you can set realistic expectations for your team.

## Exercise 1: Watch a Developer Session

Pair with a developer on your team (or use this repo) and observe a Claude Code session. Note:

- How does the developer frame requests?
- When does Claude ask for clarification vs. proceed?
- What triggers Claude to re-read files or run tests?
- How long does a typical task take from prompt to completion?

Write down 3 things that surprised you about how Claude Code works.

## Exercise 2: Spot the Context Limit

Open the order service codebase (`app/`) and imagine describing the entire project to someone in a single conversation. Consider:

- How many files would they need to read?
- What context would they lose if you could only tell them about 10 files at a time?
- How would you break a large feature into pieces that each fit in a single conversation?

Review `backlog/features/005-customer-dashboard.md` -- this feature is too large for one Claude Code session. List which parts you'd split into separate tasks.

## Exercise 3: Requirements Quality Matters

Compare these two backlog tickets:

- `backlog/features/001-order-search.md` (well-written)
- `backlog/features/002-order-history-export.md` (vague)

For each ticket, answer: if a developer pasted this into Claude Code, would Claude have enough information to implement it correctly? What's missing from the vague ticket?
