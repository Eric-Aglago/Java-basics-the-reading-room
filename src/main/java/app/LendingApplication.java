package app;

import application.LendingService;
import domain.Library;
import domain.LendingItem;
import domain.Loan;
import domain.Member;
import persistence.InMemoryItemRepository;
import persistence.InMemoryLoanRepository;
import persistence.InMemoryMemberRepository;
import persistence.ItemRepository;
import persistence.LoanRepository;
import persistence.MemberRepository;

public class LendingApplication {

    public static void main(String[] args) {
        Library lib = new Library("The Reading Room");
        ItemRepository itemRepository = new InMemoryItemRepository();
        MemberRepository memberRepository = new InMemoryMemberRepository();
        LoanRepository loanRepository = new InMemoryLoanRepository();
        LendingService lendingService = new LendingService(itemRepository, memberRepository, loanRepository);

        LendingItem novel = new LendingItem("Things Fall Apart", "BOOK");
        LendingItem audioCourse = new LendingItem("French Audio Course", "AUDIO_KIT");
        itemRepository.save(novel);
        itemRepository.save(audioCourse);

        Member kofi = new Member("Kofi Mensah", "STANDARD");
        Member ama = new Member("Ama Owusu", "STUDENT");
        memberRepository.save(kofi);
        memberRepository.save(ama);

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
        for (LendingItem item : itemRepository.findAll()) {
            System.out.println("  \"" + item.getTitle() + "\" (" + item.getType() + "): " + item.getStatus()
                    + (item.getCurrentBorrower() != null ? " - with " + item.getCurrentBorrower() : ""));
        }
        System.out.println("Active loans:");
        for (Loan loan : loanRepository.findAll()) {
            if (loan.getStatus().equals("ACTIVE")) {
                System.out.println("  Loan #" + loan.getId() + ": \"" + loan.getItemTitle() + "\" -> "
                        + loan.getMemberName());
            }
        }
    }

    private static void printBorrowMessage(LendingItem item, Loan loan) {
        if (loan.getLoanDays() > 0) {
            System.out.println("Lent \"" + item.getTitle() + "\" to " + loan.getMemberName() + " for "
                    + loan.getLoanDays() + " days");
        }
    }
}
