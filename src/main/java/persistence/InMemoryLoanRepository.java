package persistence;

import domain.Loan;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryLoanRepository implements LoanRepository {
    private final Map<Integer, Loan> loans = new LinkedHashMap<>();

    @Override
    public void save(Loan loan) {
        loans.put(loan.getId(), loan);
    }

    @Override
    public Optional<Loan> findById(int loanId) {
        return Optional.ofNullable(loans.get(loanId));
    }

    @Override
    public List<Loan> findAll() {
        return List.copyOf(loans.values());
    }
}
