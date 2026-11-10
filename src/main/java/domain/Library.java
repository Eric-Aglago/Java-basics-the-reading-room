package domain;

/*
 * The library model contains library-level identity only.
 * Item, member, and loan collections belong to repositories so storage is not
 * coupled to this domain object.
 */
public class Library {
    private final String name;

    public Library(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
