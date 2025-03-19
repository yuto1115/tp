package wanted.logic.parser;

import wanted.commons.core.datatypes.Index;
import wanted.logic.commands.RepayCommand;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.Amount;

import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CliSyntax.*;

public class RepayCommandParser implements Parser<RepayCommand> {
    public RepayCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_AMOUNT);
        Index index;
        Amount returnedAmount;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            String returnedAmountString = "";
            if (argMultimap.getValue(PREFIX_AMOUNT).isPresent()) {
                returnedAmountString = argMultimap.getValue(PREFIX_AMOUNT).get();
            }
            returnedAmount = ParserUtil.parseAmount(returnedAmountString);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RepayCommand.MESSAGE_USAGE), pe);
        }
        return new RepayCommand(index, returnedAmount);
    }
}
