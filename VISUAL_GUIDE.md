# Stock Checker Application - Visual Guide

This document provides visual representations and diagrams for the Stock Checker application architecture and workflows.

---

## System Architecture Diagram

```
┌────────────────────────────────────────────────────────────────────┐
│                     Stock Checker Web Application                   │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │                    Client / API Consumer                    │   │
│  │                   (curl, Postman, Browser)                  │   │
│  └──────────────────────┬─────────────────────────────────────┘   │
│                         │ HTTP/JSON                                 │
│                         ▼                                            │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │              Controller Layer (REST API)                    │   │
│  │                                                              │   │
│  │  ┌──────────────────────────────────────────────────┐      │   │
│  │  │           StockController                         │      │   │
│  │  │  • GET /api/stocks                               │      │   │
│  │  │  • GET /api/stocks/{symbol}                      │      │   │
│  │  │  • PUT /api/stocks/{symbol}/price                │      │   │
│  │  └──────────────────────────────────────────────────┘      │   │
│  │                                                              │   │
│  │  Responsibilities:                                           │   │
│  │  • HTTP Request/Response handling                           │   │
│  │  • Input validation                                         │   │
│  │  • DTO transformation                                       │   │
│  │  • Status code management                                   │   │
│  └──────────────────────┬───────────────────────────────────────   │
│                         │                                            │
│                         ▼                                            │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │              Service Layer (Business Logic)                 │   │
│  │                                                              │   │
│  │  ┌──────────────────────────────────────────────────┐      │   │
│  │  │        StockService (Interface)                   │      │   │
│  │  └──────────────────────────────────────────────────┘      │   │
│  │                         ▲                                    │   │
│  │                         │                                    │   │
│  │  ┌──────────────────────────────────────────────────┐      │   │
│  │  │        StockServiceImpl                           │      │   │
│  │  │  • getStockQuote(symbol)                         │      │   │
│  │  │  • getAllStocks()                                │      │   │
│  │  │  • updateStockPrice(symbol, price)              │      │   │
│  │  └──────────────────────────────────────────────────┘      │   │
│  │                                                              │   │
│  │  Responsibilities:                                           │   │
│  │  • Business rules                                            │   │
│  │  • Data orchestration                                       │   │
│  │  • Exception handling                                       │   │
│  └──────────────────────┬───────────────────────────────────────   │
│                         │                                            │
│                         ▼                                            │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │           Repository Layer (Data Access)                    │   │
│  │                                                              │   │
│  │  ┌──────────────────────────────────────────────────┐      │   │
│  │  │           StockRepository                         │      │   │
│  │  │  • findBySymbol(symbol)                          │      │   │
│  │  │  • findAll()                                     │      │   │
│  │  │  • save(stock)                                   │      │   │
│  │  │  • exists(symbol)                                │      │   │
│  │  └──────────────────────────────────────────────────┘      │   │
│  │                                                              │   │
│  │  Responsibilities:                                           │   │
│  │  • CRUD operations                                          │   │
│  │  • Data retrieval                                           │   │
│  │  • Storage abstraction                                      │   │
│  └──────────────────────┬───────────────────────────────────────   │
│                         │                                            │
│                         ▼                                            │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │              Data Store (In-Memory)                         │   │
│  │                                                              │   │
│  │  ConcurrentHashMap<String, Stock>                           │   │
│  │                                                              │   │
│  │  Pre-populated with sample data:                            │   │
│  │  • AAPL - Apple Inc.                                        │   │
│  │  • GOOGL - Alphabet Inc.                                    │   │
│  │  • MSFT - Microsoft Corporation                             │   │
│  │  • AMZN - Amazon.com Inc.                                   │   │
│  │  • TSLA - Tesla Inc.                                        │   │
│  └────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │              Cross-Cutting Concerns                          │   │
│  │                                                              │   │
│  │  • GlobalExceptionHandler                                   │   │
│  │  • Input Validation                                         │   │
│  │  • Logging                                                  │   │
│  └────────────────────────────────────────────────────────────┘   │
└────────────────────────────────────────────────────────────────────┘
```

---

## Data Model Diagram

