# Design Notes

The Reading Room tracker has been reorganized so each part of the program has a
clear responsibility. The changes preserve the existing BOOK and AUDIO_KIT
loan periods and make lending records safer to update as the system grows.

## 1. Separating the classes

The original program placed the application entry point, library operations,
items, members, and loans in one source file. These responsibilities are now
represented by separate classes and packages: `LendingApplication` starts the
program, domain classes represent library concepts, `LendingService` coordinates
lending workflows, and repositories handle storage.

This separation makes each part easier to understand and change. For example,
changing how loans are stored does not require editing the member model. For
volunteers, it also makes it easier to find the right place for a change and
reduces the chance that an unrelated part of the program is affected.

## 2. Introducing `LendingService`

`LendingService` coordinates borrowing and returning. It checks an item's
availability, asks its loan policy for a duration, updates the item and member,
and records the resulting loan. The `Library` class is now a small domain model
for the library itself rather than a place for lending workflows or storage.

Keeping this process in a service gives the system one place to enforce the
steps involved in a borrow or return. It can be tested independently with
in-memory repositories, and changes to a workflow do not need to be mixed into
the application entry point or data models. This reduces the business risk of
different parts of the program applying inconsistent lending rules.

## 3. Using `LoanPolicy`

`LoanPolicy` supplies the duration calculation for an item and member. The
current policies preserve the existing rules: BOOK loans are 14 days for
standard members and 28 days for students; AUDIO_KIT loans are 7 and 14 days,
respectively.

This keeps duration rules out of `LendingService`. A policy can be tested
directly, and changes to loan periods are less likely to accidentally affect
the process of recording or returning a loan.

## 4. Introducing repositories

`ItemRepository`, `MemberRepository`, and `LoanRepository` define how those
records are saved and retrieved. Their in-memory implementations store records
for the current run. `Library` no longer directly manages collections, while
`LendingService` uses repository interfaces to persist changes.

This separation makes storage replaceable: an in-memory implementation can
later be exchanged for a database or file-backed implementation without
rewriting the lending workflow. It also makes tests easier to set up with
controlled data. In business terms, it reduces the cost and risk of changing
how records are stored as the library's needs evolve.

## 5. Preventing duplicate borrowing

Before creating a loan, `LendingService` checks whether the item is already
marked `ON_LOAN`. If it is, the service rejects the request before changing the
member's history or recording another loan.

Without this check, two borrowers could appear to hold the same physical copy.
That can lead to disputes, inaccurate availability information, and extra work
for volunteers reconciling records. The check protects both the library's
inventory and its loan history.

## 6. Removing title-based return matching

Each `Loan` now references the actual `LendingItem` it represents. A return
uses the loan's ID to find that record and updates only that loan and its
associated item.

Titles describe items; they do not uniquely identify copies. A library may own
several copies with the same title, so title-based matching can close the wrong
loan or several loans at once. Using the loan and item references keeps
availability and return history aligned with the specific copy involved.

## 7. Supporting future ELoan items

An item can be constructed with a `LoanPolicy`, so a future ELoan can use its
own policy without adding ELoan-specific duration branches to
`LendingService`. The existing BOOK and AUDIO_KIT policies remain unchanged.
To introduce ELoans, the project would add an ELoan policy and select or inject
that policy when creating ELoan items. The application setup may also need to
create ELoan examples, but the existing borrowing workflow does not need to
change.

This is valuable because future item-specific rules can be added in isolation.
It limits the amount of existing code that must be revisited and reduces the
risk that adding a new item type will disrupt current lending.

## 8. Improvement beyond the brief

The loan repository supports lookup by unique loan ID. Returning a loan by ID
fails clearly if the ID does not exist, and the service rejects a loan that has
already been returned before changing item state.

This is more than a cosmetic improvement: it prevents duplicate returns from
silently changing records and gives callers an explicit result when they
reference an invalid or completed loan. It also makes the return operation
unambiguous and straightforward to test.

## 9. Overall business value

Together, these decisions make the tracker easier to maintain without
compromising the reliability of its records. Clear class responsibilities make
changes easier to locate; a service centralizes lending workflows; policies
allow item-specific rules to grow; repositories isolate storage choices; and
availability and loan-ID checks protect the inventory and history.

For the volunteers maintaining the system, this means fewer hidden dependencies,
clearer places to make updates, and less risk that a small change will cause
confusing or costly lending mistakes.
