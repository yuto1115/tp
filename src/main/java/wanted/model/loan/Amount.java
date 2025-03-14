package wanted.model.loan;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.commons.exceptions.IllegalValueException;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.AppUtil.checkArgument;

public class Amount {

    public static final String MESSAGE_CONSTRAINTS =
            "Loan amounts should only contain numbers, and it should adhere to the format {Dollars}.{Cents}";
    public static final String VALIDATION_REGEX = "\\d+\\.\\d{2}";
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
        value = MoneyInt.fromDollarAndCent(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidAmount(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value.toString();
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
        return value.equals(otherAmount.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
