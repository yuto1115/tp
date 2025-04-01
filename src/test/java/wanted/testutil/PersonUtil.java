package wanted.testutil;

import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;
import static wanted.logic.parser.CliSyntax.PREFIX_NAME;
import static wanted.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import wanted.logic.commands.AddCommand;
import wanted.logic.commands.EditCommand.EditPersonDescriptor;
import wanted.model.loan.Loan;
import wanted.model.tag.Tag;


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

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        //append the string representation {dollar}.{cents}
        descriptor.getAmount().ifPresent(amount -> sb.append(PREFIX_AMOUNT).append(amount).append(" "));
        descriptor.getDate()
            .ifPresent(loanDate -> sb.append(PREFIX_DATE).append(loanDate.value.toString()).append(" "));

        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
