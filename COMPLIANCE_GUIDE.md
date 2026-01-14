# Compliance Documentation Best Practices Guide

## Purpose
This guide provides recommendations for creating effective compliance documentation that code review agents can access and validate against.

---

## Key Principles

### 1. Machine-Readable Format
- Use **Markdown** for human and machine readability
- Include **structured sections** with clear headers
- Use **consistent formatting** throughout
- Add **code examples** in fenced code blocks
- Include **links** between related documents

### 2. Specific and Measurable
- Define **concrete rules**, not vague guidelines
- Provide **measurable criteria** (e.g., "80% test coverage" not "good coverage")
- Include **pass/fail examples**
- Use **objective language**

### 3. Contextual and Practical
- Explain **why** rules exist (rationale)
- Provide **real code examples**
- Include **common violations** and fixes
- Link to **external standards** when applicable

### 4. Maintainable and Versioned
- Keep documents **focused** (single responsibility)
- Use **version control** (Git)
- Include **last updated date**
- Track **change history**

---

## Document Structure Template

### Standard Document Format

```markdown
# [Document Title]

**Version**: 1.0  
**Last Updated**: YYYY-MM-DD  
**Status**: Active  
**Applies To**: [Scope - e.g., "All Java code" or "REST APIs"]

## Overview
[Brief description of what this document covers and why it matters]

## Table of Contents
- [Section 1]
- [Section 2]
- [etc.]

## [Section Name]

### Rule [ID]: [Rule Title]
**Severity**: Critical | High | Medium | Low  
**Category**: [e.g., Naming | Security | Performance]

**Description**:
[Clear explanation of the rule]

**Rationale**:
[Why this rule exists]

**Requirements**:
- [Specific requirement 1]
- [Specific requirement 2]

**Examples**:

✅ **Compliant Code**:
```java
// Good example
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

❌ **Non-Compliant Code**:
```java
// Bad example
public class user_service {
    public UserRepository repo;
}
```

**Validation**:
- [How to check compliance - manual or automated]

**References**:
- [Link to related rules or external standards]

---

[Repeat for each rule]

## Summary Checklist
- [ ] [Quick checklist item 1]
- [ ] [Quick checklist item 2]

## Related Documents
- [Link to related compliance doc 1]
- [Link to related compliance doc 2]

## Appendix
[Additional resources, tools, configurations]
```

---

## Recommended Compliance Documents

### 1. Coding Standards (coding-standards.md)

**Key Sections**:
- Naming Conventions
  - Classes (PascalCase)
  - Methods (camelCase)
  - Constants (UPPER_SNAKE_CASE)
  - Packages (lowercase)
- Code Formatting
  - Indentation (spaces vs tabs)
  - Line length limits
  - Brace style
- Code Organization
  - Import ordering
  - Class member ordering
  - Package structure
- Complexity Limits
  - Cyclomatic complexity (< 10)
  - Method length (< 50 lines)
  - Class length (< 500 lines)
  - Parameter count (< 5)
- Comments and Documentation
  - When to comment
  - JavaDoc requirements
  - TODO format

**Example Rule**:
```markdown
### Rule CS-001: Class Naming Convention
**Severity**: High  
**Category**: Naming

**Description**:
All class names must use PascalCase and be descriptive nouns.

**Requirements**:
- Start with uppercase letter
- Use mixed case (PascalCase)
- No underscores or hyphens
- Be descriptive and meaningful
- Avoid abbreviations unless widely known

**Examples**:
✅ Compliant: `UserService`, `StockRepository`, `OrderController`
❌ Non-Compliant: `userService`, `stock_repo`, `OC`, `Handler1`

**Validation**:
Regex pattern: `^[A-Z][a-zA-Z0-9]*$`
```

---

### 2. Security Requirements (security-requirements.md)

**Key Sections**:
- Input Validation
  - Whitelist validation
  - Sanitization requirements
  - Type checking
- Authentication/Authorization
  - Authentication patterns
  - Authorization checks
  - Session management
- Error Handling
  - No sensitive data in errors
  - Consistent error messages
  - Logging vs user messages
- Data Protection
  - Encryption requirements
  - Secure storage
  - Data minimization
- Secure Coding Practices
  - SQL injection prevention
  - XSS prevention
  - CSRF protection
  - Dependency security
- OWASP Top 10 Considerations

**Example Rule**:
```markdown
### Rule SEC-001: Input Validation Required
**Severity**: Critical  
**Category**: Security - Input Validation

