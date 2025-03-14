package wanted.model.loan;

import static java.util.Objects.requireNonNull;

import wanted.commons.core.datatypes.Date;

/**
 * A wrapper class to manage the date a loan was taken out
 * Guarantees: immutable; is valid as declared in {@link #isValidLoanDate(String)}
 */

public class LoanDate {

    public static final String MESSAGE_CONSTRAINTS =
            "Loan amounts should only contain numbers, and it should adhere to the format {Dollars}.{Cents}";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * There should be 3 alphanumeric inputs to simulate Day-Month-Year
     * To be updated with the addition of a datetime object
     */
    public static final String VALIDATION_REGEX = "\\p{Alnum}+ \\p{Alnum}+ \\p{Alnum}+";

    public final Date value;
    /**
     * Constructs a {@code Date}.
     *
     * @param date and valid date - following the valid name structure.
     */
    public LoanDate(String date) {
        requireNonNull(date);
        value = new Date(date);
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidLoanDate(String test) {
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
        if (!(other instanceof LoanDate)) {
            return false;
        }

        LoanDate otherDate = (LoanDate) other;
        return value.equals(otherDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
