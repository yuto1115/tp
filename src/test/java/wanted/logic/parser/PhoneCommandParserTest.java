package wanted.logic.parser;

import static wanted.logic.parser.CliSyntax.PREFIX_PHONE;
import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import wanted.logic.commands.CommandTestUtil;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.Phone;
import wanted.logic.commands.PhoneCommand;

public class PhoneCommandParserTest {
    private final PhoneCommandParser parser = new PhoneCommandParser();

    @Test
    public void parse_validArgs_returnPhoneCommand() {
        String input = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PHONE + CommandTestUtil.VALID_PHONE;
        assertParseSuccess(parser, input, new PhoneCommand(INDEX_FIRST_PERSON, new Phone(CommandTestUtil.VALID_PHONE)));
    }

    @Test
    public void parse_validDeleteArgs_returnPhoneCommand() {
        String input = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PHONE + PhoneCommand.DELETE_WORD;
        assertParseSuccess(parser, input, new PhoneCommand(INDEX_FIRST_PERSON, Phone.EMPTY_PHONE));
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("0 p/@"));
        assertThrows(ParseException.class, () -> parser.parse("-1 p/*"));
        assertThrows(ParseException.class, () -> parser.parse("a p/2"));
    }

    @Test
    public void parse_repeatedPhoneNumber_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 p/2345 p/6789"));
    }

}
