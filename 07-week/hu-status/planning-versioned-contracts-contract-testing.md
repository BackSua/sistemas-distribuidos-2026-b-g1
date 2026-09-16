# Planning: Versioned Contracts and Contract Testing

## Introduction

In distributed systems and microservices architectures, services communicate through well-defined interfaces known as **contracts**. A contract acts as a formal agreement between systems, defining how data is exchanged, what operations are available, and how consumers should interact with providers.

As software evolves, services continuously change. New features, new endpoints, additional fields, improved security requirements, and business-driven enhancements are regularly introduced. Without a strategy to manage these changes, dependent systems can break, causing outages, deployment failures, and compatibility issues.

To maintain stability while allowing continuous evolution, modern organizations rely on two essential practices:

- **Versioned Contracts**
- **Contract Testing**

Together, these practices enable teams to evolve services safely while ensuring that consumers and providers remain compatible across releases.

---

# What is a Contract?

A **contract** is an agreement between a service provider and a service consumer.

It defines precisely how communication should occur between systems.

A contract typically specifies:

- Available operations
- Request structure
- Response structure
- Data types
- Validation rules
- Error responses
- Authentication requirements
- Communication protocols

Consumers depend on these definitions to interact correctly with the service.

---

## Example REST Contract

```json
{
  "id": 100,
  "name": "John Smith"
}
```

This contract indicates that the consumer should expect:

- An integer identifier
- A string containing the customer's name

---

## Example gRPC Contract

```proto
message Customer {
  int32 id = 1;
  string name = 2;
}
```

The contract explicitly defines the structure and type of data exchanged.

---

# Why Contracts Matter

In distributed systems, multiple teams often work independently.

Without clearly defined contracts:

- Integrations become unreliable
- Services break unexpectedly
- Teams cannot work independently
- Deployment risks increase
- Troubleshooting becomes difficult

---

## Benefits of Well-Managed Contracts

### Consistency

Consumers always know what responses to expect.

### Predictability

System behavior remains stable across releases.

### Integration Stability

Changes can be managed in a controlled manner.

### Team Autonomy

Teams can work independently without constant coordination.

### Safer Deployments

Risk is reduced when interfaces are formally defined.

---

# What are Versioned Contracts?

A **versioned contract** is a contract that contains an explicit version identifier.

Versioning enables services to evolve while maintaining support for existing consumers.

Example:

```text
API Version 1 (v1)
API Version 2 (v2)
```

Older consumers continue using v1 while newer consumers migrate to v2.

This approach avoids breaking existing integrations when introducing enhancements.

---

# Why Versioning is Necessary

Applications evolve continuously.

Common changes include:

- New fields
- New endpoints
- Modified business behavior
- Improved data models
- Security enhancements
- Performance optimizations

Without versioning, consumers may receive unexpected responses and fail.

---

## Original Response

```json
{
  "id": 100,
  "name": "John Smith"
}
```

---

## Modified Response

```json
{
  "customerId": 100,
  "fullName": "John Smith"
}
```

Applications expecting:

```json
{
  "id": 100,
  "name": "John Smith"
}
```

may stop functioning correctly.

Versioning prevents this scenario.

---

# Types of Contract Changes

Contract changes generally fall into two categories:

- Backward-Compatible Changes
- Breaking Changes

---

# Backward-Compatible Changes

These changes do not affect existing consumers.

Existing implementations continue working without modification.

---

## Examples

### Adding Optional Fields

```json
{
  "id": 100,
  "name": "John Smith",
  "email": "john@example.com"
}
```

Existing consumers still receive the fields they expect.

---

### Adding New Endpoints

```http
/api/customers
/api/customers/search
```

The original functionality remains untouched.

---

### Extending Functionality

Additional capabilities can be introduced without changing existing behavior.

---

## Benefits

- Lower risk
- Easier upgrades
- Better consumer experience
- Reduced maintenance effort

---

# Breaking Changes

Breaking changes require consumers to modify their implementations.

These changes can cause failures if not managed carefully.

---

## Examples

### Renaming Fields

Before:

```json
{
  "id": 100
}
```

After:

```json
{
  "customerId": 100
}
```

---

### Removing Properties