**Description**:
All user inputs must be validated before processing.

**Requirements**:
- Validate at entry point (controller layer)
- Use whitelist validation when possible
- Validate type, length, format, and range
- Reject invalid input with clear error message
- Never trust user input

**Examples**:
✅ Compliant:
```java
@PutMapping("/stocks/{symbol}/price")
public ResponseEntity<?> updatePrice(
    @PathVariable @Pattern(regexp = "^[A-Z]{1,5}$") String symbol,
    @RequestParam @Min(0.01) @Max(999999.99) double price) {
    // Process validated input
}
```

❌ Non-Compliant:
```java
@PutMapping("/stocks/{symbol}/price")
public ResponseEntity<?> updatePrice(
    @PathVariable String symbol,  // No validation
    @RequestParam double price) {  // No validation
    // Direct processing - unsafe!
}
```

**Validation**:
- All controller parameters have validation annotations
- No raw String/primitive processing without checks
```

---

### 3. API Design Guidelines (api-design-guidelines.md)

**Key Sections**:
- RESTful Principles
  - Resource naming
  - HTTP methods usage
  - Stateless design
- URL Structure
  - Naming conventions
  - Hierarchy
  - Plural vs singular
- HTTP Status Codes
  - Success codes (200, 201, 204)
  - Client error codes (400, 404)
  - Server error codes (500)
- Request/Response Format
  - JSON structure
  - Field naming (camelCase)
  - Date/time format (ISO 8601)
- Error Handling
  - Error response structure
  - Error codes
  - Error messages
- Versioning Strategy
  - URL versioning
  - Header versioning
- Pagination and Filtering
  - Query parameters
  - Response metadata

**Example Rule**:
```markdown
### Rule API-001: Resource Naming Convention
**Severity**: High  
**Category**: API Design - Naming

**Description**:
REST resource names must use plural nouns in lowercase.

**Requirements**:
- Use plural form of nouns
- Use lowercase
- Separate words with hyphens
- Avoid verbs in resource names
- Keep URLs simple and intuitive

**Examples**:
✅ Compliant:
- `/api/stocks`
- `/api/stocks/{symbol}`
- `/api/user-profiles`

❌ Non-Compliant:
- `/api/stock` (singular)
- `/api/getStocks` (verb)
- `/api/Stocks` (uppercase)
- `/api/user_profiles` (underscore)

**Validation**:
- All @RequestMapping paths use plural nouns
- No verbs in URL paths
```

---

### 4. Testing Requirements (testing-requirements.md)

**Key Sections**:
- Test Coverage
  - Minimum coverage percentage
  - What to measure (line, branch)
  - Exclusions
- Test Types
  - Unit tests
  - Integration tests
  - End-to-end tests
- Test Structure
  - Arrange-Act-Assert pattern
  - Given-When-Then pattern
  - Test naming conventions
- Test Best Practices
  - Test independence
  - Test data management
  - Mock usage guidelines
  - Assertion practices
- Test Performance
  - Execution time limits
  - Parallelization

**Example Rule**:
```markdown
### Rule TEST-001: Minimum Code Coverage
**Severity**: High  
**Category**: Testing - Coverage

**Description**:
All new code must maintain minimum 80% line coverage.

**Requirements**:
- 80% minimum line coverage for new code
- 70% minimum branch coverage
- Exclude only infrastructure code (config, DTOs)
- No coverage exemptions without justification

**Validation**:
```bash
mvn clean test jacoco:report
# Check target/site/jacoco/index.html
# Ensure line coverage >= 80%
```

**Measurement**:
Use JaCoCo Maven plugin with enforcement:
```xml
<execution>
    <id>check</id>
    <goals><goal>check</goal></goals>
    <configuration>
        <rules>
            <rule>
                <element>BUNDLE</element>
                <limits>
                    <limit>
                        <counter>LINE</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.80</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>
</execution>
```
```

---

### 5. Documentation Standards (documentation-standards.md)

**Key Sections**:
- JavaDoc Requirements
  - What to document
  - Required tags
  - Format and style
- Inline Comments
  - When to use
  - What to avoid
  - Format
- README Standards
  - Required sections
  - Format
- API Documentation
  - Endpoint documentation
  - Examples
  - Tools (Swagger/OpenAPI)
- Architecture Documentation
  - ADR format
  - Diagram requirements

**Example Rule**:
```markdown
### Rule DOC-001: Public API JavaDoc Required
**Severity**: High  
**Category**: Documentation

