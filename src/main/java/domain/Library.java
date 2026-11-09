package domain;

import java.util.ArrayList;
import java.util.List;

public class Library {
    public String name;
    public List<LendingItem> items = new ArrayList<>();
    public List<Member> members = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();

    public Library(String name) {
        this.name = name;
    }

    public void addItem(LendingItem i) {
        items.add(i);
    }

    public void addMember(Member m) {
        members.add(m);
    }

    // Lend an item to a member. The loan period depends on the kind of item and member.
    public Loan borrow(Member m, LendingItem item) {
        int loanDays = 0;
        if (item.type.equals("BOOK")) {
            loanDays = 14;
            if (m.kind.equals("STUDENT")) {
                loanDays = 28; // students get longer loans
            }
            item.status = "ON_LOAN";
            item.currentBorrower = m.name;
        } else if (item.type.equals("AUDIO_KIT")) {
            loanDays = 7;
            if (m.kind.equals("STUDENT")) {
                loanDays = 14; // students get longer loans
            }
            item.status = "ON_LOAN";
            item.currentBorrower = m.name;
        }
        m.history.add("Borrowed " + item.title);
        Loan loan = new Loan(m.name, item.title, loanDays);
        loans.add(loan);
        return loan;
    }

    public void returnItem(LendingItem item) {
        item.status = "AVAILABLE";
        item.currentBorrower = null;
        for (Loan l : loans) {
            if (l.itemTitle.equals(item.title) && l.status.equals("ACTIVE")) {
                l.status = "RETURNED";
            }
        }
    }

    public List<Loan> getLoans() {
        return loans;
    }
}
