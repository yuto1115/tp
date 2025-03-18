package wanted.logic.parser;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CommandParserTestUtil.assertParseFailure;
import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import wanted.logic.commands.FindCommand;
import wanted.model.loan.NameContainsKeywordsPredicate;

/**
 * Note this is disabled in the MVP
 */

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assumeTrue(FindCommand.IS_ENABLED);
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        assumeTrue(FindCommand.IS_ENABLED);
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

}
