package wanted.model.loan;

import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.commons.util.AppUtil;
import wanted.commons.util.ToStringBuilder;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.LoanTransaction;

/**
 * Manages the total amount, the remaining amount, and the transaction history of a loan.
 * Guarantees: immutable values
 * Due to the immutability of the class, methods that modify its state will return a new LoanAmount.
 */
public class LoanAmount implements Comparable<LoanAmount> {
    private final MoneyInt totalAmount;
    private final MoneyInt remainingAmount;
    private final ArrayList<LoanTransaction> transactionHistory;

    /**
     * Constructs a new LoanAmount with an empty transaction history (i.e. totalAmount = remainingAmount = 0).
     */
    public LoanAmount() {
        this.totalAmount = MoneyInt.fromCent(0);
        this.remainingAmount = MoneyInt.fromCent(0);
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Constructs a new LoanAmount with the given transaction history.
     * Total amount and remaining amount are calculated internally using the given history.
     *
     * @throws ExcessRepaymentException If the loan balance gets negative at any point of time.
     */
    public LoanAmount(ArrayList<LoanTransaction> transactionHistory) throws ExcessRepaymentException {
        requireAllNonNull(transactionHistory);

        MoneyInt totalAmount = MoneyInt.fromCent(0);
        MoneyInt remainingAmount = MoneyInt.fromCent(0);
        for (LoanTransaction transaction : transactionHistory) {
            totalAmount = transaction.getTotalAmountAfterTransaction(totalAmount);
            remainingAmount = transaction.getRemainingAmountAfterTransaction(remainingAmount);
        }

        this.totalAmount = totalAmount;
        this.remainingAmount = remainingAmount;
        this.transactionHistory = transactionHistory;
    }

    /**
     * Returns the total amount of money loaned so far.
     */
    public MoneyInt getTotalAmount() {
        return this.totalAmount;
    }

    /**
     * Returns the amount of money that is loaned but not yet repaid.
     */
    public MoneyInt getRemainingAmount() {
        return this.remainingAmount;
    }

    /**
     * Returns the number of transactions performed.
     */
    public int getTransactionsCount() {
        return this.transactionHistory.size();
    }

    /**
     * Returns the transaction at the given index of the history.
     * There should not be a getter method for the entire transaction history (i.e. {@code List<LoanTransaction>})
     * as it can let other objects modify the transaction history of this object.
     */
    public LoanTransaction getTransaction(Index index) {
        AppUtil.checkArgument(0 <= index.getZeroBased() && index.getZeroBased() < transactionHistory.size(),
                "Index out of bounds");
        return transactionHistory.get(index.getZeroBased());
    }

    /**
     * Returns a new LoanAmount with the given LoanTransaction appended to this LoanAmount object.
     */
    public LoanAmount appendTransaction(LoanTransaction transaction) throws ExcessRepaymentException {
        requireAllNonNull(transaction);

        @SuppressWarnings("unchecked") ArrayList<LoanTransaction> newTransactionHistory =
                (ArrayList<LoanTransaction>) this.transactionHistory.clone();
        newTransactionHistory.add(transaction);

        return new LoanAmount(newTransactionHistory);
    }

    /**
     * Returns if the remaining loan amount equals zero.
     */
    public boolean isRepaid() {
        return this.remainingAmount.getValueTimesOneHundred() == 0;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("totalAmount", totalAmount)
                .add("remainingAmount", remainingAmount)
                .add("transactions", transactionHistory)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LoanAmount)) {
            return false;
        }
        LoanAmount otherLoanAmount = (LoanAmount) other;
        return this.totalAmount.equals(otherLoanAmount.totalAmount)
                && this.remainingAmount.equals(otherLoanAmount.remainingAmount)
                && this.transactionHistory.equals(otherLoanAmount.transactionHistory);
    }

    @Override
    public int compareTo(LoanAmount o) {
        return this.remainingAmount.getValueTimesOneHundred() - o.remainingAmount.getValueTimesOneHundred();
    }
}
