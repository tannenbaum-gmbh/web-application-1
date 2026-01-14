# Compliance Checking Guide

## Overview
This guide explains how the automated code review process validates code against the centralized compliance documentation in this repository.

---

## How It Works

### 1. Centralized Compliance Documentation
**Location**: `COMPLIANCE_RULES.md` (repository root)

This single, comprehensive document serves as the **single source of truth** for all compliance requirements. It contains:

- **10 Major Compliance Categories**:
  1. Security Requirements
  2. Code Quality Standards
  3. Architecture and Design Patterns
  4. Testing Requirements
  5. Error Handling and Validation
  6. Performance and Resource Management
  7. Configuration and Properties
  8. Build and Deployment
  9. Version Control and Git Practices
  10. Code Review Checklist

- **Detailed Rules with Examples**: Each category contains specific MANDATORY rules with code examples
- **Validation Criteria**: Clear acceptance/rejection criteria
- **Code Review Checklists**: Pre-submission and reviewer checklists

### 2. Code Review Agent Access
When a code review is requested, the code review agent:

1. **Reads COMPLIANCE_RULES.md** - Loads all compliance rules and requirements
2. **Analyzes Code Changes** - Reviews all modified files in the pull request
3. **Cross-References Rules** - Validates code against each applicable compliance rule
4. **Generates Report** - Creates detailed feedback with:
   - Specific violations found
   - References to rule sections in COMPLIANCE_RULES.md
   - Severity levels (MANDATORY violations block merge)
   - Code examples and recommendations

### 3. Validation Process

```
┌─────────────────────────────────────────────────────────────┐
│                    Pull Request Created                      │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Code Review Agent Triggered                     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│          Agent Reads COMPLIANCE_RULES.md                     │
│   • Loads all 10 compliance categories                      │
│   • Parses MANDATORY rules                                  │
│   • Loads code review checklists                            │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│           Agent Analyzes Code Changes                        │
│   • Reviews each modified file                              │
│   • Checks security requirements                            │
│   • Validates code quality standards                        │
│   • Verifies architecture patterns                          │
│   • Checks testing requirements                             │
│   • Validates error handling                                │
│   • Reviews documentation                                   │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│          Agent Generates Compliance Report                   │
│   • Lists violations with severity                          │
│   • References specific COMPLIANCE_RULES.md sections        │
│   • Provides recommendations                                │
│   • Suggests fixes with examples                            │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│        Developer Reviews Feedback & Makes Fixes              │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│          Re-Review Process (if needed)                       │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│       All Compliance Checks Pass → Merge Approved            │
└─────────────────────────────────────────────────────────────┘
```

---

## Compliance Categories Checked

### 1. Security Requirements ✓
**What's Checked**:
- Input validation (all REST parameters validated)
- Exception handling (proper GlobalExceptionHandler)
- Logging (no sensitive data logged)
- Secrets management (no hardcoded credentials)

**Example Validation**:
```java
// ✅ COMPLIANT - Input validation present
@PostMapping
public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) {
    // @Valid ensures validation
}

// ❌ NON-COMPLIANT - Missing validation
@PostMapping
public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
    // No @Valid annotation
}
```

### 2. Code Quality Standards ✓
**What's Checked**:
- Naming conventions (PascalCase classes, camelCase methods)
- Method size (< 100 lines)
- Documentation (JavaDoc for public APIs)
- Single Responsibility Principle

**Example Validation**:
```java
// ✅ COMPLIANT - Proper naming and documentation
/**
 * Retrieves stock by symbol.
 * @param symbol the stock symbol
 * @return Stock object
 */
public Stock getStockBySymbol(String symbol) {
    return repository.findBySymbol(symbol);
}

// ❌ NON-COMPLIANT - Poor naming, no documentation
public Stock get(String s) {
    return repo.find(s);
}
```

### 3. Architecture and Design Patterns ✓
**What's Checked**:
- Layered architecture (Controller → Service → Repository)
- Dependency injection (constructor injection)
- RESTful design (proper HTTP methods and status codes)
- No business logic in controllers

**Example Validation**:
```java
// ✅ COMPLIANT - Proper layering
@RestController
public class StockController {
    private final StockService service;
    
    public StockController(StockService service) {
        this.service = service;
    }
    
    @GetMapping("/api/stocks/{symbol}")
    public ResponseEntity<Stock> getStock(@PathVariable String symbol) {
        return ResponseEntity.ok(service.getStockBySymbol(symbol));
    }
}

// ❌ NON-COMPLIANT - Business logic in controller
@RestController
public class StockController {
    @GetMapping("/api/stocks/{symbol}")
    public ResponseEntity<Stock> getStock(@PathVariable String symbol) {
        // Business logic should be in service layer
        Stock stock = database.query("SELECT * FROM stocks WHERE symbol = ?", symbol);
        stock.calculateMetrics();
        return ResponseEntity.ok(stock);
    }
}
```

