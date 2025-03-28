package wanted.logic.parser;

import static java.util.Objects.requireNonNull;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.commands.RepayCommand;
import wanted.logic.parser.exceptions.ParseException;

/**
 * Parser to parse repay command
 */
public class RepayCommandParser implements Parser<RepayCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the RepayCommand
     * and returns an RepayCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RepayCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT);

        if (argMultimap.getValue(PREFIX_AMOUNT).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RepayCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_AMOUNT);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RepayCommand.MESSAGE_USAGE), pe);
        }
        MoneyInt returnedAmount = ParserUtil.parseMoneyAmount(argMultimap.getValue(PREFIX_AMOUNT).get());

        return new RepayCommand(index, returnedAmount);
    }
}
