package wanted.logic.parser;

import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.parser.CommandParserTestUtil.assertParseFailure;
//import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;

//import java.util.Arrays;
import org.junit.jupiter.api.Test;

import wanted.logic.commands.FindCommand;
//import wanted.model.loan.NameContainsKeywordsPredicate;

/**
 * Unit tests for {@code FindCommandParser}.
 */
public class FindCommandParserTest {

    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        //TODO: write FindCommandParserTest for valid arguments
    }
}
