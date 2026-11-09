package domain;

public class Loan {
    private static int counter = 1;

    private final int id;
    private final String memberName;
    private final String itemTitle;
    private final int loanDays;
    private String status; // "ACTIVE" or "RETURNED"

    public Loan(String memberName, String itemTitle, int loanDays) {
        this.id = counter;
        counter++;
        this.memberName = memberName;
        this.itemTitle = itemTitle;
        this.loanDays = loanDays;
        this.status = "ACTIVE";
    }

    public int getId() {
        return id;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public String getStatus() {
        return status;
    }

    public void markReturned() {
        status = "RETURNED";
    }
}
