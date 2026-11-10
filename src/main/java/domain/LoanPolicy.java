package domain;

/*
 * Item-specific lending rules are kept behind this strategy so new policies
 * can be added without adding item-type branches to LendingService.
 */
public interface LoanPolicy {
    int loanDaysFor(Member member);

    default void applyTo(LendingItem item, Member member) {
        item.lendTo(member.getName());
    }

    static LoanPolicy forType(String type) {
        return switch (type) {
            case "BOOK" -> member -> member.getKind().equals("STUDENT") ? 28 : 14;
            case "AUDIO_KIT" -> member -> member.getKind().equals("STUDENT") ? 14 : 7;
            // Preserve the previous zero-day loan behavior for unrecognized types.
            default -> new LoanPolicy() {
                @Override
                public int loanDaysFor(Member member) {
                    return 0;
                }

                @Override
                public void applyTo(LendingItem item, Member member) {
                }
            };
        };
    }
}
