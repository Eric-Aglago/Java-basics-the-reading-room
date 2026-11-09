package application;

import domain.Library;
import domain.LendingItem;
import domain.Loan;
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

        int loanDays = 0;
        if (item.getType().equals("BOOK")) {
            loanDays = 14;
            if (member.getKind().equals("STUDENT")) {
                loanDays = 28;
            }
            item.lendTo(member.getName());
        } else if (item.getType().equals("AUDIO_KIT")) {
            loanDays = 7;
            if (member.getKind().equals("STUDENT")) {
                loanDays = 14;
            }
            item.lendTo(member.getName());
        }

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