```
┌────────────────────────────┐
│         Stock              │
├────────────────────────────┤
│ - symbol: String           │  (e.g., "AAPL")
│ - name: String             │  (e.g., "Apple Inc.")
│ - sector: String           │  (e.g., "Technology")
├────────────────────────────┤
│ + getSymbol(): String      │
│ + getName(): String        │
│ + getSector(): String      │
│ + setSymbol(String)        │
│ + setName(String)          │
│ + setSector(String)        │
└────────────────────────────┘
           │
           │ used to create
           ▼
┌────────────────────────────┐
│       StockQuote           │
├────────────────────────────┤
│ - symbol: String           │
│ - price: double            │
│ - change: double           │
│ - changePercent: double    │
│ - timestamp: LocalDateTime │
├────────────────────────────┤
│ + getSymbol(): String      │
│ + getPrice(): double       │
│ + getChange(): double      │
│ + getChangePercent(): dbl  │
│ + getTimestamp(): LDT      │
│ + setters...               │
└────────────────────────────┘
```

---

## API Request/Response Flow

```
Client Request Flow:
──────────────────

1. Client → HTTP Request
   GET /api/stocks/AAPL

2. StockController
   ├─ Validate input (symbol pattern)
   ├─ Call stockService.getStockQuote("AAPL")
   └─ Return ResponseEntity

3. StockServiceImpl
   ├─ Call stockRepository.findBySymbol("AAPL")
   ├─ Check if stock exists
   ├─ Calculate quote (price, change, etc.)
   └─ Return StockQuote

4. StockRepository
   ├─ Query ConcurrentHashMap
   └─ Return Optional<Stock>

5. Response Flow (Success)
   StockQuote → Service → Controller → HTTP Response
   
   {
     "symbol": "AAPL",
     "name": "Apple Inc.",
     "price": 150.25,
     "change": 2.50,
     "changePercent": 1.69,
     "timestamp": "2026-01-14T12:00:00Z"
   }

6. Response Flow (Error - Not Found)
   StockNotFoundException → GlobalExceptionHandler → HTTP 404
   
   {
     "timestamp": "2026-01-14T12:00:00Z",
     "status": 404,
     "error": "Not Found",
     "message": "Stock not found: XYZ",
     "path": "/api/stocks/XYZ"
   }
```

---

## Exception Handling Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    Exception Handling Flow                       │
└─────────────────────────────────────────────────────────────────┘

Scenario 1: Stock Not Found
────────────────────────────
Request: GET /api/stocks/INVALID
    ↓
StockController.getStockQuote("INVALID")
    ↓
StockService.getStockQuote("INVALID")
    ↓
Repository returns Optional.empty()
    ↓
Service throws StockNotFoundException
    ↓
GlobalExceptionHandler catches exception
    ↓
Returns ResponseEntity<ErrorResponse> (HTTP 404)
    ↓
Client receives structured error response


Scenario 2: Invalid Input
──────────────────────────
Request: PUT /api/stocks/AAPL/price?price=-10
    ↓
Controller validation annotations trigger
    ↓
@Min(0.01) constraint violation
    ↓
GlobalExceptionHandler catches MethodArgumentNotValidException
    ↓
Returns ResponseEntity<ErrorResponse> (HTTP 400)
    ↓
Client receives validation error response


Scenario 3: Unexpected Error
─────────────────────────────
Request: GET /api/stocks/AAPL
    ↓
Unexpected runtime exception in service
    ↓
GlobalExceptionHandler catches Exception
    ↓
Logs full stack trace (internal)
    ↓
Returns generic error message (external, HTTP 500)
    ↓
Client receives safe error response (no sensitive data)
```

---

## Compliance Documentation Structure

```
┌─────────────────────────────────────────────────────────────────┐
│              Compliance Documentation Framework                  │
└─────────────────────────────────────────────────────────────────┘

docs/compliance/
│
├─ README.md ─────────────────────┐
│  • Framework overview             │
│  • Purpose and usage              │
│  • Navigation guide               │
│  • Quick reference                │
│                                   │
├─ coding-standards.md ────────────┤
│  • Naming conventions             │
│  • Code formatting                │  Each document contains:
│  • Complexity limits              │  ────────────────────
│  • Comment requirements           │  • Specific rules
│                                   │  • Code examples
├─ security-requirements.md ───────┤  • Validation criteria
│  • Input validation               │  • Rationale
│  • Error handling                 │  • Pass/fail examples
│  • Secure coding practices        │  • References
│  • OWASP considerations           │
│                                   │
├─ api-design-guidelines.md ───────┤
│  • RESTful principles             │
│  • HTTP methods                   │
│  • Status codes                   │
│  • Request/response formats       │
│                                   │
├─ testing-requirements.md ────────┤
│  • Coverage requirements          │
│  • Test structure                 │
│  • Mock usage                     │
│  • Test naming                    │
│                                   │
├─ documentation-standards.md ─────┤
│  • JavaDoc requirements           │
│  • Inline comments                │
│  • README standards               │
│  • API documentation              │
│                                   │
└─ architecture-principles.md ─────┘
   • Layered architecture
   • SOLID principles
   • Design patterns
   • Dependency rules
