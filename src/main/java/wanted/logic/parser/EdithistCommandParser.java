package wanted.logic.parser;

import static java.util.Objects.requireNonNull;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;
import static wanted.logic.parser.CliSyntax.PREFIX_INDEX;

import wanted.commons.core.datatypes.Index;
import wanted.logic.commands.EdithistCommand;
import wanted.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EdithistCommand object
 */
public class EdithistCommandParser implements Parser<EdithistCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EdithistCommand
     * and returns an EdithistCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EdithistCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argumentMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_AMOUNT, PREFIX_DATE);

        if (!argumentMultimap.arePrefixesPresent(PREFIX_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EdithistCommand.MESSAGE_USAGE));
        }

        argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX, PREFIX_AMOUNT, PREFIX_DATE);

        Index loanIndex;
        Index transactionIndex;
        try {
            loanIndex = ParserUtil.parseIndex(argumentMultimap.getPreamble());
            transactionIndex = ParserUtil.parseIndex(argumentMultimap.getValue(PREFIX_INDEX).get());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EdithistCommand.MESSAGE_USAGE));
        }

        EdithistCommand.EditTransactionDescriptor editTransactionDescriptor =
                new EdithistCommand.EditTransactionDescriptor();

        if (argumentMultimap.getValue(PREFIX_AMOUNT).isPresent()) {
            editTransactionDescriptor.setAmount(
                    ParserUtil.parseMoneyAmount(argumentMultimap.getValue(PREFIX_AMOUNT).get()));
        }
        if (argumentMultimap.getValue(PREFIX_DATE).isPresent()) {
            editTransactionDescriptor.setDate(ParserUtil.parseDate(argumentMultimap.getValue(PREFIX_DATE).get()));
        }

        return new EdithistCommand(loanIndex, transactionIndex, editTransactionDescriptor);
    }

}