### 4. Testing Requirements ✓
**What's Checked**:
- Test coverage (80%+ for services)
- Unit tests for business logic
- Integration tests for REST APIs
- Test independence and quality

**Example Validation**:
```java
// ✅ COMPLIANT - Descriptive test name, proper structure
@Test
void getStockBySymbol_WhenStockExists_ReturnsStock() {
    // Arrange
    String symbol = "AAPL";
    Stock expected = new Stock(symbol, "Apple", 150.0);
    when(repository.findBySymbol(symbol)).thenReturn(Optional.of(expected));
    
    // Act
    Stock actual = service.getStockBySymbol(symbol);
    
    // Assert
    assertEquals(expected, actual);
}

// ❌ NON-COMPLIANT - Poor test name, unclear purpose
@Test
void test1() {
    Stock s = service.getStockBySymbol("AAPL");
    assertNotNull(s);
}
```

### 5. Error Handling and Validation ✓
**What's Checked**:
- Custom exceptions used appropriately
- Validation rules enforced
- Meaningful error messages
- No exceptions swallowed silently

### 6. Performance and Resource Management ✓
**What's Checked**:
- Resources properly closed
- Thread-safe collections for concurrent access
- No resource leaks
- Pagination for large result sets

### 7. Configuration and Properties ✓
**What's Checked**:
- No hardcoded environment-specific values
- application.properties used correctly
- No secrets in configuration files
- Proper use of @Value or @ConfigurationProperties

### 8. Build and Deployment ✓
**What's Checked**:
- Clean build configuration (pom.xml)
- Pinned dependency versions
- No unused dependencies
- No compiler warnings

### 9. Version Control and Git Practices ✓
**What's Checked**:
- Clear commit messages
- Appropriate branch usage
- No secrets in version control
- Proper .gitignore configuration

### 10. Code Review Checklist ✓
**What's Checked**:
- All pre-submission checklist items completed
- Tests pass
- Documentation updated
- Security reviewed

---

## Demo Workflow

### Step 1: Make Code Changes
Developer creates or modifies code in a feature branch.

### Step 2: Submit Pull Request
Developer creates a PR with a description of changes and references to the requirements.

### Step 3: Automated Code Review
Code review agent automatically:
1. Reads `COMPLIANCE_RULES.md`
2. Analyzes all changed files
3. Validates against compliance rules
4. Posts detailed review comments

### Step 4: Review Feedback
Developer receives feedback such as:
```
❌ COMPLIANCE VIOLATION: Security Requirements (Section 1.1)

File: StockController.java, Line 45
Issue: Missing input validation on REST endpoint parameter

Rule: "All external inputs MUST be validated using Jakarta Validation annotations"
Reference: COMPLIANCE_RULES.md, Section 1.1 - Input Validation

Current Code:
  @PostMapping
  public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {

Recommended Fix:
  @PostMapping
  public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) {
  
Add @Valid annotation to enforce validation rules defined in the Stock model.
```

### Step 5: Address Feedback
Developer fixes the issues and commits changes.

### Step 6: Re-Review
Agent automatically re-reviews the updated code.

### Step 7: Approval
Once all compliance checks pass, the PR is approved for merge.

---

## Benefits of This Approach

### 1. Single Source of Truth ✓
- One document (`COMPLIANCE_RULES.md`) contains all rules
- Version-controlled with the code
- Changes to rules are tracked in Git history
- Easy to reference and update

### 2. Automated Validation ✓
- Consistent enforcement across all PRs
- No manual checklist review needed
- Immediate feedback to developers
- Reduces review time and errors

### 3. Educational ✓
- Developers learn compliance requirements
- Examples in compliance doc guide implementation
- Feedback includes rule references
- Builds compliance knowledge over time

### 4. Audit Trail ✓
- All compliance validations logged
- Review comments tied to specific rules
- Changes to compliance rules tracked
- Easy to demonstrate compliance to auditors

### 5. Scalability ✓
- Same process works for any size team
- No additional setup needed for new developers
- Compliance rules apply consistently
- Easy to add new rules or update existing ones

---

## Maintaining Compliance Documentation

### When to Update COMPLIANCE_RULES.md

1. **New Security Requirements**: Add rules to Section 1
2. **New Architecture Patterns**: Update Section 3
3. **Changed Technology Stack**: Update relevant sections
4. **Regulatory Changes**: Add new compliance requirements
5. **Lessons Learned**: Incorporate feedback from production issues

### Update Process

1. Create a PR to modify `COMPLIANCE_RULES.md`
2. Document the reason for the change
3. Update affected code examples
4. Get approval from team lead
5. Merge and communicate changes to team

### Version Control

- Document version and last update date in COMPLIANCE_RULES.md
- Tag major compliance updates (e.g., `compliance-v2.0`)
- Maintain changelog of compliance rule changes
- Review compliance doc quarterly

