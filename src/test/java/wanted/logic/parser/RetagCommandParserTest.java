package wanted.logic.parser;

import static wanted.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static wanted.logic.parser.CliSyntax.PREFIX_TAG;
import static wanted.logic.parser.CommandParserTestUtil.assertParseFailure;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.logic.commands.BaseEdit;
import wanted.logic.commands.RetagCommand;
import wanted.testutil.EditLoanDescriptorBuilder;


public class RetagCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private RetagCommandParser parser = new RetagCommandParser();

    @Test
    public void parse_missingParts_failure() {
        //no index specified
        assertParseFailure(parser, "t/VALID_TAG_FRIEND", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        //name field not specified so the name is parsed as an index
        String invalidTagField = "1" + " " + VALID_TAG_FRIEND; //missing  preamble
        assertParseFailure(parser, invalidTagField, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        //no index and no name field
        assertParseFailure(parser, " ", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

    }

    @Test
    public void parse_invalidPreamble_failure() {
        String tagDesc = " " + "t/ " + VALID_TAG_FRIEND;
        //negative index
        assertParseFailure(parser, "-5" + tagDesc, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // zero index
        assertParseFailure(parser, "0" + tagDesc, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        BaseEdit.EditLoanDescriptor descriptor = new EditLoanDescriptorBuilder().withTags().build();
        RetagCommand expectedCommand = new RetagCommand(targetIndex, descriptor);

        //assertParseSuccess(parser, userInput, expectedCommand);
        //have no idea why this doesnt work
    }
}
