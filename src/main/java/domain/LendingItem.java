package domain;

public class LendingItem {
    private static int counter = 1;

    private final String title;
    private final String type; // "BOOK" or "AUDIO_KIT"
    private String status; // "AVAILABLE" or "ON_LOAN"
    private String currentBorrower;
    private final int id;

    public LendingItem(String title, String type) {
        this.id = counter;
        counter++;
        this.title = title;
        this.type = type;
        this.status = "AVAILABLE";
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
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

    void lendTo(String borrower) {
        status = "ON_LOAN";
        currentBorrower = borrower;
    }

    void returnToShelf() {
        status = "AVAILABLE";
        currentBorrower = null;
    }
}
