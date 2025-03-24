package wanted.model.loan;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.AppUtil.checkArgument;

import wanted.commons.core.datatypes.MoneyInt;

/**
 * A wrapper class to manage the amount of a loan taken out
 * Guarantees: immutable initial value; is valid as declared in {@link #isValidAmount(String)}
 */

public class Amount implements Comparable<Amount> {

    public static final String MESSAGE_CONSTRAINTS =
            "Loan amounts should only contain numbers, and it should adhere to the format {Dollars}.{Cents}";
    public static final String VALIDATION_REGEX = "\\d+\\.\\d{2}";
    public final MoneyInt initValue;
    public final MoneyInt value;

    /**
     * Constructs a {@code Amount}.
     *
     * @param amount and string a valid amount number represented as a decimal to 2dp.
     */
    public Amount(String amount) throws IllegalArgumentException {
        requireNonNull(amount);
        checkArgument(isValidAmount(amount), MESSAGE_CONSTRAINTS);
        String[] args = amount.split("\\.");
        this.initValue = MoneyInt.fromDollarAndCent(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        this.value = initValue;
    }

    /**
     * Constructs a {@code Amount} with specified initial and remaining amount left on the loan.
     *
     * @param initValue Initial loan amount.
     * @param value Current remaining loan amount.
     */
    public Amount(MoneyInt initValue, MoneyInt value) throws IllegalArgumentException {
        requireNonNull(initValue);
        requireNonNull(value);
        this.initValue = initValue;
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid amount number.
     */
    public static boolean isValidAmount(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.value.getStringRepresentationWithFixedDecimalPoint();
    }

    /**
     * Returns this Amount in the format xx.xx/xx.xx, with the initial amount of the loan at the back.
     */
    public String toStringWithInitialAmount() {
        return "$" + this.value.getStringRepresentationWithFixedDecimalPoint() + "/$"
                + this.initValue.getStringRepresentationWithFixedDecimalPoint();
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
        return this.value.equals(otherAmount.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(Amount o) {
        return this.value.getValueTimesOneHundred() - o.value.getValueTimesOneHundred();
    }

    public boolean isRepaid() {
        return this.value.getValueTimesOneHundred() == 0;
    }
}
