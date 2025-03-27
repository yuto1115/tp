package wanted.logic.parser;

import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import wanted.logic.commands.CommandTestUtil;
import wanted.logic.commands.IncreaseCommand;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.Amount;

public class IncreaseCommandTest {

    private IncreaseCommandParser increaseCommand = new IncreaseCommandParser();

    @Test
    public void parse_validArgs_returnsIncreaseCommand() {
        Amount amt = new Amount("10.00");
        String input = INDEX_FIRST_PERSON.getOneBased() + " " + "l/" + CommandTestUtil.VALID_AMOUNT_AMY;
        assertParseSuccess(increaseCommand, "1 l/10.00", new IncreaseCommand(INDEX_FIRST_PERSON, amt));
    }

    @Test
    public void parse_repeatedAmount_failure() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 l/10.00 l/10.00"));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("-1 l/10.00"));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 a/100.00"));
    }

    @Test
    public void parse_missingAmount_throwsParseException() {
        assertThrows(NoSuchElementException.class, () -> increaseCommand.parse("1"));
    }

    @Test
    public void parse_negativeAmount_throwsParseException() {
        assertThrows(ParseException.class, () -> increaseCommand.parse("1 l/-100.00")
        );
    }
}
