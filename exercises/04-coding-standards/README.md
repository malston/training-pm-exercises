# Module 4: Coding Standards

Understand why conventions matter and what to look for when teams adopt them.

## Exercise 1: Read the CLAUDE.md

Open `CLAUDE.md` in the project root. This file tells Claude Code how to write code for this project.

Answer:

- What conventions does it enforce? (formatting, naming, patterns)
- If a developer asked Claude to "add a new endpoint," would Claude know the project's conventions from this file?
- What's missing from the CLAUDE.md that would help Claude produce more consistent code?

Write 2-3 rules you'd add to the CLAUDE.md based on what you see in the existing codebase.

## Exercise 2: Consistency Check

Compare the three PR branches against the patterns in the main codebase:

```bash
git diff main..feature/order-search --stat
git diff main..feature/order-notifications --stat
git diff main..feature/bulk-status --stat
```

For each branch, check:

- Does the code follow the same patterns as existing endpoints?
- Are new methods named consistently with existing ones?
- Does the PR add tests? (Is that consistent with what main has?)

Which branch is the most consistent with the existing codebase? Which deviates the most?

## Exercise 3: Standards as Investment

Your team is debating whether to spend a sprint writing CLAUDE.md files and coding standards documentation instead of building features.

Make the business case using the order service as an example:

- How many endpoints does the app have? How consistent are they?
- If a developer asks Claude to "add pagination to all endpoints," would Claude produce consistent code without standards documentation?
- What's the cost of inconsistency over 6 months of development?
