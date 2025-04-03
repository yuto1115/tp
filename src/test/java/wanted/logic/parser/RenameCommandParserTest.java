package wanted.logic.parser;

import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static wanted.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static wanted.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static wanted.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static wanted.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import wanted.logic.commands.RenameCommand;
import wanted.model.loan.Name;

public class RenameCommandParserTest {
    public static final String INVALID_RENAME_COMMAND_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RenameCommand.MESSAGE_USAGE);
    private RenameCommandParser parser = new RenameCommandParser();

    @Test
    public void parse_missingParts_failure() {
        //no index specified
        assertParseFailure(parser, NAME_DESC_AMY, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        //name field not specified so the name is parsed as an index
        String invalidNameField = "1" + " " + VALID_NAME_AMY; //missing  preamble
        assertParseFailure(parser, invalidNameField, INVALID_RENAME_COMMAND_FORMAT);

        //no index and no name field
        assertParseFailure(parser, " ", INVALID_RENAME_COMMAND_FORMAT);

    }

    @Test
    public void parse_invalidPreamble_failure() {

        //negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", INVALID_RENAME_COMMAND_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS);
    }

}
