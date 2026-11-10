# The Reading Room Rebuild

## Project Overview

A Java 21 rebuild of the Reading Room community library's lending tracker. It
preserves the established BOOK and AUDIO_KIT loan periods while improving
maintainability, protecting lending records, and preparing for future item
types.

## Original Problems

- **Duplicate borrowing:** An item could be loaned to multiple members, making
  it unclear who held a copy.
- **Title-based returns:** Copies with the same title could have the wrong
  loans closed, corrupting inventory and history.
- **Unsupported item types:** The original could create inconsistent loan
  records without valid item handling.
- **Single-file design:** Mixed responsibilities made changes harder to
  understand, review, and maintain.

The rebuild prevents duplicate borrowing and title-matched returns. The
default policy still preserves the previous zero-day behavior for unknown item
types; new types should receive an explicit policy.

## Rebuild Objectives

- Separate application startup, lending workflows, domain models, and storage.
- Encapsulate state and prevent multiple active loans for one item.
- Return the exact loan and item copy, not records selected by title.
- Preserve existing BOOK and AUDIO_KIT rules and support future policies.
- Add a meaningful improvement beyond the assignment requirements.

## New Architecture

Packages separate the command-line application (`app`), workflows
(`application`), business concepts (`domain`), and storage (`persistence`).

| Class | Responsibility |
|---|---|
| `LendingApplication` | Configures and runs the example flow and prints its report. |
| `LendingService` | Coordinates borrowing and returns, checks availability, and saves changes. |
| `Library` | Represents the library; it does not own storage collections. |
| `LendingItem`, `Member`, `Loan` | Encapsulate item, member, and loan data and state. |
| `LoanPolicy` | Provides item-specific loan duration and lending behavior. |
| `ItemRepository`, `MemberRepository`, `LoanRepository` | Define storage contracts; in-memory implementations store the records. |

## Key Design Decisions

- **Separate classes:** Focused responsibilities make changes easier to locate
  and reduce unintended effects elsewhere.
- **Encapsulation:** Private fields and controlled operations protect records
  from inconsistent updates.
- **Service layer:** `LendingService` coordinates borrowing and returns in one
  testable place instead of mixing workflows into models or startup code.
- **Policies and repositories:** Policies isolate item rules; repositories
  isolate storage. Both can change without rewriting the lending workflow.
- **Rebuild rather than patch the monolith:** Restructuring addresses the
  original coupling at its source, giving volunteers a clearer foundation for
  safe future changes.

## Business Rule Enforcement

Before creating a loan, `LendingService` rejects an item already marked
`ON_LOAN`, before changing member history or recording another loan. This was
the highest-priority defect because conflicting borrowers make it impossible
for staff to reliably know who has each copy.

## Behaviour Preservation

The established durations remain:

- BOOK: 14 days for standard members; 28 for students.
- AUDIO_KIT: 7 days for standard members; 14 for students.

Successful lending and returns retain their behavior. The duplicate-borrow
example now reports a rejection and keeps the original loan active; this
intentional difference fixes the critical business defect.

## Future Extension: ELoans

Create an `ELoanPolicy` implementing `LoanPolicy` and inject it when creating
an ELoan item. The existing `LendingService`, `LendingItem`, `Loan`, and
repository contracts do not need ELoan-specific changes. Application setup
would change only if it needs to create or demonstrate ELoans. Alternatively,
if ELoans are registered through `LoanPolicy.forType(...)`, that mapping must
be extended.

## Improvement Beyond the Brief

Loans can be found by unique ID. Returns with an unknown ID or an already
returned loan are rejected before item state changes. This protects inventory
and history from accidental repeat operations; it is a business-rule safeguard,
not a cosmetic change. The explicit ID and validation also make the return flow
easier to test and maintain.

## Commit History Summary

The current Git history contains only the baseline commit `bcd7414` (`intial
commit before diagnosis`). The diagnosis, rebuild, and design documentation
are staged but not yet committed. They represent the diagnosis, class
separation, service/policy design, repositories, and lending-integrity fixes;
these are milestones, not separate commits.

## Lessons Learned

Design affects day-to-day service: inaccurate loan records create confusion
for staff and borrowers. Clear responsibilities, protected state, and
unambiguous loan identity reduce those risks. Policies and repositories also
make future changes—new item types or storage options—less disruptive for
volunteers.

## Running the Application

Install Java 21 and Maven first. Verify they are available in your terminal:

```powershell
java -version
mvn -version
```

Open a terminal, navigate to the project directory, then compile and run:

```powershell
cd path\to\java-basics-c-the-reading-room
mvn compile
java -cp target\classes app.LendingApplication
```

Replace `path\to` with the location where you cloned or saved the project. For
example, if it is in your Downloads folder:

```powershell
cd C:\Users\YourName\Downloads\java-basics-c-the-reading-room
mvn compile
java -cp target\classes app.LendingApplication
```

## Running the Tests

Requires Java 21 and Maven.

1. Open a terminal in the project directory.
2. Check that Java and Maven are available:
   ```bash
   java -version
   mvn -version
   ```
3. Run the lending service test:
   ```bash
   mvn -Dtest=LendingServiceTest test
   ```
4. Confirm the test summary reports 6 tests with no failures or errors, followed by `BUILD SUCCESS`.

To run all project tests instead, use:
```bash
mvn test
```
