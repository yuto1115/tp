package wanted.model.loan;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import wanted.commons.core.datatypes.Date;

/**
 * A wrapper class to manage the date a loan was taken out
 * Guarantees: immutable; is valid as declared in {@link #isValidLoanDate(String)}
 */

public class LoanDate {

    public static final String MESSAGE_CONSTRAINTS =
            "Date value should be in format YYYY-MM-DD. (e.g. d/2024-01-21)";
    /*
     * The first character of the date must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * There should be 3 alphanumeric inputs to simulate Day-Month-Year
     * To be updated with the addition of a datetime object
     */
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

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
     * Returns true if a given string is a valid loan date.
     */
    public static boolean isValidLoanDate(String test) {
        try {
            LocalDate.parse(test, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
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
