package wanted.logic.parser;

import static java.util.Objects.requireNonNull;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CliSyntax.PREFIX_PHONE;

import wanted.commons.core.datatypes.Index;
import wanted.logic.commands.PhoneCommand;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.Phone;

/**
 * Parse the phone command
 */
public class PhoneCommandParser implements Parser<PhoneCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PhoneCommand
     * and returns an PhoneCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public PhoneCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_PHONE);

        if (!argMultimap.arePrefixesPresent(PREFIX_PHONE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PhoneCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PHONE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, PhoneCommand.MESSAGE_USAGE), pe);
        }
        Phone updatedPhone;
        String phoneNumber = argMultimap.getValue(PREFIX_PHONE).get();
        if (phoneNumber.trim().equalsIgnoreCase(PhoneCommand.DELETE_WORD)) {
            updatedPhone = Phone.EMPTY_PHONE;
        } else {
            updatedPhone = ParserUtil.parsePhone(phoneNumber);
        }

        return new PhoneCommand(index, updatedPhone);
    }
}