**Description**:
All public classes, methods, and fields must have JavaDoc.

**Requirements**:
- All public classes have class-level JavaDoc
- All public methods have method JavaDoc
- Include @param for all parameters
- Include @return for non-void methods
- Include @throws for declared exceptions
- Describe purpose, not implementation

**Examples**:
✅ Compliant:
```java
/**
 * Service for managing stock data and quotes.
 * Provides methods to retrieve and update stock information.
 *
 * @since 1.0
 */
public interface StockService {
    
    /**
     * Retrieves the current quote for a stock.
     *
     * @param symbol the stock symbol (e.g., "AAPL")
     * @return the current stock quote
     * @throws StockNotFoundException if stock not found
     */
    StockQuote getStockQuote(String symbol);
}
```

❌ Non-Compliant:
```java
// No JavaDoc
public interface StockService {
    StockQuote getStockQuote(String symbol);
}
```

**Validation**:
- Maven JavaDoc plugin with strict mode
- Build fails on missing JavaDoc
```

---

### 6. Architecture Principles (architecture-principles.md)

**Key Sections**:
- Layered Architecture
  - Layer definitions
  - Dependency rules
  - Communication patterns
- SOLID Principles
  - Single Responsibility
  - Open/Closed
  - Liskov Substitution
  - Interface Segregation
  - Dependency Inversion
- Design Patterns
  - When to use
  - Common patterns
  - Anti-patterns to avoid
- Dependency Management
  - Injection patterns
  - Dependency direction
  - Module boundaries
- Package Organization
  - Package by layer vs feature
  - Naming conventions

**Example Rule**:
```markdown
### Rule ARCH-001: Dependency Direction Rule
**Severity**: Critical  
**Category**: Architecture - Dependencies

**Description**:
Dependencies must flow from outer layers to inner layers.

**Requirements**:
- Controllers depend on Services (not vice versa)
- Services depend on Repositories (not vice versa)
- No circular dependencies
- Core business logic has no framework dependencies

**Dependency Flow**:
```
Controller → Service → Repository → Data Store
    ↓           ↓          ↓
  (HTTP)    (Business)  (Data)
```

**Examples**:
✅ Compliant:
```java
@RestController
public class StockController {
    private final StockService stockService;
    
    // Controller depends on Service ✓
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }
}
```

❌ Non-Compliant:
```java
@Service
public class StockServiceImpl {
    private final StockController controller;  // ✗ Wrong direction!
    
    public StockServiceImpl(StockController controller) {
        this.controller = controller;
    }
}
```

**Validation**:
- Use ArchUnit for automated architecture testing
- Build fails on dependency violations
```

---

## Code Review Configuration

### review-config.json Structure

```json
{
  "version": "1.0",
  "complianceDocumentation": {
    "location": "docs/compliance",
    "documents": [
      {
        "name": "Coding Standards",
        "path": "docs/compliance/coding-standards.md",
        "priority": "high",
        "categories": ["naming", "formatting", "complexity"]
      },
      {
        "name": "Security Requirements",
        "path": "docs/compliance/security-requirements.md",
        "priority": "critical",
        "categories": ["security", "validation", "errors"]
      },
      {
        "name": "API Design Guidelines",
        "path": "docs/compliance/api-design-guidelines.md",
        "priority": "high",
        "categories": ["api", "rest", "endpoints"]
      },
      {
        "name": "Testing Requirements",
        "path": "docs/compliance/testing-requirements.md",
        "priority": "high",
        "categories": ["testing", "coverage", "quality"]
      },
      {
        "name": "Documentation Standards",
        "path": "docs/compliance/documentation-standards.md",
        "priority": "medium",
        "categories": ["documentation", "javadoc", "comments"]
      },
      {
        "name": "Architecture Principles",
        "path": "docs/compliance/architecture-principles.md",
        "priority": "critical",
        "categories": ["architecture", "design", "patterns"]
      }
    ]
  },
  "reviewFocusAreas": [
    "security vulnerabilities",
    "code quality",
    "compliance violations",
    "best practice adherence",
    "test coverage",
    "documentation completeness"
  ],
  "severityLevels": {
    "critical": {
      "description": "Must fix before merge",
      "examples": ["security vulnerabilities", "architecture violations"]
    },
    "high": {
      "description": "Should fix before merge",
      "examples": ["missing tests", "poor naming", "missing documentation"]
    },
    "medium": {
      "description": "Fix in follow-up",
      "examples": ["minor style issues", "optimization opportunities"]
    },
    "low": {
      "description": "Nice to have",
      "examples": ["suggestions", "alternative approaches"]
    }
  },
  "autoCheckItems": [
    "naming conventions",
    "code formatting",
    "test coverage thresholds",
    "security patterns",
    "documentation presence"
  ],
  "manualReviewItems": [
    "business logic correctness",
    "architecture appropriateness",
    "complex security scenarios"
  ]
}
```

---

## Compliance Checklist Format

### compliance-checklist.md Structure

```markdown
# Code Review Compliance Checklist

