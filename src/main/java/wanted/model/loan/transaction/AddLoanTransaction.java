package wanted.model.loan.transaction;

import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.loan.LoanDate;

/**
 * Represents a transaction of adding loan.
 */
public class AddLoanTransaction extends LoanTransaction {
    /**
     * Constructs an AddLoanTransaction with the given amount and the date.
     */
    public AddLoanTransaction(MoneyInt amount, LoanDate date) {
        super(amount, date);
    }

    @Override
    public String getExplanation() {
        return "$"
                + this.getAmount().getStringRepresentationWithFixedDecimalPoint()
                + " loaned on "
                + this.getDate();
    }

    @Override
    public MoneyInt getTotalAmountAfterTransaction(MoneyInt previousTotalAmount) {
        requireAllNonNull(previousTotalAmount);
        int resultCent = previousTotalAmount.getValueTimesOneHundred()
                + this.getAmount().getValueTimesOneHundred();
        return MoneyInt.fromCent(resultCent);
    }

    @Override
    public MoneyInt getRemainingAmountAfterTransaction(MoneyInt previousRemainingAmount) {
        requireAllNonNull(previousRemainingAmount);
        int resultCent = previousRemainingAmount.getValueTimesOneHundred()
                + this.getAmount().getValueTimesOneHundred();
        return MoneyInt.fromCent(resultCent);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddLoanTransaction)) {
            return false;
        }

        AddLoanTransaction otherAddLoanTransaction = (AddLoanTransaction) other;
        return this.getAmount().equals(otherAddLoanTransaction.getAmount())
                && this.getDate().equals(otherAddLoanTransaction.getDate());
    }
}
