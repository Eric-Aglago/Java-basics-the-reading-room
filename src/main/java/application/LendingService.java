package application;

import domain.Library;
import domain.LendingItem;
import domain.Loan;
import domain.LoanPolicy;
import domain.Member;
import persistence.ItemRepository;
import persistence.LoanRepository;
import persistence.MemberRepository;

public class LendingService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public LendingService(ItemRepository itemRepository, MemberRepository memberRepository,
                          LoanRepository loanRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    public Loan borrow(Member member, LendingItem item) {
        /*
         * Business rule: one physical item can have only one active loan.
         * Reject before changing the item, member history, or loan records.
         */
        if (item.getStatus().equals("ON_LOAN")) {
            throw new IllegalStateException("Item \"" + item.getTitle() + "\" is already on loan");
        }

        // Keep item-specific duration and state rules in its policy, not in this workflow.
        LoanPolicy policy = item.getLoanPolicy();
        int loanDays = policy.loanDaysFor(member);
        policy.applyTo(item, member);
        itemRepository.save(item);

        member.recordBorrowedItem(item.getTitle());
        memberRepository.save(member);
        Loan loan = new Loan(member.getName(), item, loanDays);
        loanRepository.save(loan);
        return loan;
    }

    public void returnItem(int loanId) {
        // Resolve by unique loan ID rather than title, which may be shared by multiple copies.
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan #" + loanId + " does not exist"));

        // Check before changing either record so a repeated return cannot alter state again.
        if (!loan.getStatus().equals("ACTIVE")) {
            throw new IllegalStateException("Loan #" + loanId + " has already been returned");
        }

        // A loan retains its actual item reference, so only that copy is made available.
        LendingItem item = loan.getItem();
        item.returnToShelf();
        itemRepository.save(item);
        loan.markReturned();
        loanRepository.save(loan);
    }
}
