# Stock Checker - Java Web Application Demo

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9.x-red.svg)](https://maven.apache.org/)

A professional Java web application demo showcasing modern SDLC practices with integrated code compliance documentation and automated code review capabilities.

---

## 📋 Project Status

**Current Phase**: Implementation Complete ✓  
**Next Phase**: Ready for Demo  

---

## 🎯 Project Purpose

This demo application is designed for customer workshops to demonstrate:
- Modern Java web application development with Spring Boot
- Centralized, version-controlled code compliance documentation
- Integration of compliance standards with automated code review processes
- Professional software development lifecycle (SDLC) practices

---

## 🏗️ Application Overview

**Stock Checker** is a simple banking-related web application that provides REST API endpoints for checking stock prices and managing stock quotes.

### Technology Stack
- **Java 17 LTS** - Modern Java with long-term support
- **Spring Boot 3.2.x** - Industry-standard framework
- **Maven 3.9.x** - Build automation and dependency management
- **JUnit 5** - Modern testing framework
- **RESTful API** - HTTP/JSON endpoints
- **In-Memory Storage** - Simple HashMap-based data store (demo)

### Architecture
```
┌─────────────────────┐
│  REST Controller    │  ← /api/stocks endpoints
├─────────────────────┤
│  Service Layer      │  ← Business logic
├─────────────────────┤
│  Repository Layer   │  ← Data access
├─────────────────────┤
│  In-Memory Store    │  ← HashMap storage
└─────────────────────┘
```

---

## 📚 Documentation

### Core Documents
- **[README.md](README.md)** - This file, project overview and getting started guide
- **[COMPLIANCE_RULES.md](COMPLIANCE_RULES.md)** - **Centralized compliance rules and coding standards** ✓
- **[COMPLIANCE_CHECKING_GUIDE.md](COMPLIANCE_CHECKING_GUIDE.md)** - **Guide for compliance checking with code review agent** ✓
- **[PROJECT_PLAN.md](PROJECT_PLAN.md)** - Detailed implementation plan with 6 phases and 25+ tasks
- **[ARCHITECTURE_OVERVIEW.md](ARCHITECTURE_OVERVIEW.md)** - Comprehensive architecture and design guide
- **[COMPLIANCE_GUIDE.md](COMPLIANCE_GUIDE.md)** - Best practices for compliance documentation
- **[IMPLEMENTATION_HANDOFF.md](IMPLEMENTATION_HANDOFF.md)** - Handoff summary for implementation team

---

## 🔍 Key Features

### 1. Centralized Compliance Documentation ✓
**Location**: `COMPLIANCE_RULES.md` (root directory)

This single, comprehensive document contains all compliance rules:
- **Security Requirements** - Input validation, error handling, logging, secrets management
- **Code Quality Standards** - Naming conventions, method design, documentation
- **Architecture and Design Patterns** - Layered architecture, dependency injection, RESTful design
- **Testing Requirements** - Coverage thresholds, test quality, test organization
- **Error Handling and Validation** - Custom exceptions, validation rules
- **Performance and Resource Management** - Resource cleanup, concurrency
- **Configuration and Properties** - Externalized config, secrets management
- **Build and Deployment** - Build configuration, quality checks
- **Version Control and Git Practices** - Commit messages, branching strategy
- **Code Review Checklist** - Pre-submission and reviewer checklists

### 2. Code Review Integration ✓
**How it Works**: The code review agent accesses `COMPLIANCE_RULES.md` to:
- Validate code against all defined compliance rules
- Generate detailed compliance reports with rule references
- Check security, code quality, architecture, and testing requirements
- Provide actionable feedback linked to specific compliance sections

### 3. Professional Application Structure
```
src/main/java/com/demo/stockchecker/
├── StockCheckerApplication.java
├── controller/
│   └── StockController.java
├── service/
│   ├── StockService.java
│   └── StockServiceImpl.java
├── model/
│   ├── Stock.java
│   └── StockQuote.java
├── repository/
│   └── StockRepository.java
└── exception/
    ├── StockNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17 JDK
- Maven 3.9.x
- Git

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Test
```bash
mvn test
```

All 31 tests should pass:
- StockCheckerApplicationTests: 1 test
- StockServiceImplTest: 17 tests  
- StockControllerTest: 13 tests
- AuthControllerTest: 3 tests
- JwtAuthenticationIntegrationTest: 5 tests

### Authentication
The application uses JWT (JSON Web Token) for authentication. All `/api/stocks/**` endpoints (except `/api/stocks/health`) require a valid JWT token.

#### Get JWT Token
```bash
# Login to get JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser"}'

# Response:
# {
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "type": "Bearer",
#   "username": "testuser"
# }
```

### API Endpoints
```
POST   /api/auth/login          → Generate JWT token (public)
GET    /api/stocks/health       → Health check (public)
GET    /api/stocks              → List all stocks (requires JWT)
GET    /api/stocks/{symbol}     → Get specific stock quote (requires JWT)
POST   /api/stocks              → Create new stock (requires JWT)
PUT    /api/stocks/{symbol}     → Update stock (requires JWT)
PUT    /api/stocks/{symbol}/price → Update stock price (requires JWT)
DELETE /api/stocks/{symbol}     → Delete stock (requires JWT)
```

### Sample Requests with JWT
```bash
# 1. Get JWT token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo"}' | jq -r '.token')

# 2. Access protected endpoint with token
curl http://localhost:8080/api/stocks \
  -H "Authorization: Bearer $TOKEN"

# 3. Get specific stock
curl http://localhost:8080/api/stocks/AAPL \
  -H "Authorization: Bearer $TOKEN"

# 4. Create new stock
curl -X POST http://localhost:8080/api/stocks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"symbol":"NVDA","name":"NVIDIA Corporation","currentPrice":500.00,"change":5.00,"changePercent":1.0}'

# 5. Update stock price
curl -X PUT http://localhost:8080/api/stocks/AAPL/price \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"currentPrice":155.00}'

# 6. Access public health endpoint (no token required)
curl http://localhost:8080/api/stocks/health
```

### JWT Configuration
- **Token Expiration**: 24 hours (86400000 ms)
- **Algorithm**: HMAC-SHA with secret key
- **Session**: Stateless (no server-side session storage)
- **Note**: For production, externalize the JWT secret using environment variables

---

## 📖 Implementation Plan

### Phase 1: Project Foundation (1 hour)
- Maven POM configuration with Spring Boot
- Main application class
- Application properties configuration

### Phase 2: Compliance Documentation (5.5 hours)
- Create compliance documentation structure
- Write 6 detailed compliance documents
- Include code examples and validation criteria

### Phase 3: Code Review Configuration (2.5 hours)
- Set up .github/code-review/ directory
- Create review configuration files
- Document review process and guidelines

### Phase 4: Application Implementation (5.5 hours)
- Implement model classes
- Create repository layer
- Build service layer
- Develop REST controller
- Add exception handling

### Phase 5: Testing & Documentation (7 hours)
- Write unit tests (80%+ coverage)
- Create integration tests
- Add JavaDoc comments
- Update documentation

### Phase 6: Build Validation (4 hours)
- Verify Maven build
- Test application startup
- Validate all endpoints
- Conduct final review

**Total Estimated Effort**: 25-28 hours

---

## ✅ Success Criteria

### Technical
- Application builds successfully with Maven
- All tests pass with 80%+ code coverage
- All REST endpoints functional
- Code follows documented standards
- No critical security vulnerabilities

### Documentation
- All 6 compliance documents complete
- Code review configuration in place
- README clear and comprehensive
- API fully documented

### Demo Quality
- Easy to understand and navigate
- Quick setup (< 5 minutes)
- Professional presentation quality
- Clear value demonstration

---

## 🔐 Compliance & Code Review

### Compliance-First Approach
All code must adhere to documented standards in **[COMPLIANCE_RULES.md](COMPLIANCE_RULES.md)**. This comprehensive document covers:
- Security requirements (input validation, exception handling, logging)
- Code quality standards (naming, method design, documentation)
- Architecture patterns (layering, dependency injection, REST principles)
- Testing requirements (coverage, quality, organization)
- Error handling and validation rules
- Performance and resource management
- Configuration and properties management
- Build and deployment standards
- Version control and Git practices
- Complete code review checklists

### Automated Code Review
The code review agent accesses the centralized **COMPLIANCE_RULES.md** to:
- Validate code against 10 major compliance categories
- Check security vulnerabilities and secure coding practices
- Verify adherence to architecture and design patterns
- Validate test coverage and quality requirements
- Generate detailed compliance reports with section references
- Provide actionable feedback linked to specific rules
- Ensure all code review checklist items are addressed

### Code Review Process
1. Developer submits code for review
2. Code review agent reads **COMPLIANCE_RULES.md**
3. Agent validates code against each compliance section
4. Detailed report generated with:
   - Compliance violations found
   - References to specific rule sections
   - Severity levels (MANDATORY violations block merge)
   - Recommended fixes with examples
5. Developer addresses feedback and resubmits

---

## 🎓 Learning Objectives

This demo teaches:
1. **Modern Java Development** - Spring Boot, Maven, RESTful APIs
2. **Code Compliance** - Centralized, version-controlled standards
3. **Automated Quality** - Integration of compliance with code review
4. **SDLC Best Practices** - Professional development workflows
5. **Documentation** - Clear, maintainable project documentation

---

## 🛠️ Development Tools

### Recommended
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Build**: Maven
- **Testing**: JUnit 5, Spring Boot Test
- **Quality**: Checkstyle, PMD, SpotBugs, SonarQube
- **Coverage**: JaCoCo

---

## 📊 Project Timeline

```
Planning Phase        ✓ Complete (2026-01-14)
Implementation Phase  ✓ Complete (2026-01-14)
Testing & Validation  ✓ Complete (All 31 tests passing)
Demo Preparation      ✓ Ready for Demo
```

---

## 🤝 Contributing

### For Implementation Team
1. Review all planning documents in order:
   - PROJECT_PLAN.md (detailed tasks)
   - ARCHITECTURE_OVERVIEW.md (technical design)
   - COMPLIANCE_GUIDE.md (standards templates)
   - IMPLEMENTATION_HANDOFF.md (summary)

2. Follow the implementation plan sequentially
3. Reference compliance docs while coding
4. Write tests alongside implementation
5. Document as you develop

### Standards
- All code must comply with `docs/compliance/` standards
- Minimum 80% test coverage required
- JavaDoc required for all public APIs
- No code merged without review

---

## 📁 Repository Structure (Planned)

```
web-application-1/
├── .github/
│   └── code-review/                 # Code review configuration
│       ├── review-config.json
│       ├── compliance-checklist.md
│       └── review-guidelines.md
├── docs/
│   ├── compliance/                  # Compliance documentation
│   │   ├── README.md
│   │   ├── coding-standards.md
│   │   ├── security-requirements.md
│   │   ├── api-design-guidelines.md
│   │   ├── testing-requirements.md
│   │   ├── documentation-standards.md
│   │   └── architecture-principles.md
│   └── API.md                       # API documentation
├── src/
│   ├── main/
│   │   ├── java/                    # Application code
│   │   └── resources/               # Configuration files
│   └── test/
│       └── java/                    # Test code
├── PROJECT_PLAN.md                  # Detailed implementation plan
├── ARCHITECTURE_OVERVIEW.md         # Architecture guide
├── COMPLIANCE_GUIDE.md              # Compliance best practices
├── IMPLEMENTATION_HANDOFF.md        # Handoff summary
├── pom.xml                          # Maven configuration
├── README.md                        # This file
└── LICENSE                          # License file
```

---

## 📝 Notes

### Design Principles
- **Simplicity**: Keep business logic simple to focus on structure
- **Compliance First**: Code follows documented standards
- **Demo Ready**: Everything clear and presentable
- **Professional**: Production-ready patterns and practices

### Key Decisions
- **In-Memory Storage**: No database needed for demo simplicity
- **Mock Data**: Pre-populated sample stocks (AAPL, GOOGL, MSFT, etc.)
- **REST Only**: No UI, focus on backend API
- **Java 17**: Modern language features, long-term support

---

## 📞 Support

For questions or issues:
1. Review the planning documents
2. Consult compliance documentation
3. Check Spring Boot official docs
4. Contact project lead

---

## 📄 License

This project is licensed under the terms specified in the LICENSE file.

---

## 🎯 Quick Links

- [**Compliance Rules**](COMPLIANCE_RULES.md) - Single source of truth for all compliance requirements
- [**Compliance Checking Guide**](COMPLIANCE_CHECKING_GUIDE.md) - How code review validates against compliance
- [Detailed Implementation Plan](PROJECT_PLAN.md)
- [Architecture & Design Guide](ARCHITECTURE_OVERVIEW.md)
- [Compliance Documentation Guide](COMPLIANCE_GUIDE.md)
- [Implementation Handoff Summary](IMPLEMENTATION_HANDOFF.md)

---

**Status**: Implementation Complete ✓ | Ready for Demo  
**Last Updated**: 2026-01-14  
**Project Goal**: Customer Workshop Demo - Modern SDLC with Integrated Compliance