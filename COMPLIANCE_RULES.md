# Code Compliance Rules and Standards

## Overview
This document defines the mandatory compliance rules and coding standards for the Stock Checker application. All code must adhere to these rules before being merged. The code review process will validate against these requirements.

---

## 1. Security Requirements

### 1.1 Input Validation
**MANDATORY**: All external inputs (REST API parameters, request bodies) MUST be validated.
- Use Jakarta Validation annotations (@NotNull, @NotBlank, @Size, @Pattern, etc.)
- Validate at the controller/API boundary
- Return appropriate HTTP 400 Bad Request for validation failures
- Never trust user input

**Example:**
```java
@PostMapping
public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) {
    // Validation is enforced by @Valid
}
```

### 1.2 Exception Handling
**MANDATORY**: All exceptions MUST be properly handled.
- Use @RestControllerAdvice or @ControllerAdvice for global exception handling
- Never expose internal error details or stack traces to clients
- Log exceptions with appropriate severity levels
- Return standardized error responses

### 1.3 Logging
**MANDATORY**: Sensitive data MUST NOT be logged.
- Never log passwords, API keys, tokens, or PII
- Use appropriate log levels (ERROR, WARN, INFO, DEBUG, TRACE)
- Include correlation IDs for request tracking
- Log security-relevant events (authentication, authorization failures)

---

## 2. Code Quality Standards

