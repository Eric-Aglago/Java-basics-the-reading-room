package application;

import domain.Library;
import domain.LendingItem;
import domain.Loan;
import domain.LoanPolicy;
import domain.Member;

public class LendingService {
    private final Library library;

    public LendingService(Library library) {
        this.library = library;
    }

    public Loan borrow(Member member, LendingItem item) {
        if (item.getStatus().equals("ON_LOAN")) {
            throw new IllegalStateException("Item \"" + item.getTitle() + "\" is already on loan");
        }

        LoanPolicy policy = item.getLoanPolicy();
        int loanDays = policy.loanDaysFor(member);
        policy.applyTo(item, member);

        member.recordBorrowedItem(item.getTitle());
        Loan loan = new Loan(member.getName(), item.getTitle(), loanDays);
        library.addLoan(loan);
        return loan;
    }

    public void returnItem(LendingItem item) {
        item.returnToShelf();
        for (Loan loan : library.getLoans()) {
            if (loan.getItemTitle().equals(item.getTitle()) && loan.getStatus().equals("ACTIVE")) {
                loan.markReturned();
            }
        }
    }
}
