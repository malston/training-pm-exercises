# Training: Product Manager Exercises

Exercise materials for the [Product Manager Learning Path](https://malston.github.io/claude-code-wiki/training/product-manager-path/). Practice technical literacy skills alongside developers using Claude Code.

## Project Structure

```
training-pm-modules/
├── backlog/              # Mock product backlog
│   ├── features/         # Feature requests (mix of well and poorly written)
│   ├── bugs/             # Bug reports
│   └── improvements/     # Enhancement requests
├── app/                  # Order management service (Java/Spring Boot)
├── modules/            # Exercise instructions per module
├── templates/            # Reusable templates (acceptance criteria, decomposition)
└── CLAUDE.md             # Project conventions for Claude Code
```

## Prerequisites

- Java 21+
- Maven 3.9+
- Claude Code CLI
- GitHub CLI (for PR exercises)

## Getting Started

**Browse the backlog:**

Look through the `backlog/` directory to see the product backlog. Some tickets are well-written with testable acceptance criteria. Others are vague and need improvement.

**Run the application:**

```bash
cd app
./mvnw spring-boot:run
```

The order service API starts at `http://localhost:8080`.

## Exercises by Module

| Module                   | Directory                        | Focus                                |
| ------------------------ | -------------------------------- | ------------------------------------ |
| 1. How Claude Code Works | `modules/01-claude-code-basics/` | Setting expectations                 |
| 2. Design Principles     | `modules/02-design-principles/`  | Recognizing good design              |
| 3. Architecture          | `modules/03-architecture/`       | Evaluating trade-offs                |
| 4. Coding Standards      | `modules/04-coding-standards/`   | Why conventions matter               |
| 5. TDD                   | `modules/05-tdd/`                | Writing testable acceptance criteria |
| 6. Workflows             | `modules/06-workflows/`          | PR review, feature decomposition     |

Each exercise directory has a `README.md` with instructions.

## Solutions

This branch (`solutions`) contains worked solutions for every exercise. Each module directory has a `SOLUTIONS.md` explaining the approach and key decisions.

**Attempt the exercises first.** Switch to the `main` branch to work through exercises without seeing answers:

```bash
git checkout main
```

When you're ready to check your work, switch back:

```bash
git checkout solutions
```
