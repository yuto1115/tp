package wanted.logic.parser;

import static wanted.logic.parser.CliSyntax.PREFIX_INDEX;
import static wanted.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import wanted.logic.commands.DelhistCommand;
import wanted.logic.parser.exceptions.ParseException;

public class DelhistCommandParserTest {
    private DelhistCommandParser parser = new DelhistCommandParser();

    @Test
    public void parse_validArgs_returnDelhistCommand() {
        String input = INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_INDEX + INDEX_SECOND_PERSON.getOneBased();
        assertParseSuccess(parser, input, new DelhistCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_repeatedIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 i/2 i/3"));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse("1 i/0"));
        assertThrows(ParseException.class, () -> parser.parse("1 i/-1"));
        assertThrows(ParseException.class, () -> parser.parse("1 i/a"));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("0 i/2"));
        assertThrows(ParseException.class, () -> parser.parse("-1 i/2"));
        assertThrows(ParseException.class, () -> parser.parse("a i/2"));
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1"));
        assertThrows(ParseException.class, () -> parser.parse("1 2"));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("i/1"));
        assertThrows(ParseException.class, () -> parser.parse("i/1 i/2"));
    }

    @Test
    public void parse_redundancy_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 i/2 l/10.00"));
        assertThrows(ParseException.class, () -> parser.parse("1 l/10.00 i/2"));
    }
}
