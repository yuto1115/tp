package wanted.model.loan;

import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.commons.util.ToStringBuilder;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.LoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;
import wanted.model.tag.Tag;

/**
 * Represents a Loan in the loan book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 * Due to the immutability of the class, methods that modify its state will return a new Loan.
 */
public class Loan {
    // Identity fields
    private final Name name;

    // Data fields
    private final LoanAmount loanAmount;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Constructs a new Loan with the given name, LoanAmount, and tags.
     */
    public Loan(Name name, LoanAmount loanAmount, Set<Tag> tags) {
        requireAllNonNull(name, loanAmount, tags);
        this.name = name;
        this.loanAmount = loanAmount;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public LoanAmount getLoanAmount() {
        return loanAmount;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same loan.
     * This defines a weaker notion of equality between two loans.
     */
    public boolean isSameLoan(Loan otherLoan) {
        if (otherLoan == this) {
            return true;
        }

        return otherLoan != null
                && otherLoan.name.equals(name);
    }

    /**
     * Returns true if both loans have the same identity and data fields.
     * This defines a stronger notion of equality between two loans.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Loan otherLoan)) {
            return false;
        }

        return name.equals(otherLoan.name)
                && loanAmount.equals(otherLoan.loanAmount)
                && tags.equals(otherLoan.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, loanAmount, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("amount", loanAmount)
                .add("tags", tags)
                .toString();
    }

    /**
     * Performs a new transaction of loaning money with the specified amount and the date.
     * A new Loan object is created and returned, hence the original Loan remains unchanged.
     *
     * @param loaned Amount given to loanee in this transaction.
     * @param date   Date of the transaction.
     * @return New Loan after the loaning transaction.
     */
    public Loan addLoan(MoneyInt loaned, LoanDate date) {
        requireAllNonNull(loaned, date);
        AddLoanTransaction transaction = new AddLoanTransaction(loaned, date);

        LoanAmount newLoanAmount;
        try {
            newLoanAmount = loanAmount.appendTransaction(transaction);
        } catch (ExcessRepaymentException e) {
            throw new RuntimeException("Loan addition should not cause ExcessRepaymentException.");
        }

        return new Loan(this.name, newLoanAmount, this.tags);
    }

    /**
     * Performs a new transaction of repaying money with the specified amount and the date.
     * A new Loan object is created and returned, hence the original Loan remains unchanged.
     *
     * @param repaid Amount repaid from loanee in this transaction.
     * @param date   Date of the transaction.
     * @return New Loan after the repaying transaction.
     */
    public Loan repayLoan(MoneyInt repaid, LoanDate date) throws ExcessRepaymentException {
        requireAllNonNull(repaid, date);
        RepayLoanTransaction transaction = new RepayLoanTransaction(repaid, date);

        LoanAmount newLoanAmount = loanAmount.appendTransaction(transaction);

        return new Loan(this.name, newLoanAmount, this.tags);
    }

    /**
     * Delete a transaction at the given index of the transaction history.
     * A new Loan object is created and returned, hence the original Loan remains unchanged.
     *
     * @param index Index of the transaction to be deleted.
     */
    public Loan deleteTransaction(Index index) throws ExcessRepaymentException {
        requireAllNonNull(index);
        ArrayList<LoanTransaction> transactions = this.loanAmount.getTransactionHistoryCopy();
        if (index.getZeroBased() >= transactions.size()) {
            throw new IllegalArgumentException("Index out of bounds.");
        }

        transactions.remove(index.getZeroBased());

        return new Loan(this.name, new LoanAmount(transactions), this.tags);
    }

}
