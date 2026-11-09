package app;

import domain.Library;
import domain.LendingItem;
import domain.Loan;
import domain.Member;

public class LendingApplication {

    public static void main(String[] args) {
        Library lib = new Library("The Reading Room");

        LendingItem novel = new LendingItem("Things Fall Apart", "BOOK");
        LendingItem audioCourse = new LendingItem("French Audio Course", "AUDIO_KIT");
        lib.addItem(novel);
        lib.addItem(audioCourse);

        Member kofi = new Member("Kofi Mensah", "STANDARD");
        Member ama = new Member("Ama Owusu", "STUDENT");
        lib.addMember(kofi);
        lib.addMember(ama);

        // a normal afternoon
        printBorrowMessage(novel, lib.borrow(kofi, novel));
        printBorrowMessage(audioCourse, lib.borrow(ama, audioCourse));
        lib.returnItem(audioCourse);
        System.out.println("Returned \"" + audioCourse.title + "\"");

        // ... and the sort of thing that worries them:
        printBorrowMessage(novel, lib.borrow(ama, novel)); // the novel is already out with Kofi, but nothing stops this

        System.out.println();
        System.out.println("=== " + lib.name + " - end of day ===");
        System.out.println("Items:");
        for (LendingItem item : lib.items) {
            System.out.println("  \"" + item.title + "\" (" + item.type + "): " + item.status
                    + (item.currentBorrower != null ? " - with " + item.currentBorrower : ""));
        }
        System.out.println("Active loans:");
        for (Loan loan : lib.getLoans()) {
            if (loan.status.equals("ACTIVE")) {
                System.out.println("  Loan #" + loan.id + ": \"" + loan.itemTitle + "\" -> " + loan.memberName);
            }
        }

    }

    private static void printBorrowMessage(LendingItem item, Loan loan) {
        if (item.type.equals("BOOK") || item.type.equals("AUDIO_KIT")) {
            System.out.println("Lent \"" + item.title + "\" to " + loan.memberName + " for "
                    + loan.loanDays + " days");
        }
    }
}
