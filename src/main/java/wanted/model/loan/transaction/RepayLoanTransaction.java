package wanted.model.loan.transaction;

import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;

/**
 * Represents a transaction of repaying loan.
 */
public class RepayLoanTransaction extends LoanTransaction {
    /**
     * Constructs a RepayLoanTransaction with the given amount and the date.
     */
    public RepayLoanTransaction(MoneyInt amount, LoanDate date) {
        super(amount, date);
    }

    @Override
    public String getExplanation() {
        return "$"
                + this.getAmount().getStringRepresentationWithFixedDecimalPoint()
                + " repaid on "
                + this.getDate();
    }

    @Override
    public MoneyInt getTotalAmountAfterTransaction(MoneyInt previousTotalAmount) {
        requireAllNonNull(previousTotalAmount);
        return previousTotalAmount;
    }

    @Override
    public MoneyInt getRemainingAmountAfterTransaction(MoneyInt previousRemainingAmount)
            throws ExcessRepaymentException {
        requireAllNonNull(previousRemainingAmount);
        int resultCent = previousRemainingAmount.getValueTimesOneHundred()
                - this.getAmount().getValueTimesOneHundred();
        if (resultCent < 0) {
            throw new ExcessRepaymentException();
        }
        return MoneyInt.fromCent(resultCent);
    }

    @Override
    public LoanTransaction getNewTransactionOfSameType(MoneyInt amount, LoanDate date) {
        return new RepayLoanTransaction(amount, date);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RepayLoanTransaction)) {
            return false;
        }

        RepayLoanTransaction otherRepayLoanTransaction = (RepayLoanTransaction) other;
        return this.getAmount().equals(otherRepayLoanTransaction.getAmount())
                && this.getDate().equals(otherRepayLoanTransaction.getDate());
    }
}