```

---

## Code Review Integration Workflow

```
┌─────────────────────────────────────────────────────────────────┐
│              Code Review with Compliance Validation              │
└─────────────────────────────────────────────────────────────────┘

Step 1: Developer commits code
    ↓
Step 2: Code Review Agent triggered
    ↓
Step 3: Agent loads configuration
    ├─ Reads .github/code-review/review-config.json
    ├─ Identifies compliance document paths
    └─ Loads compliance-checklist.md
    ↓
Step 4: Agent analyzes code
    ├─ Parse source files
    ├─ Extract code patterns
    └─ Identify components
    ↓
Step 5: Validate against compliance
    ├─ Check coding standards
    │   └─ Naming, formatting, complexity
    ├─ Check security requirements
    │   └─ Input validation, error handling
    ├─ Check API design
    │   └─ REST principles, status codes
    ├─ Check testing requirements
    │   └─ Coverage, test structure
    ├─ Check documentation
    │   └─ JavaDoc, comments
    └─ Check architecture
        └─ Layer dependencies, patterns
    ↓
Step 6: Generate compliance report
    ├─ List violations by severity
    ├─ Link to specific compliance rules
    ├─ Provide code snippets
    ├─ Suggest fixes
    └─ Calculate compliance score
    ↓
Step 7: Return review results
    └─ Detailed summary with findings
    ↓
Step 8: Developer addresses feedback
    └─ Fix violations, re-submit
```

---

## Implementation Timeline

```
Project Timeline (25-28 hours)
═══════════════════════════════

Phase 1: Foundation [1h]
├─ [30m] Maven POM setup
├─ [15m] Main application class
└─ [15m] Configuration files
    ↓
Phase 2: Compliance Docs [5.5h]
├─ [30m] Documentation structure
├─ [1h]  Coding standards
├─ [1h]  Security requirements
├─ [45m] API design guidelines
├─ [45m] Testing requirements
├─ [30m] Documentation standards
└─ [1h]  Architecture principles
    ↓
Phase 3: Code Review Config [2.5h]
├─ [10m] Directory structure
├─ [30m] Review configuration
├─ [1h]  Compliance checklist
└─ [45m] Review guidelines
    ↓
Phase 4: Application Code [5.5h]
├─ [45m] Model classes
├─ [45m] Repository layer
├─ [1h]  Service layer
├─ [20m] Custom exceptions
├─ [45m] Exception handler
└─ [1.5h] REST controller
    ↓
Phase 5: Testing & Docs [7h]
├─ [1.5h] Service unit tests
├─ [2h]   Controller integration tests
├─ [20m]  Application context test
├─ [1.5h] JavaDoc completion
├─ [1h]   Main README update
└─ [45m]  API documentation
    ↓
Phase 6: Validation [4h]
├─ [30m] Maven build verification
├─ [20m] Application startup test
├─ [45m] Endpoint testing
├─ [30m] Compliance doc validation
├─ [1.5h] Final code review
└─ [1h]  Demo package preparation
    ↓
    ✓ COMPLETE
```

---

## Technology Stack Visualization

```
┌─────────────────────────────────────────────────────────────────┐
│                     Technology Stack                             │
└─────────────────────────────────────────────────────────────────┘

Application Layer
┌───────────────────────────────────────────────────────┐
│  Spring Boot 3.2.x                                    │
│  ┌─────────────────────────────────────────────────┐ │
│  │  Spring Web (REST)                              │ │
│  │  Spring Core (DI/IoC)                           │ │
│  │  Spring Boot Starter                            │ │
│  └─────────────────────────────────────────────────┘ │
└───────────────────────────────────────────────────────┘

Programming Language
┌───────────────────────────────────────────────────────┐
│  Java 17 LTS                                          │
│  • Records                                            │
│  • Pattern Matching                                   │
│  • Text Blocks                                        │
│  • Sealed Classes                                     │
└───────────────────────────────────────────────────────┘

Build & Dependencies
┌───────────────────────────────────────────────────────┐
│  Maven 3.9.x                                          │
│  • Dependency Management                              │
│  • Build Lifecycle                                    │
│  • Plugin Execution                                   │
└───────────────────────────────────────────────────────┘