---

## Example Code Review Session

### Initial PR
```
PR #123: Add stock price history tracking

Changes:
- Added PriceHistory model
- Updated StockService with history tracking
- Added new REST endpoint: GET /api/stocks/{symbol}/history
```

### Automated Review Feedback
```
Code Review Report - PR #123

✅ PASSED: Security Requirements
  - Input validation present on all endpoints
  - No sensitive data in logs
  - Exception handling implemented

✅ PASSED: Code Quality Standards
  - All public methods have JavaDoc
  - Naming conventions followed
  - Methods under 50 lines

❌ FAILED: Architecture and Design Patterns
  Issue: Business logic in controller method
  File: StockController.java, Line 89-95
  Rule: COMPLIANCE_RULES.md, Section 3.1 - Layered Architecture
  Fix: Move history aggregation logic to service layer

❌ FAILED: Testing Requirements
  Issue: No unit tests for PriceHistory class
  Rule: COMPLIANCE_RULES.md, Section 4.1 - Test Coverage
  Fix: Add unit tests for PriceHistory model validation

✅ PASSED: Error Handling
  - Custom exceptions used appropriately
  - Global exception handler catches all errors

Overall: 2 MANDATORY violations - Changes Required
```

### After Fixes
```
Code Review Report - PR #123 (Re-review)

✅ PASSED: All compliance checks
  - Architecture violations fixed
  - Test coverage now at 85%
  - All MANDATORY requirements met

Status: APPROVED for merge
```

---

## Troubleshooting

### Q: Code review agent didn't find an obvious violation
**A**: The agent may not catch everything on first pass. Manual review is still valuable. Consider updating COMPLIANCE_RULES.md with more specific examples if the pattern recurs.

### Q: False positive in code review
**A**: If you believe a violation is incorrect, comment on the PR explaining why. The team lead can override if justified.

### Q: How to handle exceptions to compliance rules
**A**: Document the exception with justification in code comments. Team lead must approve exceptions. Consider if COMPLIANCE_RULES.md needs updating.

### Q: Compliance rules conflict with each other
**A**: Raise this with the team lead. Update COMPLIANCE_RULES.md to clarify or resolve the conflict. Compliance doc should be self-consistent.

---

## Integration with CI/CD

### Automated Workflow
```yaml
# Example GitHub Actions workflow
name: Compliance Check

on: [pull_request]

jobs:
  compliance:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run Code Review
        run: |
          # Code review agent runs here
          # Reads COMPLIANCE_RULES.md
          # Posts review comments
      - name: Block on Violations
        run: |
          # Prevent merge if MANDATORY violations found
```

### Quality Gates
- **Compiler**: Must build without errors
- **Tests**: All tests must pass
- **Coverage**: Minimum 80% for service layer
- **Compliance**: All MANDATORY rules must pass
- **Security**: No high/critical vulnerabilities

---

## Key Takeaways

1. ✅ **COMPLIANCE_RULES.md is the single source of truth** for all compliance requirements
2. ✅ **Code review agent reads this document** to validate every PR
3. ✅ **Automated validation** ensures consistent enforcement
4. ✅ **Detailed feedback** with rule references helps developers learn
5. ✅ **Version-controlled compliance** enables audit trails and change tracking
6. ✅ **Scalable process** works for teams of any size
7. ✅ **Educational approach** builds compliance culture over time

---

## Next Steps

### For Developers
1. Read [COMPLIANCE_RULES.md](COMPLIANCE_RULES.md) thoroughly
2. Reference it while writing code
3. Use the code review checklist before submitting PRs
4. Learn from code review feedback

### For Team Leads
1. Review and approve COMPLIANCE_RULES.md updates
2. Monitor compliance violations trends
3. Update rules based on lessons learned
4. Communicate compliance changes to team

### For the Organization
1. Adopt this pattern across projects
2. Customize COMPLIANCE_RULES.md per project needs
3. Share compliance updates across teams
4. Measure compliance improvement over time

---

## Additional Resources

- [COMPLIANCE_RULES.md](COMPLIANCE_RULES.md) - Complete compliance documentation
- [README.md](README.md) - Project overview and getting started
- [ARCHITECTURE_OVERVIEW.md](ARCHITECTURE_OVERVIEW.md) - Technical architecture guide
- Spring Boot Documentation - https://spring.io/projects/spring-boot
- Java Best Practices - https://www.oracle.com/java/technologies/javase/codeconventions-introduction.html

---

## Document Information
- **Version**: 1.0
- **Last Updated**: 2026-01-14
- **Purpose**: Guide for compliance checking with automated code review
- **Audience**: Developers, Team Leads, Workshop Attendees

---

**This document demonstrates how centralized compliance documentation integrates with automated code review to ensure consistent code quality and regulatory compliance.**
