package wanted.logic.parser;

import static wanted.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static wanted.logic.parser.CliSyntax.PREFIX_DATE;
import static wanted.logic.parser.CliSyntax.PREFIX_INDEX;
import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.commands.EdithistCommand;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.LoanDate;

public class EdithistCommandParserTest {

    private static final String VALID_DESC_INDEX = PREFIX_INDEX + "2";
    private static final String VALID_DESC_AMOUNT = PREFIX_AMOUNT + "20.15";
    private static final String VALID_DESC_DATE = PREFIX_DATE + "1st Apr 2024";

    private static final String INVALID_DESC_INDEX_1 = PREFIX_INDEX + "0";
    private static final String INVALID_DESC_INDEX_2 = PREFIX_INDEX + "-1";
    private static final String INVALID_DESC_INDEX_3 = PREFIX_INDEX + "a";
    private static final String INVALID_DESC_AMOUNT_1 = PREFIX_AMOUNT + "-20.15";
    private static final String INVALID_DESC_AMOUNT_2 = PREFIX_AMOUNT + "20.1";
    private static final String INVALID_DESC_DATE_1 = PREFIX_DATE + "1st Apr";
    private static final String INVALID_DESC_DATE_2 = PREFIX_DATE + "01-04-2024";

    private EdithistCommandParser parser = new EdithistCommandParser();

    @Test
    public void parse_validFullArgs_success() {
        EdithistCommand.EditTransactionDescriptor expectedDescriptor =
                new EdithistCommand.EditTransactionDescriptor();
        expectedDescriptor.setAmount(MoneyInt.fromCent(2015));
        expectedDescriptor.setDate(new LoanDate("1st Apr 2024"));
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_INDEX
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertParseSuccess(parser, input, new EdithistCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                    expectedDescriptor));
        }
        // different order
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE
                    + " " + VALID_DESC_INDEX;
            assertParseSuccess(parser, input, new EdithistCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                    expectedDescriptor));
        }
    }

    @Test
    public void parse_validPartialArgs_success() {
        // no edition
        {
            EdithistCommand.EditTransactionDescriptor expectedDescriptor =
                    new EdithistCommand.EditTransactionDescriptor();
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_INDEX;
            assertParseSuccess(parser, input, new EdithistCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                    expectedDescriptor));
        }
        // only amount
        {
            EdithistCommand.EditTransactionDescriptor expectedDescriptor =
                    new EdithistCommand.EditTransactionDescriptor();
            expectedDescriptor.setAmount(MoneyInt.fromCent(2015));
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_INDEX
                    + " " + VALID_DESC_AMOUNT;
            assertParseSuccess(parser, input, new EdithistCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                    expectedDescriptor));
        }
        // only date
        {
            EdithistCommand.EditTransactionDescriptor expectedDescriptor =
                    new EdithistCommand.EditTransactionDescriptor();
            expectedDescriptor.setDate(new LoanDate("1st Apr 2024"));
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_INDEX
                    + " " + VALID_DESC_DATE;
            assertParseSuccess(parser, input, new EdithistCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                    expectedDescriptor));
        }
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_repeatedIndex_failure() {
        String input = INDEX_FIRST_PERSON.getOneBased()
                + " " + VALID_DESC_INDEX
                + " " + VALID_DESC_INDEX
                + " " + VALID_DESC_AMOUNT
                + " " + VALID_DESC_DATE;
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_repeatedAmount_failure() {
        String input = INDEX_FIRST_PERSON.getOneBased()
                + " " + VALID_DESC_INDEX
                + " " + VALID_DESC_AMOUNT
                + " " + VALID_DESC_AMOUNT
                + " " + VALID_DESC_DATE;
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_repeatedDate_failure() {
        String input = INDEX_FIRST_PERSON.getOneBased()
                + " " + VALID_DESC_INDEX
                + " " + VALID_DESC_AMOUNT
                + " " + VALID_DESC_DATE
                + " " + VALID_DESC_DATE;
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        {
            String input = "0"
                    + " " + VALID_DESC_INDEX
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
        {
            String input = "-1"
                    + " " + VALID_DESC_INDEX
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
        {
            String input = "a"
                    + " " + VALID_DESC_INDEX
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
    }

    @Test
    public void parse_invalidIndex_failure() {
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + INVALID_DESC_INDEX_1
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + INVALID_DESC_INDEX_2
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + INVALID_DESC_INDEX_3
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
    }

    @Test
    public void parse_invalidAmount_failure() {
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_INDEX
                    + " " + INVALID_DESC_AMOUNT_1
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_INDEX
                    + " " + INVALID_DESC_AMOUNT_2
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
    }

    @Test
    public void parse_invalidDate_failure() {
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_INDEX
                    + " " + VALID_DESC_AMOUNT
                    + " " + INVALID_DESC_DATE_1;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_INDEX
                    + " " + VALID_DESC_AMOUNT
                    + " " + INVALID_DESC_DATE_2;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        String input = VALID_DESC_INDEX
                + " " + VALID_DESC_AMOUNT
                + " " + VALID_DESC_DATE;
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        {
            String input = INDEX_FIRST_PERSON.getOneBased()
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
        {
            String input = "1 2"
                    + " " + VALID_DESC_AMOUNT
                    + " " + VALID_DESC_DATE;
            assertThrows(ParseException.class, () -> parser.parse(input));
        }
    }

    @Test
    public void parse_redundancy_throwsParseException() {
        String input = INDEX_FIRST_PERSON.getOneBased()
                + " " + VALID_DESC_INDEX
                + " " + VALID_DESC_AMOUNT
                + " " + VALID_DESC_DATE
                + " t/friends";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }
}
