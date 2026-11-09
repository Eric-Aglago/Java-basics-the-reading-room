# Ranked Defects

## 1. The System Allows the Same Book to Be Borrowed More Than Once (Severe)

**Root Cause:**  
The `borrow()` function does not check whether an item is already on loan before creating a new loan record.

**Business Risk:**  
Because of this, the same item can be loaned to multiple people at the same time. This may cause confusion about who actually has the book and lead to inaccurate lending records.

**Location:**  
Lines 104-114 in the `borrow()` method.

---

## 2. Returning a Book Can Close the Wrong Loan Records (Severe)

**Root Cause:**  
When a book is returned, the system looks only at the title and closes all active loans with the same title. It does not check which specific copy or loan is being returned.

**Business Risk:**  
If there are multiple copies of a book, returning one copy could incorrectly mark other active loans as returned, resulting in inaccurate borrowing records.

**Location:**  
Lines 127-135 in the `returnItem()` method.

---

## 3. Unsupported Item Types Still Create Loan Records (High Impact)

**Root Cause:**  
The `borrow()` function only handles `BOOK` and `AUDIO_KIT` item types correctly. If any other item type is used, the system still creates a loan record even though the item is not processed properly.

**Business Risk:**  
The system can generate inconsistent records where an item appears available while also showing an active loan, leading to incorrect lending and due-date information.

**Location:**  
Lines 95-124 in the `borrow()` method.

---

## 4. Using One File for the Entire System (High Impact)

**Root Cause:**  
Everything is written in a single `LendingTracker.java` file, meaning the lending rules, item records, member records, and loan management functionality are all mixed together instead of being separated into different classes and files.

**Business Risk:**  
As additional features are added, the code will become harder to understand, test, and maintain. A small change in one part of the system could unintentionally affect another part, increasing the risk of defects and making future updates more difficult.

**Location:**  
Throughout `LendingTracker.java`.