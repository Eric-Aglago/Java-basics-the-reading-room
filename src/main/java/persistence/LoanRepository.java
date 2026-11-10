package persistence;

import domain.Loan;

import java.util.List;
import java.util.Optional;

/** Storage contract for loans, including lookup by their unique identifier. */
public interface LoanRepository {
    void save(Loan loan);

    Optional<Loan> findById(int loanId);

    List<Loan> findAll();
}
