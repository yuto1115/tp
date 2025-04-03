package wanted.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import wanted.logic.parser.Prefix;
import wanted.model.loan.Loan;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The loan index provided is invalid";
    public static final String MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX =
            "The transaction index provided is invalid";
    public static final String MESSAGE_EXCESS_REPAYMENT_IN_HISTORY =
            "Invalid transaction update: This update would result in a negative remaining loan balance "
                    + "at some point in the history.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_COMMAND_DISABLED = "This command is disabled in the MVP";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code loan} for display to the user.
     */
    public static String format(Loan person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Total Amount: ")
                .append(person.getLoanAmount().getTotalAmount().getStringRepresentationWithFixedDecimalPoint())
                .append("; Remaining Amount: ")
                .append(person.getLoanAmount().getRemainingAmount().getStringRepresentationWithFixedDecimalPoint())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
