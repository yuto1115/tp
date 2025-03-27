package wanted.logic.parser;

import static java.util.Objects.requireNonNull;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;

import wanted.commons.core.datatypes.Index;
import wanted.logic.commands.IncreaseCommand;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.Amount;

/**
 * Parses input arguments and creates a new IncreaseCommand object
 */
public class IncreaseCommandParser implements Parser<IncreaseCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of IncreaseCommand
     * and returns an IncreaseCommand object for execution
     * @throws ParseException given incorrect input
     */
    public IncreaseCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT);
        Index index;
        String updatedAmount = "";
        try {
            index = ParserUtil.parseIndex(argumentMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, IncreaseCommand.MESSAGE_USAGE));
        }
        argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_AMOUNT);
        Amount amount = ParserUtil.parseAmount(argumentMultimap.getValue(PREFIX_AMOUNT).get());

        return new IncreaseCommand(index, amount);
    }
}