Before:

```json
{
  "id": 100,
  "name": "John"
}
```

After:

```json
{
  "id": 100
}
```

---

### Changing Data Types

Before:

```json
{
  "id": 100
}
```

After:

```json
{
  "id": "100"
}
```

---

### Modifying Endpoint Behavior

The endpoint may return different data or require different inputs.

---

# Contract Versioning Strategies

Several approaches are commonly used to implement API versioning.

---

# 1. URI Versioning

The version appears directly in the endpoint URL.

Example:

```http
/api/v1/customers
/api/v2/customers
```

---

## Advantages

- Easy to understand
- Easy routing
- Highly visible
- Widely adopted

---

## Disadvantages

- Multiple API versions must be maintained
- URLs become numerous over time

---

# 2. Header Versioning

The version is specified in an HTTP header.

Example:

```http
Accept-Version: v2
```

---

## Advantages

- Cleaner URLs
- More flexible
- Easier endpoint management

---

## Disadvantages

- Less visible
- Harder to test manually

---

# 3. Query Parameter Versioning

The version is provided through a query parameter.

Example:

```http
/api/customers?version=2
```

---

## Advantages

- Easy implementation
- Minimal infrastructure changes

---

## Disadvantages

- Less common
- Can become ambiguous

---

# 4. Media Type Versioning

Version information is embedded in content negotiation headers.

Example:

```http
Accept: application/vnd.company.v2+json
```

---

## Advantages

- REST-friendly
- Flexible
- Supports advanced API evolution

---

## Disadvantages

- More complex configuration
- Harder for new developers to understand

---

# Contract Evolution Best Practices

Versioning should be approached strategically to minimize disruptions.

---

# Prefer Additive Changes

Adding functionality is generally safer than replacing it.

Good Example:

```json
{
  "id": 100,
  "name": "John Smith",
  "email": "john@example.com"
}
```

Existing consumers remain unaffected.

---

# Avoid Renaming Existing Fields

Instead of replacing:

```json
{
  "customerId": 100
}
```

Use:

```json
{
  "id": 100,
  "customerId": 100
}
```

Allow consumers time to migrate.

---

# Deprecate Before Removing

Before removing a field, mark it as deprecated.

Example:

```text
Field: customerName
Status: Deprecated
Removal Date: Q4 2027
```

---

# Maintain Backward Compatibility

Whenever possible:

- Preserve older contract versions
- Avoid forcing immediate migrations
- Allow phased adoption

Benefits include:

- Reduced risk
- Easier upgrades
- Better user experience

---

# What is Contract Testing?

Contract testing verifies that service consumers and providers honor their agreed contract.

It ensures:

- Requests follow expected formats
- Responses contain required fields
- Data types remain valid
- Compatibility is preserved across versions

Unlike functional testing, contract testing focuses on the interface between systems rather than internal implementation.

---

# Why Contract Testing is Important

Microservices often communicate extensively.

Example:

```text
Frontend
     ↓
API Gateway
     ↓
Customer Service
     ↓
Order Service
     ↓
Payment Service
```

A change in one service can unintentionally impact many consumers.

Contract testing detects these issues early.

---

# Goals of Contract Testing

Contract testing helps organizations:

- Detect breaking changes
- Improve deployment confidence
- Increase release velocity
- Reduce integration failures
- Enable independent deployments
- Improve service reliability

---

# Consumer-Driven Contract Testing (CDC)

One of the most popular approaches is **Consumer-Driven Contract Testing**.

Consumers define expected behavior, and providers verify they continue to satisfy those expectations.

---

## CDC Workflow

```text
Consumer
      ↓
Expected Contract
      ↓
Contract Repository
      ↓
Provider Verification
```

---

## Example Consumer Expectation

Consumer expects:

```json
{
  "id": 100,
  "name": "John Smith"
}
```

Provider returns:

```json
{
  "id": 100,
  "name": "John Smith"
}
```

The test passes.

If the provider changes the response unexpectedly, the test fails immediately.

---

# Provider Contract Testing

Provider testing ensures service responses continue matching the documented contract.

Workflow:

```text
Input Request
      ↓
Provider Service
      ↓
Response Validation
```

