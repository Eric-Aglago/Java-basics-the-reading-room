package domain;

import java.util.ArrayList;
import java.util.List;

/*
 * Member details are immutable after construction. Borrowing history is
 * maintained internally and exposed as a copy so callers cannot edit it.
 */
public class Member {
    private static int counter = 1;

    private final String name;
    private final String kind; // "STANDARD" or "STUDENT"
    private final int id;
    private final List<String> history = new ArrayList<>();

    public Member(String name, String kind) {
        this.id = counter;
        counter++;
        this.name = name;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }

    public int getId() {
        return id;
    }

    public List<String> getHistory() {
        return List.copyOf(history);
    }

    public void recordBorrowedItem(String title) {
        history.add("Borrowed " + title);
    }
}
