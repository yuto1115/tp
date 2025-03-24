package wanted.commons.core.datatypes;

import wanted.commons.exceptions.IllegalValueException;
import wanted.commons.util.AppUtil;
import wanted.commons.util.ToStringBuilder;

/**
 * Represents a *non-negative* number which is a multiple of 1/100 (0.01).
 * <p>
 * {@code MoneyInt} is to be used for storing the exact value of some quantity regarding money,
 * assuming that the smallest unit of money is 1/100.
 */
public class MoneyInt {
    private final int valueTimesOneHundred; // 100 times the original value, which is an integer

    /**
     * Private constructor.
     * <p>
     * MoneyInt can only be created by calling {@link MoneyInt#fromDollarAndCent(int, int)}.
     */
    private MoneyInt(int valueTimesOneHundred) {
        assert valueTimesOneHundred >= 0;
        this.valueTimesOneHundred = valueTimesOneHundred;
    }

    /**
     * Returns the original value times one-hundred as {@code int}.
     */
    public int getValueTimesOneHundred() {
        return valueTimesOneHundred;
    }

    /**
     * Returns the {@code String} representation of the original value
     * with exactly two digits after the decimal point.
     */
    public String getStringRepresentationWithFixedDecimalPoint() {
        int beforeDecimalPoint = valueTimesOneHundred / 100;
        int afterDecimalPoint = valueTimesOneHundred % 100;
        return String.format("%d.%02d", beforeDecimalPoint, afterDecimalPoint);
    }

    /**
     * Creates a new {@code MoneyInt} from the amounts of dollars and cents.
     * That is, creates a new {@code MoneyInt} with the value equal to {@code dollar} + {@code cent} * 0.01.
     *
     * @throws IllegalValueException if either of the following conditions is not satisfied:
     *     - {@code dollar} is a non-negative integer
     *     - {@code cent} is a non-negative integer between 0 and 99, inclusive.
     */

    public static MoneyInt fromDollarAndCent(int dollar, int cent) throws IllegalArgumentException {
        AppUtil.checkArgument(dollar >= 0, "The dollar value cannot be negative");
        AppUtil.checkArgument(cent >= 0, "The cent value cannot be negative");
        AppUtil.checkArgument(cent <= 99, "The cent value cannot exceed 99");

        return new MoneyInt(dollar * 100 + cent);
    }

    /**
     * Creates a new {@code MoneyInt} from the amounts of cents.
     * That is, creates a new {@code MoneyInt} with the value equal to {@code cent} * 0.01.
     *
     * @throws IllegalValueException if either of the following conditions is not satisfied:
     *     - {@code cent} is a non-negative integer.
     */

    public static MoneyInt fromCent(int cent) throws IllegalArgumentException {
        AppUtil.checkArgument(cent >= 0, "The cent value cannot be negative");

        return new MoneyInt(cent);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MoneyInt)) {
            return false;
        }

        MoneyInt otherMoneyInt = (MoneyInt) other;
        return valueTimesOneHundred == otherMoneyInt.valueTimesOneHundred;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("valueTimesOneHundred", valueTimesOneHundred).toString();
    }
}