### 2.1 Naming Conventions
**MANDATORY**: Follow Java naming conventions.
- Classes: PascalCase (e.g., `StockController`, `StockService`)
- Methods: camelCase (e.g., `getStockBySymbol`, `updatePrice`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_RETRY_ATTEMPTS`)
- Packages: lowercase, reverse domain notation (e.g., `com.demo.stockchecker`)
- Boolean variables: use "is", "has", "can" prefixes (e.g., `isValid`, `hasError`)

### 2.2 Method Design
**MANDATORY**: Methods MUST be focused and maintainable.
- Keep methods small (< 50 lines preferred, max 100 lines)
- Single Responsibility Principle - one method does one thing
- Maximum 4-5 parameters (use objects for more)
- Avoid deep nesting (max 3 levels)
- Use descriptive names that explain what the method does

### 2.3 Documentation
**MANDATORY**: Public APIs MUST be documented.
- All public classes need JavaDoc with description
- Public methods need JavaDoc with @param, @return, @throws
- REST endpoints need clear documentation (can use Swagger/OpenAPI)
- Document complex business logic
- Keep comments up-to-date with code changes

**Example:**
```java
/**
 * Retrieves stock information by symbol.
 *
 * @param symbol the stock symbol (e.g., "AAPL")
 * @return Stock object with current information
 * @throws StockNotFoundException if stock doesn't exist
 */
public Stock getStockBySymbol(String symbol) {
    // implementation
}
```

---

## 3. Architecture and Design Patterns

### 3.1 Layered Architecture
**MANDATORY**: Maintain clear separation of concerns.
- **Controller Layer**: Handle HTTP requests/responses, input validation
- **Service Layer**: Business logic, transaction boundaries
- **Repository/DAO Layer**: Data access logic
- No business logic in controllers
- No HTTP concerns in services
- Controllers should be thin, delegating to services

### 3.2 Dependency Injection
**MANDATORY**: Use Spring's dependency injection.
- Use constructor injection (preferred) or field injection
- Avoid manual object instantiation for managed beans
- Use @Service, @Repository, @Component annotations appropriately
- Define clear interfaces for services

**Example:**
```java
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    
    private final StockService stockService;
    
    // Constructor injection (preferred)
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
}
```

### 3.3 RESTful Design
**MANDATORY**: Follow REST principles for APIs.
- Use appropriate HTTP methods (GET, POST, PUT, DELETE, PATCH)
- Use plural nouns for resources (e.g., `/api/stocks`)
- Return appropriate status codes:
  - 200 OK for successful GET/PUT
  - 201 Created for successful POST
  - 204 No Content for successful DELETE
  - 400 Bad Request for validation errors
  - 404 Not Found for missing resources
  - 409 Conflict for duplicate resources
  - 500 Internal Server Error for unexpected errors
- Use consistent URL patterns
- Version your APIs (e.g., `/api/v1/stocks`)

---

## 4. Testing Requirements

### 4.1 Test Coverage
**MANDATORY**: All new code MUST have tests.
- Minimum 80% code coverage for services
- Unit tests for business logic
- Integration tests for REST APIs
- Test happy paths AND error cases
- Use descriptive test method names

### 4.2 Test Quality
**MANDATORY**: Tests MUST be reliable and maintainable.
- Tests should be independent (no order dependency)
- Use meaningful assertions
- Mock external dependencies
- Use @MockBean for Spring integration tests
- Clean up test data after tests
- Tests should run quickly (< 5 seconds for unit tests)

**Example:**
```java
@Test
void getStockBySymbol_WhenStockExists_ReturnsStock() {
    // Arrange
    String symbol = "AAPL";
    Stock expected = new Stock(symbol, "Apple Inc.", 150.0);
    when(stockService.getStockBySymbol(symbol)).thenReturn(expected);
    
    // Act
    Stock actual = stockService.getStockBySymbol(symbol);
    
    // Assert
    assertThat(actual).isEqualTo(expected);
}
```

### 4.3 Test Organization
**MANDATORY**: Follow test structure conventions.
- Mirror production code package structure in test
- One test class per production class
- Group related tests using @Nested classes
- Use @BeforeEach/@AfterEach for setup/teardown
- Keep test data in dedicated test resources

---

## 5. Error Handling and Validation

### 5.1 Custom Exceptions
**MANDATORY**: Use meaningful custom exceptions.
- Create domain-specific exceptions (e.g., `StockNotFoundException`)
- Extend appropriate base exception (RuntimeException for unchecked)
- Include meaningful error messages
- Don't swallow exceptions silently
- Use exception chaining when wrapping exceptions

### 5.2 Validation Rules
**MANDATORY**: Validate all business rules.
- Stock symbol: Required, 1-10 uppercase letters
- Stock name: Required, non-blank, max 200 characters
- Price: Required, positive number
- Percentage: Can be negative (for losses)
- Validate data consistency before persistence

---

## 6. Performance and Resource Management

### 6.1 Resource Management
**MANDATORY**: Properly manage resources.
- Close all I/O streams, connections, files
- Use try-with-resources for AutoCloseable
- Don't hold database connections longer than needed
- Limit collection sizes when appropriate
- Use pagination for large result sets

### 6.2 Concurrency
**MANDATORY**: Handle concurrent access safely.
- Use thread-safe collections when needed (ConcurrentHashMap)
- Synchronize access to shared mutable state
- Be aware of race conditions
- Document thread-safety expectations

---

## 7. Configuration and Properties

### 7.1 Externalized Configuration
**MANDATORY**: Use Spring Boot configuration properly.
- Use application.properties or application.yml
- Don't hardcode environment-specific values
- Use @Value or @ConfigurationProperties for injection
- Support different profiles (dev, test, prod)
- Document all configuration properties

### 7.2 Secrets Management
**MANDATORY**: Never commit secrets to version control.
- No passwords, API keys, or tokens in code
- No secrets in application.properties (use environment variables)
- Use Spring Boot's encrypted properties or external secret management
- Add secrets patterns to .gitignore

---

## 8. Build and Deployment

### 8.1 Build Configuration
**MANDATORY**: Maintain clean build configuration.
- Keep pom.xml or build.gradle organized
- Pin dependency versions (no LATEST or RELEASE)
- Remove unused dependencies
- Use dependency management for version consistency
- Document custom build steps

### 8.2 Code Quality Checks
**MANDATORY**: Code must pass quality gates.
- No compiler warnings (or documented suppressions with justification)
- No critical SonarQube issues
- No high/critical security vulnerabilities
- Code formatting must be consistent
- Run static analysis tools

---

## 9. Version Control and Git Practices

### 9.1 Commit Messages
**MANDATORY**: Write clear commit messages.
- Use imperative mood ("Add feature" not "Added feature")
- First line: concise summary (< 72 chars)
- Separate subject and body with blank line
- Explain what and why, not how
- Reference issue/ticket numbers

### 9.2 Branch Strategy
**MANDATORY**: Follow branching conventions.
- Use feature branches for development
- Keep branches short-lived (< 1 week)
- Rebase/merge from main regularly
- Delete branches after merging
- No commits directly to main/master

---

## 10. Code Review Checklist

### Before Submitting for Review:
- [ ] All tests pass locally
- [ ] Code follows all compliance rules in this document
- [ ] No security vulnerabilities introduced
- [ ] No sensitive data in code or logs
- [ ] All public methods have JavaDoc
- [ ] Test coverage meets requirements (80%+)
- [ ] No compiler warnings
- [ ] Code is self-explanatory or well-commented
- [ ] Error handling is comprehensive
- [ ] REST APIs follow RESTful conventions
- [ ] Input validation is implemented
- [ ] No hardcoded configuration values

### Reviewers Must Check:
- [ ] Business logic correctness
- [ ] Security implications
- [ ] Performance considerations
- [ ] Test quality and coverage
- [ ] Code maintainability
- [ ] Compliance with this document
- [ ] API design and usability
- [ ] Error handling completeness

---

## Compliance Validation

This document is the single source of truth for code compliance. During code review:

1. **Automated checks** will validate:
   - Test coverage thresholds
   - Code formatting
   - Security vulnerabilities
   - Build success

2. **Manual review** will validate:
   - Adherence to architecture patterns
   - Code quality and maintainability
   - Business logic correctness
   - Compliance with all rules in this document

3. **Violations** will result in:
   - Change requests in PR review
   - Blocking of merge until fixed
   - Documentation of exceptions (if approved)

---

## Document Version
- **Version**: 1.0
- **Last Updated**: 2026-01-14
- **Next Review**: Quarterly or as needed
- **Owner**: Development Team Lead

## Questions or Clarifications
Contact the development team lead or open a discussion in the project repository.
