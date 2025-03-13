package wanted.model.loan;

import wanted.commons.core.datatypes.Date;

import static java.util.Objects.requireNonNull;

public class LoanDate {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";
    public final Date value;

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * To be updated with the addition of a datetime object
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

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
    public static boolean isValidDate(String test) {
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
