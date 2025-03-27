package wanted.logic.parser;

import wanted.logic.commands.SortCommand;
import wanted.logic.parser.exceptions.ParseException;

/**
 * Sort Command Parser. Would benefit in future if you need multiple sorting options.
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parse sort command
     * @param userInput input
     * @return new SortCommand
     * @throws ParseException handle exception
     */
    @Override
    public SortCommand parse(String userInput) throws ParseException {
        return new SortCommand();
    }
}
