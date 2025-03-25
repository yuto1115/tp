package wanted.logic.parser;

import static wanted.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static wanted.logic.commands.CommandTestUtil.AMOUNT_DESC_AMY;
import static wanted.logic.commands.CommandTestUtil.AMOUNT_DESC_BOB;
import static wanted.logic.commands.CommandTestUtil.DATE_DESC_AMY;
import static wanted.logic.commands.CommandTestUtil.DATE_DESC_BOB;
import static wanted.logic.commands.CommandTestUtil.INVALID_AMOUNT_DESC;
import static wanted.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static wanted.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static wanted.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static wanted.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static wanted.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static wanted.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static wanted.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static wanted.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static wanted.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static wanted.logic.commands.CommandTestUtil.VALID_AMOUNT_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_DATE_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;
import static wanted.logic.parser.CliSyntax.PREFIX_NAME;
import static wanted.logic.parser.CommandParserTestUtil.assertParseFailure;
import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static wanted.testutil.TypicalPersons.AMY;
import static wanted.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import wanted.logic.Messages;
import wanted.logic.commands.AddCommand;
import wanted.model.loan.Amount;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanDate;
import wanted.model.loan.Name;
import wanted.model.tag.Tag;
import wanted.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Loan expectedPerson = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB
                + AMOUNT_DESC_BOB + DATE_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Loan expectedPersonMultipleTags = new PersonBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + AMOUNT_DESC_BOB + DATE_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + AMOUNT_DESC_BOB
                + DATE_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        //multiple amounts
        assertParseFailure(parser, AMOUNT_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AMOUNT));

        //multiple dates
        assertParseFailure(parser, DATE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));


        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + AMOUNT_DESC_AMY + NAME_DESC_AMY + DATE_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME,
                        PREFIX_DATE, PREFIX_AMOUNT));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid amount
        assertParseFailure(parser, INVALID_AMOUNT_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AMOUNT));

        // invalid date
        assertParseFailure(parser, INVALID_DATE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));


        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid amount
        assertParseFailure(parser, validExpectedPersonString + INVALID_AMOUNT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AMOUNT));

        // invalid date
        assertParseFailure(parser, validExpectedPersonString + INVALID_DATE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Loan expectedPerson = new PersonBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY
                        + AMOUNT_DESC_AMY + DATE_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + AMOUNT_DESC_BOB + DATE_DESC_BOB,
                expectedMessage);

        // missing amount prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_AMOUNT_BOB + DATE_DESC_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_AMOUNT_BOB + VALID_DATE_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC
                + AMOUNT_DESC_BOB + DATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid amount
        assertParseFailure(parser, NAME_DESC_BOB
                + INVALID_AMOUNT_DESC + DATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Amount.MESSAGE_CONSTRAINTS);

        // invalid date
        // assertParseFailure(parser, NAME_DESC_BOB + AMOUNT_DESC_BOB
        //         + INVALID_DATE_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, LoanDate.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB
                + AMOUNT_DESC_BOB + DATE_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + INVALID_AMOUNT_DESC + DATE_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + AMOUNT_DESC_BOB
                        + DATE_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
