package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Library {
    private final String name;
    private final List<LendingItem> items = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();

    public Library(String name) {
        this.name = name;
    }

    public void addItem(LendingItem i) {
        items.add(i);
    }

    public void addMember(Member m) {
        members.add(m);
    }

    public String getName() {
        return name;
    }

    public List<LendingItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    // Lend an item to a member. The loan period depends on the kind of item and member.
    public Loan borrow(Member m, LendingItem item) {
        int loanDays = 0;
        if (item.getType().equals("BOOK")) {
            loanDays = 14;
            if (m.getKind().equals("STUDENT")) {
                loanDays = 28; // students get longer loans
            }
            item.lendTo(m.getName());
        } else if (item.getType().equals("AUDIO_KIT")) {
            loanDays = 7;
            if (m.getKind().equals("STUDENT")) {
                loanDays = 14; // students get longer loans
            }
            item.lendTo(m.getName());
        }
        m.recordBorrowedItem(item.getTitle());
        Loan loan = new Loan(m.getName(), item.getTitle(), loanDays);
        loans.add(loan);
        return loan;
    }

    public void returnItem(LendingItem item) {
        item.returnToShelf();
        for (Loan l : loans) {
            if (l.getItemTitle().equals(item.getTitle()) && l.getStatus().equals("ACTIVE")) {
                l.markReturned();
            }
        }
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }
}
