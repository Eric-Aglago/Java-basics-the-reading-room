package persistence;

import domain.Loan;

import java.util.List;

public interface LoanRepository {
    void save(Loan loan);

    List<Loan> findAll();
}