**Version**: 1.0  
**Purpose**: Automated validation checklist for code review agents

---

## How to Use This Checklist

1. Review agent loads this checklist
2. For each item, agent checks code against criteria
3. Agent reports pass/fail with evidence
4. Links to relevant compliance documentation provided
5. Detailed summary generated

---

## Checklist Items

### Category: Coding Standards

#### Item CS-001: Class Naming Convention
**Reference**: [docs/compliance/coding-standards.md#rule-cs-001]  
**Severity**: High  
**Check**: All classes use PascalCase naming  
**Pattern**: `^[A-Z][a-zA-Z0-9]*$`  
**Files**: `**/*.java`  
**Pass Criteria**: 100% of classes match pattern  
**Fail Example**: `class myClass` or `class my_class`

#### Item CS-002: Method Naming Convention
**Reference**: [docs/compliance/coding-standards.md#rule-cs-002]  
**Severity**: High  
**Check**: All methods use camelCase naming  
**Pattern**: `^[a-z][a-zA-Z0-9]*$`  
**Files**: `**/*.java`  
**Pass Criteria**: 100% of methods match pattern  
**Fail Example**: `void MyMethod()` or `void my_method()`

#### Item CS-003: Cyclomatic Complexity Limit
**Reference**: [docs/compliance/coding-standards.md#rule-cs-003]  
**Severity**: Medium  
**Check**: Methods have complexity < 10  
**Tool**: PMD or SonarQube  
**Files**: `**/*.java`  
**Pass Criteria**: No methods exceed complexity 10  
**Fail Example**: Method with 15 decision points

---

### Category: Security

#### Item SEC-001: Input Validation Present
**Reference**: [docs/compliance/security-requirements.md#rule-sec-001]  
**Severity**: Critical  
**Check**: Controller parameters have validation annotations  
**Pattern**: Look for `@Valid`, `@Pattern`, `@Min`, `@Max`, etc.  
**Files**: `**/controller/*.java`  
**Pass Criteria**: All user inputs validated  
**Fail Example**: `@PathVariable String param` without validation

#### Item SEC-002: Error Messages Safe
**Reference**: [docs/compliance/security-requirements.md#rule-sec-002]  
**Severity**: Critical  
**Check**: No sensitive data in exception messages  
**Pattern**: No stack traces or internal details in API responses  
**Files**: `**/exception/*.java`  
**Pass Criteria**: Generic error messages only  
**Fail Example**: Exposing SQL queries or file paths in errors

---

### Category: API Design

#### Item API-001: REST Resource Naming
**Reference**: [docs/compliance/api-design-guidelines.md#rule-api-001]  
**Severity**: High  
**Check**: REST paths use plural nouns  
**Pattern**: `/api/[plural-noun]`  
**Files**: `**/controller/*.java`  
**Pass Criteria**: All @RequestMapping paths compliant  
**Fail Example**: `/api/stock` or `/api/getStocks`

#### Item API-002: HTTP Status Codes
**Reference**: [docs/compliance/api-design-guidelines.md#rule-api-002]  
**Severity**: High  
**Check**: Appropriate status codes used  
**Pattern**: 200/201 for success, 400 for validation, 404 for not found, 500 for errors  
**Files**: `**/controller/*.java`  
**Pass Criteria**: Correct ResponseEntity status codes  
**Fail Example**: Returning 200 for all responses

---

### Category: Testing

#### Item TEST-001: Minimum Coverage
**Reference**: [docs/compliance/testing-requirements.md#rule-test-001]  
**Severity**: High  
**Check**: Code coverage >= 80%  
**Tool**: JaCoCo  
**Files**: All source files  
**Pass Criteria**: Line coverage >= 80%, Branch coverage >= 70%  
**Fail Example**: Coverage < 80%

#### Item TEST-002: Test Naming Convention
**Reference**: [docs/compliance/testing-requirements.md#rule-test-002]  
**Severity**: Medium  
**Check**: Test methods descriptive and follow pattern  
**Pattern**: `should[ExpectedBehavior]When[StateUnderTest]`  
**Files**: `**/test/**/*.java`  
**Pass Criteria**: All test methods clearly named  
**Fail Example**: `testMethod1()`

---

### Category: Documentation

#### Item DOC-001: Public API JavaDoc
**Reference**: [docs/compliance/documentation-standards.md#rule-doc-001]  
**Severity**: High  
**Check**: All public APIs have JavaDoc  
**Pattern**: `/** ... */` before public class/method  
**Files**: `**/*.java`  
**Pass Criteria**: 100% public APIs documented  
**Fail Example**: Public method without JavaDoc

---

### Category: Architecture

#### Item ARCH-001: Layer Dependency Direction
**Reference**: [docs/compliance/architecture-principles.md#rule-arch-001]  
**Severity**: Critical  
**Check**: Dependencies flow Controller → Service → Repository  
**Pattern**: No reverse dependencies  
**Files**: `**/*.java`  
**Pass Criteria**: No circular or reverse dependencies  
**Fail Example**: Service depending on Controller

#### Item ARCH-002: Proper Layer Annotations
**Reference**: [docs/compliance/architecture-principles.md#rule-arch-002]  
**Severity**: High  
**Check**: Classes in correct layers have correct annotations  
**Pattern**: Controllers have @RestController, Services have @Service, etc.  
**Files**: `**/*.java`  
**Pass Criteria**: All classes properly annotated  
**Fail Example**: Controller without @RestController

---

## Summary Template

After checking all items, generate summary:

```
# Code Review Summary

**Date**: [timestamp]  
**Reviewer**: [agent name]  
**Branch**: [branch name]  
**Commit**: [commit hash]

## Compliance Score: [X/Y] ([percentage]%)

### Critical Issues: [count]
[List with links to code and compliance docs]

### High Priority Issues: [count]
[List with links to code and compliance docs]

### Medium Priority Issues: [count]
[List with links to code and compliance docs]

### Low Priority Issues: [count]
[List with links to code and compliance docs]

## Category Breakdown
- Coding Standards: [X/Y passed]
- Security: [X/Y passed]
- API Design: [X/Y passed]
- Testing: [X/Y passed]
- Documentation: [X/Y passed]
- Architecture: [X/Y passed]

## Recommendations
1. [Priority action 1]
2. [Priority action 2]
3. [Priority action 3]

## Compliant Areas
- [What was done well]
- [What followed standards]

## Overall Assessment
[Pass/Fail with explanation]
```

---

## Best Practices for Code Review Agents

### 1. Load Configuration First
- Read review-config.json
- Load all compliance documents referenced
- Parse checklist items

### 2. Systematic Checking
- Process each checklist item sequentially
- Collect evidence (code snippets, metrics)
- Document findings with line numbers

### 3. Context-Aware Review
- Consider file type and purpose
- Apply appropriate rules for each layer
- Understand business context

### 4. Clear Reporting
- Link findings to specific compliance rules
- Provide code snippets showing violations
- Suggest specific fixes
- Prioritize by severity

### 5. Constructive Feedback
- Explain why something is non-compliant
- Show compliant alternative
- Link to learning resources

---

## Tools Integration

### Recommended Tools
- **Checkstyle**: Coding standards enforcement
- **PMD**: Code quality and complexity
- **SpotBugs**: Bug pattern detection
- **SonarQube**: Comprehensive code analysis
- **JaCoCo**: Test coverage measurement
- **ArchUnit**: Architecture testing
- **OWASP Dependency-Check**: Security scanning

### Configuration
Place tool configurations in project root:
- `checkstyle.xml`
- `pmd.xml`
- `spotbugs-exclude.xml`
- `sonar-project.properties`

---

## Maintenance

### Regular Updates
- Review compliance docs quarterly
- Update examples as patterns evolve
- Add new rules as needed
- Archive deprecated rules

### Feedback Loop
- Collect feedback from code reviews
- Identify frequent violations
- Enhance documentation
- Improve automation

---

**Document Version**: 1.0  
**Last Updated**: 2026-01-14  
**Status**: Reference Guide
