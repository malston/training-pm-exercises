# Order Management Service

A Spring Boot order management API used for product manager training exercises.

## Commands

- `cd app && ./mvnw spring-boot:run` -- start the API on port 8080
- `cd app && ./mvnw test` -- run tests

## Project Conventions

### Backend (Java/Spring Boot)

- REST endpoints under `src/main/java/com/example/orderservice/controller/`
- Service layer under `src/main/java/com/example/orderservice/service/`
- JPA entities under `src/main/java/com/example/orderservice/model/`
- Tests mirror the source structure under `src/test/java/`
- Use constructor injection, not field injection
- Return `ResponseEntity` from controller methods
- H2 in-memory database for development

### Backlog

- Feature requests in `backlog/features/` as numbered markdown files
- Bug reports in `backlog/bugs/`
- Each ticket follows the template in `templates/ticket-template.md`
