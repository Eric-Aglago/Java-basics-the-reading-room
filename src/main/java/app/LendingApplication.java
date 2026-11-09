package app;

import application.LendingService;
import domain.Library;
import domain.LendingItem;
import domain.Loan;
import domain.Member;

public class LendingApplication {

    public static void main(String[] args) {
        Library lib = new Library("The Reading Room");
        LendingService lendingService = new LendingService(lib);

        LendingItem novel = new LendingItem("Things Fall Apart", "BOOK");
        LendingItem audioCourse = new LendingItem("French Audio Course", "AUDIO_KIT");
        lib.addItem(novel);
        lib.addItem(audioCourse);

        Member kofi = new Member("Kofi Mensah", "STANDARD");
        Member ama = new Member("Ama Owusu", "STUDENT");
        lib.addMember(kofi);
        lib.addMember(ama);

        // a normal afternoon
        printBorrowMessage(novel, lendingService.borrow(kofi, novel));
        printBorrowMessage(audioCourse, lendingService.borrow(ama, audioCourse));
        lendingService.returnItem(audioCourse);
        System.out.println("Returned \"" + audioCourse.getTitle() + "\"");

        // ... and the sort of thing that worries them:
        try {
            printBorrowMessage(novel, lendingService.borrow(ama, novel));
        } catch (IllegalStateException exception) {
            System.out.println("Could not lend \"" + novel.getTitle() + "\": " + exception.getMessage());
        }

        System.out.println();
        System.out.println("=== " + lib.getName() + " - end of day ===");
        System.out.println("Items:");
        for (LendingItem item : lib.getItems()) {
            System.out.println("  \"" + item.getTitle() + "\" (" + item.getType() + "): " + item.getStatus()
                    + (item.getCurrentBorrower() != null ? " - with " + item.getCurrentBorrower() : ""));
        }
        System.out.println("Active loans:");
        for (Loan loan : lib.getLoans()) {
            if (loan.getStatus().equals("ACTIVE")) {
                System.out.println("  Loan #" + loan.getId() + ": \"" + loan.getItemTitle() + "\" -> "
                        + loan.getMemberName());
            }
        }
    }

    private static void printBorrowMessage(LendingItem item, Loan loan) {
        if (item.getType().equals("BOOK") || item.getType().equals("AUDIO_KIT")) {
            System.out.println("Lent \"" + item.getTitle() + "\" to " + loan.getMemberName() + " for "
                    + loan.getLoanDays() + " days");
        }
    }
}