Testing
┌───────────────────────────────────────────────────────┐
│  JUnit 5                                              │
│  Spring Boot Test                                     │
│  MockMvc                                              │
│  Mockito                                              │
│  JaCoCo (Coverage)                                    │
└───────────────────────────────────────────────────────┘

Quality Tools (Recommended)
┌───────────────────────────────────────────────────────┐
│  Checkstyle     → Coding standards                    │
│  PMD            → Code quality                        │
│  SpotBugs       → Bug detection                       │
│  SonarQube      → Comprehensive analysis              │
│  ArchUnit       → Architecture testing                │
└───────────────────────────────────────────────────────┘

Data Storage
┌───────────────────────────────────────────────────────┐
│  ConcurrentHashMap (In-Memory)                        │
│  • Thread-safe                                        │
│  • Fast access                                        │
│  • Simple for demo                                    │
└───────────────────────────────────────────────────────┘
```

---

## Compliance Validation Matrix

```
┌─────────────────────────────────────────────────────────────────┐
│            Compliance Validation Checklist Matrix                │
└─────────────────────────────────────────────────────────────────┘

Category         │ Rule   │ Severity │ Automated │ Manual
─────────────────┼────────┼──────────┼───────────┼────────
Coding Standards │        │          │           │
  Naming         │ CS-001 │ High     │    ✓      │   -
  Formatting     │ CS-002 │ Medium   │    ✓      │   -
  Complexity     │ CS-003 │ Medium   │    ✓      │   -
  Comments       │ CS-004 │ Low      │    ✓      │   ✓
─────────────────┼────────┼──────────┼───────────┼────────
Security         │        │          │           │
  Input Valid.   │ SEC-001│ Critical │    ✓      │   ✓
  Error Handling │ SEC-002│ Critical │    ✓      │   ✓
  Secure Coding  │ SEC-003│ High     │    ✓      │   ✓
─────────────────┼────────┼──────────┼───────────┼────────
API Design       │        │          │           │
  REST Naming    │ API-001│ High     │    ✓      │   -
  Status Codes   │ API-002│ High     │    ✓      │   ✓
  Response Format│ API-003│ High     │    ✓      │   -
─────────────────┼────────┼──────────┼───────────┼────────
Testing          │        │          │           │
  Coverage       │TEST-001│ High     │    ✓      │   -
  Test Structure │TEST-002│ Medium   │    ✓      │   ✓
  Test Naming    │TEST-003│ Low      │    ✓      │   -
─────────────────┼────────┼──────────┼───────────┼────────
Documentation    │        │          │           │
  JavaDoc        │ DOC-001│ High     │    ✓      │   ✓
  Comments       │ DOC-002│ Medium   │    -      │   ✓
  README         │ DOC-003│ Medium   │    -      │   ✓
─────────────────┼────────┼──────────┼───────────┼────────
Architecture     │        │          │           │
  Dependencies   │ARCH-001│ Critical │    ✓      │   ✓
  Layering       │ARCH-002│ Critical │    ✓      │   ✓
  Patterns       │ARCH-003│ High     │    -      │   ✓

Legend:
  ✓ = Validation performed
  - = Not applicable or not required
```

---

## Success Metrics Dashboard

```
┌─────────────────────────────────────────────────────────────────┐
│                   Project Success Metrics                        │
└─────────────────────────────────────────────────────────────────┘

Technical Metrics
═════════════════
Build Status              ⏳ Pending    Target: ✓ Pass
Test Coverage             ⏳ Pending    Target: ≥ 80%
Code Quality Score        ⏳ Pending    Target: ≥ 85/100
Security Vulnerabilities  ⏳ Pending    Target: 0 Critical
API Endpoints             ⏳ Pending    Target: 3/3 Working

Documentation Metrics
═════════════════════
Compliance Docs          ✓ Complete    Target: 6/6 Docs
Code Review Config       ✓ Complete    Target: 3/3 Files
README Quality           ✓ Complete    Target: Comprehensive
API Documentation        ⏳ Pending    Target: All endpoints
JavaDoc Coverage         ⏳ Pending    Target: 100% public APIs

Quality Metrics
═══════════════
Coding Standards         ⏳ Pending    Target: 100% Compliant
Security Standards       ⏳ Pending    Target: 100% Compliant
API Design Standards     ⏳ Pending    Target: 100% Compliant
Testing Standards        ⏳ Pending    Target: ≥ 80% Coverage
Architecture Compliance  ⏳ Pending    Target: 0 Violations

