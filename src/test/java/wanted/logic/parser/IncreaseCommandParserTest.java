package wanted.logic.parser;

import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.commands.CommandTestUtil;
import wanted.logic.commands.IncreaseCommand;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.LoanDate;

public class IncreaseCommandParserTest {
    private IncreaseCommandParser increaseCommand = new IncreaseCommandParser();

    @Test
    public void parse_validArgs_returnsIncreaseCommand() {
        MoneyInt amt = MoneyInt.fromDollarAndCent(10, 10);
        LoanDate date = new LoanDate(CommandTestUtil.VALID_DATE_AMY);
        String input = INDEX_FIRST_PERSON.getOneBased()
                + " l/" + "10.00"
                + " d/" + CommandTestUtil.VALID_DATE_AMY;
        assertParseSuccess(increaseCommand, input, new IncreaseCommand(INDEX_FIRST_PERSON, amt, date));
    }

    @Test
    public void parse_repeatedAmount_failure() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 l/10.00 l/10.00 d/10th December 2024"));
    }

    @Test
    public void parse_repeatedDate_failure() {
        assertThrows(ParseException.class, () ->
                increaseCommand.parse("1 l/10.00 d/10 Dec 2024 d/10th December 2024"));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("-1 l/10.00 10th December 2024"));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 a/100.00 10th December 2024"));
    }

    @Test
    public void parse_missingAmount_throwsParseException() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 d/10th December 2024"));
    }

    @Test
    public void parse_missingDate_throwsParseException() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 l/10.00"));
    }

    @Test
    public void parse_negativeAmount_throwsParseException() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 l/-100.00 d/10th December 2024")
        );
    }

    @Test
    public void parse_invalidDateFormat_throwsParseException() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 l/10.00 d/10th 2024")
        );
    }
}
