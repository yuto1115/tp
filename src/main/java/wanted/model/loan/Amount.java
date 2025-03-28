package wanted.model.loan;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.AppUtil.checkArgument;

import wanted.commons.core.datatypes.MoneyInt;

/**
 * A wrapper class to manage the total amount and the remaining amount of a loan.
 * Guarantees: immutable values
 * Due to the immutability of the class, methods that modify its state will return a new Amount.
 */
public class Amount implements Comparable<Amount> {
    public final MoneyInt totalValue;
    public final MoneyInt remainingValue;

    /**
     * Constructs a {@code Amount} with specified total and remaining amount left on the loan.
     *
     * @param totalValue Initial loan amount.
     * @param remainingValue Current remaining loan amount.
     */
    public Amount(MoneyInt totalValue, MoneyInt remainingValue) throws IllegalArgumentException {
        requireNonNull(totalValue);
        requireNonNull(remainingValue);
        this.totalValue = totalValue;
        this.remainingValue = remainingValue;
    }

    @Override
    public String toString() {
        return this.remainingValue.getStringRepresentationWithFixedDecimalPoint();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Amount)) {
            return false;
        }
        Amount otherAmount = (Amount) other;
        return this.remainingValue.equals(otherAmount.remainingValue);
    }

    @Override
    public int hashCode() {
        return this.remainingValue.hashCode();
    }

    @Override
    public int compareTo(Amount o) {
        return this.remainingValue.getValueTimesOneHundred() - o.remainingValue.getValueTimesOneHundred();
    }

    public boolean isRepaid() {
        return this.remainingValue.getValueTimesOneHundred() == 0;
    }

    /**
     * Returns a new Amount with the specified loaned amount added to this Amount object.
     *
     * @param loaned Amount loaned.
     */
    public Amount addAmount(MoneyInt loaned) {
        int totalCents = this.totalValue.getValueTimesOneHundred() + loaned.getValueTimesOneHundred();
        int remainingCents = this.remainingValue.getValueTimesOneHundred() + loaned.getValueTimesOneHundred();
        return new Amount(MoneyInt.fromCent(totalCents), MoneyInt.fromCent(remainingCents));
    }

    /**
     * Returns a new Amount with the specified loaned amount repaid to this Amount object.
     *
     * @param repaid Amount repaid.
     */
    public Amount repayAmount(MoneyInt repaid) {
        int remainingCents = this.remainingValue.getValueTimesOneHundred() - repaid.getValueTimesOneHundred();
        return new Amount(this.totalValue, MoneyInt.fromCent(remainingCents));
    }
}