Demo Readiness
══════════════
Setup Time               ⏳ Pending    Target: < 5 minutes
Ease of Understanding    ⏳ Pending    Target: Clear & Simple
Professional Quality     ⏳ Pending    Target: Production-ready
Value Demonstration      ⏳ Pending    Target: Clear ROI

Overall Progress: Planning ✓ | Implementation ⏳ | Testing ⏳ | Demo ⏳
```

---

## Component Interaction Sequence

```
Sequence Diagram: Get Stock Quote
══════════════════════════════════

Client          Controller       Service         Repository      Data Store
  │                 │              │                 │               │
  ├─GET /api/stocks/AAPL────────►│                 │               │
  │                 │              │                 │               │
  │                 ├─Validate     │                 │               │
  │                 │  Input       │                 │               │
  │                 │              │                 │               │
  │                 ├─getStockQuote("AAPL")────────►│               │
  │                 │              │                 │               │
  │                 │              ├─findBySymbol("AAPL")──────────►│
  │                 │              │                 │               │
  │                 │              │                 ├─HashMap.get() │
  │                 │              │                 │               │
  │                 │              │                 │◄──Stock object│
  │                 │              │                 │               │
  │                 │              │◄──Optional<Stock>──────────────┤
  │                 │              │                 │               │
  │                 │              ├─Calculate Quote │               │
  │                 │              │  (price, change) │               │
  │                 │              │                 │               │
  │                 │◄──StockQuote object─────────┤               │
  │                 │              │                 │               │
  │                 ├─Transform    │                 │               │
  │                 │  to JSON     │                 │               │
  │                 │              │                 │               │
  │◄────200 OK + JSON──────────────┤                 │               │
  │  {quote data}   │              │                 │               │
```

---

## Demo Walkthrough Steps

```
┌─────────────────────────────────────────────────────────────────┐
│                   Demo Walkthrough Guide                         │
└─────────────────────────────────────────────────────────────────┘

Step 1: Introduction (2 minutes)
────────────────────────────────
□ Explain project purpose
□ Overview of technology stack
□ Highlight key demo features

Step 2: Project Structure (3 minutes)
──────────────────────────────────────
□ Show repository structure
□ Explain layered architecture
□ Point out key directories
  • src/main/java - Application code
  • docs/compliance - Standards
  • .github/code-review - Review config

Step 3: Compliance Documentation (5 minutes)
─────────────────────────────────────────────
□ Navigate to docs/compliance/
□ Open coding-standards.md
  • Show rule structure
  • Highlight code examples
□ Open security-requirements.md
  • Demonstrate input validation rules
□ Explain machine-readable format
□ Show linking between documents

Step 4: Code Review Integration (5 minutes)
────────────────────────────────────────────
□ Open .github/code-review/review-config.json
  • Show compliance doc references
  • Explain severity levels
□ Open compliance-checklist.md
  • Show automated check items
  • Demonstrate pass/fail criteria
□ Explain review workflow

Step 5: Application Code (7 minutes)
─────────────────────────────────────
□ Show StockController
  • REST endpoints
  • Input validation
  • Follows API guidelines
□ Show StockService
  • Business logic
  • Exception handling
□ Show StockRepository
  • Data access
  • In-memory storage
□ Demonstrate compliance adherence

Step 6: Testing (3 minutes)
────────────────────────────
□ Show unit tests
□ Show integration tests
□ Run tests: mvn test
□ Show coverage report

Step 7: Build & Run (3 minutes)
────────────────────────────────
□ Build: mvn clean package
□ Run: mvn spring-boot:run
□ Show startup logs
□ Application ready message

Step 8: API Testing (5 minutes)
────────────────────────────────
□ GET /api/stocks - List all stocks
□ GET /api/stocks/AAPL - Get specific quote
□ PUT /api/stocks/AAPL/price - Update price
□ Test error case (invalid symbol)
□ Show error handling

Step 9: Value Demonstration (5 minutes)
────────────────────────────────────────
□ Summarize benefits:
  • Centralized compliance
  • Version-controlled standards
  • Automated validation
  • Professional SDLC
□ Discuss scalability
□ Q&A

Total Time: ~35-40 minutes
```

---

**Document Version**: 1.0  
**Last Updated**: 2026-01-14  
**Purpose**: Visual reference for architecture, flows, and demo walkthrough