The provider verifies that every response complies with contractual expectations.

---

# Contract Testing vs Integration Testing

| Feature | Contract Testing | Integration Testing |
|----------|-----------------|--------------------|
| Scope | Service Contract | Full Integration |
| Speed | Fast | Slower |
| Dependencies | Minimal | Multiple Systems |
| Failure Detection | Early | Later |
| Complexity | Low | Higher |
| Objective | Compatibility | End-to-End Validation |

Both testing approaches complement one another.

---

# Popular Contract Testing Tools

## Pact

One of the most popular contract testing solutions.

Supports:

- Java
- C#
- JavaScript
- Python
- Go

Features:

- Consumer-driven contracts
- Automated verification
- Contract publication

---

# Spring Cloud Contract

Widely used in Java and Spring ecosystems.

Provides:

- Contract generation
- Provider verification
- Stub generation

---

# OpenAPI Testing

Uses OpenAPI specifications as the source of truth.

Example:

```yaml
openapi: 3.0.0
```

Benefits:

- API documentation
- Validation
- Governance
- Consistency

---

# gRPC Contract Validation

For gRPC services, the `.proto` file serves as the contract.

Example:

```proto
service CustomerService {
  rpc GetCustomer(CustomerRequest)
      returns (CustomerResponse);
}
```

Benefits:

- Strong typing
- Automatic validation
- Language-independent contracts

---

# Versioning and Contract Testing Together

Versioning and contract testing are complementary practices.

Versioning allows systems to evolve safely.

Contract testing verifies that evolution does not introduce incompatibilities.

---

## Typical Workflow

```text
Design Contract
        ↓
Version Contract
        ↓
Implement Service
        ↓
Execute Contract Tests
        ↓
Deploy Safely
```

This process significantly reduces deployment risk.

---

# CI/CD Integration

Contract tests should run automatically in every deployment pipeline.

Example:

```text
Developer Commit
        ↓
Build
        ↓
Unit Tests
        ↓
Contract Tests
        ↓
Integration Tests
        ↓
Deployment
```

---

## Benefits

### Faster Feedback

Problems are identified immediately.

### Safer Releases

Breaking changes are caught before deployment.

### Automated Validation

Human error is reduced.

---

# Best Practices

## Define Contracts First

Adopt a Contract-First Development approach.

Benefits:

- Clear expectations
- Better collaboration
- Reduced ambiguity

---

## Use Semantic Versioning

Example:

```text
v1.0.0
v1.1.0
v2.0.0
```

Meaning:

- Major → Breaking changes
- Minor → New functionality
- Patch → Bug fixes

---

## Automate Contract Verification

Never rely solely on manual testing.

Include verification in:

- CI pipelines
- Pull requests
- Release workflows

---

## Minimize Breaking Changes

Prefer extension over replacement.

---

## Document Contract Changes

Maintain:

- Changelogs
- Deprecation notices
- Upgrade guides
- Migration instructions

---

## Run Contract Tests Continuously

Execute tests in:

- Builds
- Pull Requests
- Release Pipelines
- Production Validation Pipelines

---

# Common Challenges

Organizations frequently encounter:

## Version Sprawl

Too many supported versions increase maintenance costs.

---

## Legacy Consumer Support

Older applications may depend on deprecated interfaces.

---

## Poor Documentation

Consumers may not know how contracts have changed.

---

## Ownership Ambiguity

Unclear responsibility for maintaining contracts.

---

## Cross-Team Coordination

Multiple teams may have conflicting requirements.

---

# Conclusion

Versioned contracts and contract testing are essential practices for building reliable, scalable, and maintainable distributed systems.

**Versioned contracts** provide a structured mechanism for evolving APIs and services without disrupting existing consumers. They allow teams to introduce new functionality while preserving compatibility and reducing upgrade risks.

**Contract testing** ensures that providers and consumers continue to honor agreed interfaces throughout the software lifecycle. By validating interactions early and automatically, organizations reduce failures, improve confidence, and support independent deployments.

When combined with CI/CD automation, semantic versioning, backward-compatible design, and contract-first development, these practices establish a strong foundation for modern microservices architectures and enable organizations to evolve their systems with confidence.