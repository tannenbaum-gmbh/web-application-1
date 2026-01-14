# Stock Checker Application - Architecture Overview

## Quick Reference

This document provides a high-level architecture overview for the Stock Checker Java web application demo.

---

## Application Purpose

A simple banking-related web application for checking stock prices, designed to demonstrate:
- Modern Java development practices
- Integrated code compliance documentation
- Automated code review processes
- Future SDLC best practices

---

## Technology Decisions

### Core Stack
- **Java 17 LTS**: Modern Java with long-term support
- **Spring Boot 3.2.x**: Industry-standard framework for rapid development
- **Maven**: Build automation and dependency management
- **JUnit 5**: Modern testing framework

### Why These Choices?
1. **Enterprise Standard**: Spring Boot is widely adopted in banking/finance
2. **Simplicity**: Minimal configuration, maximum productivity
3. **Demo Friendly**: Quick to start, easy to explain
4. **Future Proof**: LTS versions ensure longevity

---

## Architecture Pattern: Layered Architecture

```
┌─────────────────────────────────────┐
│   REST API Layer (Controller)       │  ← HTTP Requests/Responses
├─────────────────────────────────────┤
│   Business Logic Layer (Service)    │  ← Business Rules
├─────────────────────────────────────┤
│   Data Access Layer (Repository)    │  ← Data Operations
├─────────────────────────────────────┤
│   In-Memory Storage (HashMap)       │  ← Simple Data Store
└─────────────────────────────────────┘
```

### Layer Responsibilities

#### Controller Layer
- **Purpose**: HTTP request/response handling
- **Components**: `StockController`
- **Responsibilities**:
  - Route HTTP requests to appropriate services
  - Validate input data
  - Transform DTOs to/from domain models
  - Return appropriate HTTP status codes

#### Service Layer
- **Purpose**: Business logic and orchestration
- **Components**: `StockService`, `StockServiceImpl`
- **Responsibilities**:
  - Implement business rules
  - Coordinate between repositories
  - Handle business exceptions
  - Transaction management (if needed)

#### Repository Layer
- **Purpose**: Data access abstraction
- **Components**: `StockRepository`
- **Responsibilities**:
  - CRUD operations
  - Data retrieval and storage
  - Abstraction over data source

#### Model Layer
- **Purpose**: Domain objects
- **Components**: `Stock`, `StockQuote`
- **Responsibilities**:
  - Represent business entities
  - Encapsulate data
  - Define object behavior

---

## API Design

### RESTful Endpoints

```
GET    /api/stocks              → List all stocks
GET    /api/stocks/{symbol}     → Get specific stock quote
PUT    /api/stocks/{symbol}/price → Update stock price
```

### Response Format

**Success Response:**
```json
{
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "price": 150.25,
  "change": 2.50,
  "changePercent": 1.69,
  "timestamp": "2026-01-14T12:00:00Z"
}
```

**Error Response:**
```json
{
  "timestamp": "2026-01-14T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Stock not found: XYZ",
  "path": "/api/stocks/XYZ"
}
```

---

## Data Model

### Stock Entity
```java
Stock {
  - symbol: String       (e.g., "AAPL")
  - name: String         (e.g., "Apple Inc.")
  - sector: String       (e.g., "Technology")
}
```

### Stock Quote DTO
```java
StockQuote {
  - symbol: String
  - price: double
  - change: double
  - changePercent: double
  - timestamp: LocalDateTime
}
```

---

## Compliance Documentation Framework

### Location: `docs/compliance/`

### Document Structure

```
docs/compliance/
├── README.md                      ← Framework overview
├── coding-standards.md            ← Java conventions
├── security-requirements.md       ← Security best practices
├── api-design-guidelines.md       ← REST API standards
├── testing-requirements.md        ← Test coverage & quality
├── documentation-standards.md     ← JavaDoc & comments
└── architecture-principles.md     ← Design decisions
```

### Key Compliance Areas

1. **Coding Standards**
   - Naming conventions
   - Formatting rules
   - Complexity limits
   - Code organization

2. **Security Requirements**
   - Input validation
   - Error handling (no sensitive data leakage)
   - Secure defaults
   - OWASP Top 10 awareness

3. **API Design Guidelines**
   - RESTful principles
   - HTTP method usage
   - Status codes
   - Error formats
   - Versioning strategy

4. **Testing Requirements**
   - Minimum coverage (80%)
   - Test structure (AAA pattern)
   - Unit vs integration tests
   - Mock usage

5. **Documentation Standards**
   - JavaDoc for public APIs
   - Inline comments when needed
   - README completeness
   - API documentation

6. **Architecture Principles**
   - Layered architecture
   - Dependency direction
   - SOLID principles
   - Design patterns

