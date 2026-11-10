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
        if (item.getStatus().equals("ON_LOAN")) {
            throw new IllegalStateException("Item \"" + item.getTitle() + "\" is already on loan");
        }

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

    public void returnItem(Loan loan) {
        if (!loan.getStatus().equals("ACTIVE")) {
            throw new IllegalStateException("Loan #" + loan.getId() + " is not active");
        }

        LendingItem item = loan.getItem();
        item.returnToShelf();
        itemRepository.save(item);
        loan.markReturned();
        loanRepository.save(loan);
    }
}
