package wanted.logic.parser;

import static java.util.Objects.requireNonNull;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CliSyntax.PREFIX_INDEX;

import wanted.commons.core.datatypes.Index;
import wanted.logic.commands.DelhistCommand;
import wanted.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DelhistCommand object
 */
public class DelhistCommandParser implements Parser<DelhistCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DelhistCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INDEX);

        if (!argumentMultimap.arePrefixesPresent(PREFIX_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DelhistCommand.MESSAGE_USAGE));
        }

        argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX);

        Index loanIndex;
        Index transactionIndex;
        try {
            loanIndex = ParserUtil.parseIndex(argumentMultimap.getPreamble());
            transactionIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_INDEX).get());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DelhistCommand.MESSAGE_USAGE));
        }

        return new DelhistCommand(loanIndex, transactionIndex);
    }

}