---

## Code Review Integration

### Location: `.github/code-review/`

### Components

1. **review-config.json**
   - Configuration for review agents
   - Compliance documentation paths
   - Review focus areas
   - Severity levels

2. **compliance-checklist.md**
   - Automated checklist items
   - Code patterns to validate
   - Pass/fail criteria
   - Links to compliance docs

3. **review-guidelines.md**
   - Process overview
   - How to interpret compliance docs
   - Priority levels
   - Exception handling

### How It Works

```
Code Changes
     ↓
Review Agent Triggered
     ↓
Loads review-config.json
     ↓
References Compliance Docs
     ↓
Checks Against Checklist
     ↓
Generates Review Summary
     ↓
Reports Findings
```

---

## Security Considerations

### Input Validation
- All user inputs validated at controller layer
- Stock symbols validated against pattern
- Price values must be positive numbers

### Error Handling
- Global exception handler prevents information leakage
- Generic error messages to external users
- Detailed logging for internal debugging
- No stack traces exposed in production

### Data Protection
- No sensitive data in this demo
- In future: implement proper authentication/authorization
- Follow principle of least privilege

---

## Testing Strategy

### Test Pyramid

```
        ┌──────────┐
       │    E2E     │     ← Few (manual for demo)
      ├────────────┤
     │ Integration  │     ← Some (controller tests)
    ├──────────────┤
   │   Unit Tests   │     ← Many (service, repository)
  └────────────────┘
```

### Coverage Goals
- **Unit Tests**: 80%+ code coverage
- **Integration Tests**: All endpoints covered
- **Smoke Tests**: Application context loads

---

## Build & Deployment

### Build Process
```bash
mvn clean install
```

### Run Application
```bash
java -jar target/stock-checker-1.0.0.jar
```

### Quick Start
```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Test
curl http://localhost:8080/api/stocks
```

---

## Demo Walkthrough

### 1. Project Structure Demo
- Show layered architecture in code
- Explain separation of concerns
- Highlight dependency injection

### 2. Compliance Documentation Demo
- Navigate docs/compliance/
- Show coding standards examples
- Explain security requirements
- Demonstrate how standards apply to code

### 3. Code Review Integration Demo
- Show .github/code-review/ configuration
- Explain how agents access compliance docs
- Walk through compliance checklist
- Demonstrate automated validation

### 4. Application Demo
- Start the application
- Call REST endpoints
- Show error handling
- Demonstrate validation

### 5. Testing Demo
- Run unit tests
- Show coverage report
- Explain test structure
- Highlight integration tests

---

## Scalability Considerations

### For Future Enhancement (Beyond Demo Scope)

1. **Data Persistence**
   - Replace in-memory storage with database (H2, PostgreSQL)
   - Add Spring Data JPA

2. **Real-Time Data**
   - Integrate with actual stock API
   - Add caching layer (Redis)
   - WebSocket for live updates

3. **Authentication/Authorization**
   - Spring Security
   - JWT tokens
   - Role-based access control

4. **Monitoring & Observability**
   - Spring Actuator
   - Prometheus metrics
   - Distributed tracing

5. **Containerization**
   - Docker container
   - Kubernetes deployment
   - CI/CD pipeline

---

## Key Differentiators

### What Makes This Demo Special

1. **Compliance-First Approach**
   - Centralized, version-controlled compliance docs
   - Machine-readable standards
   - Integrated with code review

2. **Automated Validation**
   - Code review agents can access standards
   - Automated compliance checking
   - Consistent enforcement

3. **Clear Documentation**
   - Every decision documented
   - Standards with examples
   - Easy to understand and follow

4. **Professional Quality**
   - Production-ready patterns
   - Best practices demonstrated
   - Enterprise-grade structure

5. **Demo Optimized**
   - Simple business logic
   - Easy to explain
   - Quick to set up
   - Clear value proposition

---

## Success Criteria

### Technical Success
- ✅ Application builds without errors
- ✅ All tests pass
- ✅ API endpoints functional
- ✅ Code follows all standards

### Documentation Success
- ✅ Compliance docs complete
- ✅ Code review config in place
- ✅ All links work
- ✅ Examples provided

### Demo Success
- ✅ Easy to understand
- ✅ Quick to set up
- ✅ Clear value demonstrated
- ✅ Professional quality

---

## Resources & References

### Spring Boot
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Building REST Services](https://spring.io/guides/tutorials/rest/)

### Best Practices
- [RESTful API Design](https://restfulapi.net/)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Clean Code Principles](https://www.goodreads.com/book/show/3735293-clean-code)

### Testing
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)

---

**Document Version**: 1.0  
**Last Updated**: 2026-01-14  
**Prepared For**: Implementation Team & Customer Workshop
