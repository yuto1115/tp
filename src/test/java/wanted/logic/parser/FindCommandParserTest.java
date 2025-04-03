package wanted.logic.parser;

import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CommandParserTestUtil.assertParseFailure;
import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import wanted.logic.Messages;
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
    public void parse_disabledCommand_failure() {
        assumeFalse(FindCommand.IS_ENABLED);
        String userInput = "find";
        assertParseFailure(parser, userInput, Messages.MESSAGE_COMMAND_DISABLED);
    }

}
