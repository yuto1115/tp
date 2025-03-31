package wanted.model.loan.transaction;

import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;

/**
 * Abstract class to represents a loan transaction.
 * Subclasses should implement the functions to calculate the total and remaining loan amounts after the transaction,
 * given those values just before the transaction.
 */
public abstract class LoanTransaction {
    private final MoneyInt amount;
    private final LoanDate date;

    /**
     * Constructs a new transaction with the given amount and date.
     */
    public LoanTransaction(MoneyInt amount, LoanDate date) {
        requireAllNonNull(amount, date);
        this.amount = amount;
        this.date = date;
    }

    /**
     * Returns the amount of the transaction.
     */
    public MoneyInt getAmount() {
        return this.amount;
    }

    /**
     * Returns the date of the transaction.
     */
    public LoanDate getDate() {
        return this.date;
    }

    /**
     * Returns a brief explanation of the transaction.
     * Meant to be displayed on UI.
     * e.g. "$40.00 loaned on 1 Jan 2020"
     *      "$20.00 repaid on 2 Jan 2020"
     */
    public abstract String getExplanation();

    /**
     * Returns the total loan amount after this transaction is completed.
     * @param previousTotalAmount Total loan amount just before this transaction.
     */
    public abstract MoneyInt getTotalAmountAfterTransaction(MoneyInt previousTotalAmount);

    /**
     * Returns the remaining loan amount after this transaction is completed.
     * @param previousRemainingAmount Remaining loan amount just before this transaction.
     * @throws ExcessRepaymentException If the resulting value is negative.
     */
    public abstract MoneyInt getRemainingAmountAfterTransaction(MoneyInt previousRemainingAmount)
            throws ExcessRepaymentException;
}
