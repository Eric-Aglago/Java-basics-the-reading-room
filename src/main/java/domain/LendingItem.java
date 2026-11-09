package domain;

import java.util.Objects;

public class LendingItem {
    private static int counter = 1;

    private final String title;
    private final String type; // "BOOK" or "AUDIO_KIT"
    private final LoanPolicy loanPolicy;
    private String status; // "AVAILABLE" or "ON_LOAN"
    private String currentBorrower;
    private final int id;

    public LendingItem(String title, String type) {
        this(title, type, LoanPolicy.forType(type));
    }

    public LendingItem(String title, String type, LoanPolicy loanPolicy) {
        this.id = counter;
        counter++;
        this.title = title;
        this.type = type;
        this.loanPolicy = Objects.requireNonNull(loanPolicy, "loanPolicy");
        this.status = "AVAILABLE";
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public LoanPolicy getLoanPolicy() {
        return loanPolicy;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentBorrower() {
        return currentBorrower;
    }

    public int getId() {
        return id;
    }

    public void lendTo(String borrower) {
        status = "ON_LOAN";
        currentBorrower = borrower;
    }

    public void returnToShelf() {
        status = "AVAILABLE";
        currentBorrower = null;
    }
}
