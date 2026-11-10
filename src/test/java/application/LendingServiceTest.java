package application;

import domain.LendingItem;
import domain.Loan;
import domain.Member;
import domain.LoanPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.InMemoryItemRepository;
import persistence.InMemoryLoanRepository;
import persistence.InMemoryMemberRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LendingServiceTest {
    private InMemoryItemRepository itemRepository;
    private InMemoryMemberRepository memberRepository;
    private InMemoryLoanRepository loanRepository;
    private LendingService lendingService;

    @BeforeEach
    void setUp() {
        itemRepository = new InMemoryItemRepository();
        memberRepository = new InMemoryMemberRepository();
        loanRepository = new InMemoryLoanRepository();
        lendingService = new LendingService(itemRepository, memberRepository, loanRepository);
    }

    @Test
    void usesExistingBookAndAudioKitLoanPeriods() {
        Member standardMember = new Member("Standard", "STANDARD");
        Member studentMember = new Member("Student", "STUDENT");

        assertEquals(14, lendingService.borrow(standardMember, new LendingItem("Book", "BOOK")).getLoanDays());
        assertEquals(28, lendingService.borrow(studentMember, new LendingItem("Student Book", "BOOK")).getLoanDays());
        assertEquals(7, lendingService.borrow(standardMember, new LendingItem("Audio", "AUDIO_KIT")).getLoanDays());
        assertEquals(14, lendingService.borrow(studentMember, new LendingItem("Student Audio", "AUDIO_KIT")).getLoanDays());
    }

    @Test
    void rejectsDuplicateBorrowWithoutChangingMemberOrLoanRecords() {
        LendingItem item = new LendingItem("Shared Copy", "BOOK");
        Member firstMember = new Member("First", "STANDARD");
        Member secondMember = new Member("Second", "STUDENT");
        Loan firstLoan = lendingService.borrow(firstMember, item);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> lendingService.borrow(secondMember, item)
        );

        assertTrue(exception.getMessage().contains("already on loan"));
        assertEquals("ON_LOAN", item.getStatus());
        assertEquals("First", item.getCurrentBorrower());
        assertEquals(1, firstMember.getHistory().size());
        assertTrue(secondMember.getHistory().isEmpty());
        assertEquals(1, loanRepository.findAll().size());
        assertSame(firstLoan, loanRepository.findAll().get(0));
    }

    @Test
    void returnByLoanIdAffectsOnlyTheLoanAndCopyBeingReturned() {
        LendingItem firstCopy = new LendingItem("Same Title", "BOOK");
        LendingItem secondCopy = new LendingItem("Same Title", "BOOK");
        Loan firstLoan = lendingService.borrow(new Member("First", "STANDARD"), firstCopy);
        Loan secondLoan = lendingService.borrow(new Member("Second", "STANDARD"), secondCopy);

        lendingService.returnItem(firstLoan.getId());

        assertEquals("RETURNED", firstLoan.getStatus());
        assertEquals("AVAILABLE", firstCopy.getStatus());
        assertEquals("ACTIVE", secondLoan.getStatus());
        assertEquals("ON_LOAN", secondCopy.getStatus());
    }

    @Test
    void rejectsReturningAnAlreadyReturnedLoanWithoutChangingState() {
        LendingItem item = new LendingItem("Book", "BOOK");
        Loan loan = lendingService.borrow(new Member("Borrower", "STANDARD"), item);
        lendingService.returnItem(loan.getId());

        assertThrows(IllegalStateException.class, () -> lendingService.returnItem(loan.getId()));

        assertEquals("RETURNED", loan.getStatus());
        assertEquals("AVAILABLE", item.getStatus());
        assertEquals(1, loanRepository.findAll().size());
    }

    @Test
    void rejectsUnknownLoanId() {
        assertThrows(IllegalArgumentException.class, () -> lendingService.returnItem(-1));
    }

    @Test
    void acceptsAnInjectedPolicyForAFutureItemType() {
        LoanPolicy eLoanPolicy = member -> member.getKind().equals("STUDENT") ? 5 : 3;
        LendingItem eLoan = new LendingItem("Digital Book", "ELOAN", eLoanPolicy);
        Member member = new Member("Member", "STANDARD");

        Loan loan = lendingService.borrow(member, eLoan);

        assertEquals(3, loan.getLoanDays());
        assertEquals("ON_LOAN", eLoan.getStatus());
        assertFalse(loanRepository.findAll().isEmpty());
    }
}
