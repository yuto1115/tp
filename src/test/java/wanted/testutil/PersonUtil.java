package wanted.testutil;

import static wanted.logic.parser.CliSyntax.PREFIX_NAME;
import static wanted.logic.parser.CliSyntax.PREFIX_TAG;

import wanted.logic.commands.AddCommand;
import wanted.model.loan.Loan;


/**
 * A utility class for Loan.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code loan}.
     */
    public static String getAddCommand(Loan person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code loan}'s details.
     */
    public static String getPersonDetails(Loan person) {
        //todo: make the string representation of amount and date easier to access?
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        //sb.append(PREFIX_AMOUNT + person.getLoanAmount().toString() + " ");
        person.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
