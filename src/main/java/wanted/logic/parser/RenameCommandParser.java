package wanted.logic.parser;

import static java.util.Objects.requireNonNull;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static wanted.logic.parser.CliSyntax.PREFIX_NAME;

import wanted.commons.core.datatypes.Index;
import wanted.logic.commands.BaseEdit;
import wanted.logic.commands.EditCommand;
import wanted.logic.commands.RenameCommand;
import wanted.logic.parser.exceptions.ParseException;

/**
 * Header
 */
public class RenameCommandParser implements Parser<RenameCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the RenameCommand
     * and returns a RenameCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RenameCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        if (!argMultimap.arePrefixesPresent(PREFIX_NAME)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RenameCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, RenameCommand.MESSAGE_USAGE), pe);
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME);
        BaseEdit.EditLoanDescriptor editPersonDescriptor = new BaseEdit.EditLoanDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED); //change this later
        }

        return new RenameCommand(index, editPersonDescriptor);
    }
}
