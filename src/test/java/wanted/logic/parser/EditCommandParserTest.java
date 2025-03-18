package wanted.logic.parser;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.commands.CommandTestUtil.AMOUNT_DESC_AMY;
import static wanted.logic.commands.CommandTestUtil.AMOUNT_DESC_BOB;
import static wanted.logic.commands.CommandTestUtil.DATE_DESC_AMY;
import static wanted.logic.commands.CommandTestUtil.INVALID_AMOUNT_DESC;
import static wanted.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static wanted.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static wanted.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static wanted.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static wanted.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static wanted.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static wanted.logic.commands.CommandTestUtil.VALID_AMOUNT_AMY;
import static wanted.logic.commands.CommandTestUtil.VALID_DATE_AMY;
import static wanted.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;
import static wanted.logic.parser.CliSyntax.PREFIX_TAG;
import static wanted.logic.parser.CommandParserTestUtil.assertParseFailure;
import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.logic.commands.EditCommand;
import wanted.logic.commands.EditCommand.EditPersonDescriptor;
import wanted.model.loan.Name;
import wanted.model.tag.Tag;
import wanted.testutil.EditPersonDescriptorBuilder;

/**
 * Note this is disabled in the MVP
 */
public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assumeTrue(EditCommand.IS_ENABLED);
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assumeTrue(EditCommand.IS_ENABLED);
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assumeTrue(EditCommand.IS_ENABLED);
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS); // invalid tag

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Loan} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_AMOUNT_DESC + INVALID_DATE_DESC,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        assumeTrue(EditCommand.IS_ENABLED);
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_DESC_HUSBAND + NAME_DESC_AMY + TAG_DESC_FRIEND
                + AMOUNT_DESC_AMY + DATE_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withAmount(VALID_AMOUNT_AMY).withLoanDate(VALID_DATE_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        assumeTrue(EditCommand.IS_ENABLED);
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + AMOUNT_DESC_AMY + DATE_DESC_AMY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withLoanDate(VALID_DATE_AMY).withAmount(VALID_AMOUNT_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        assumeTrue(EditCommand.IS_ENABLED);
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        //amount
        userInput = targetIndex.getOneBased() + AMOUNT_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withAmount(VALID_AMOUNT_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        //date
        userInput = targetIndex.getOneBased() + DATE_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder().withLoanDate(VALID_DATE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        assumeTrue(EditCommand.IS_ENABLED);
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_AMOUNT_DESC + AMOUNT_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AMOUNT));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + AMOUNT_DESC_BOB + INVALID_AMOUNT_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AMOUNT));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + AMOUNT_DESC_AMY + DATE_DESC_AMY
                + TAG_DESC_FRIEND + AMOUNT_DESC_AMY + DATE_DESC_AMY + TAG_DESC_FRIEND
                + AMOUNT_DESC_BOB + DATE_DESC_AMY + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AMOUNT, PREFIX_DATE));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_AMOUNT_DESC + INVALID_DATE_DESC
                + INVALID_AMOUNT_DESC + INVALID_DATE_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AMOUNT, PREFIX_DATE));
    }

    @Test
    public void parse_resetTags_success() {
        assumeTrue(EditCommand.IS_ENABLED);
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
