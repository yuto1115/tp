package wanted.model.loan;

import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;

import wanted.commons.core.datatypes.MoneyInt;
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
     * <p>
     * The given {@code ArrayList<LoanTransaction>} array is copied internally to prevent the caller
     * of this constructor from modifying the array and also modifying this object unintentionally.
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
        this.transactionHistory = new ArrayList<>();
        this.transactionHistory.addAll(transactionHistory);
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
     * Returns the number of recorded transactions.
     */
    public int getTransactionsCount() {
        return this.transactionHistory.size();
    }

    /**
     * Returns a copy of the transaction history.
     * <p>
     * The original transaction history object should not be revealed as it can let other objects modify
     * the transaction history of this LoanAmount.
     * Note that LoanTransaction objects themselves need not be copied since they are truly immutable.
     */
    public ArrayList<LoanTransaction> getTransactionHistoryCopy() {
        ArrayList<LoanTransaction> copiedTransactionHistory = new ArrayList<>();
        copiedTransactionHistory.addAll(this.transactionHistory);
        return copiedTransactionHistory;
    }

    /**
     * Returns a new LoanAmount with the given LoanTransaction appended to this LoanAmount object.
     */
    public LoanAmount appendTransaction(LoanTransaction transaction) throws ExcessRepaymentException {
        requireAllNonNull(transaction);

        ArrayList<LoanTransaction> newTransactionHistory = this.getTransactionHistoryCopy();
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
