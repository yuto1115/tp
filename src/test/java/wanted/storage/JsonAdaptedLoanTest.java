package wanted.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalPersons.BENSON;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class JsonAdaptedLoanTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_AMOUNT = "12.hi";
    // private static final String INVALID_DATE = "Febru@ry #14!";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_AMOUNT = BENSON.getLoanAmount().toString();
    // private static final String VALID_DATE = BENSON.getLoanDate().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedLoan person = new JsonAdaptedLoan(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    /* todo: recover
    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedLoan person =
                new JsonAdaptedLoan(INVALID_NAME, VALID_AMOUNT, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedLoan person = new JsonAdaptedLoan(null,
                VALID_AMOUNT, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAmount_throwsIllegalValueException() {
        JsonAdaptedLoan person =
                new JsonAdaptedLoan(VALID_NAME, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, LoanAmount.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAmount_throwsIllegalValueException() {
        JsonAdaptedLoan person =
                new JsonAdaptedLoan(VALID_NAME,
                        INVALID_AMOUNT, VALID_TAGS);
        String expectedMessage = LoanAmount.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedLoan person =
                new JsonAdaptedLoan(VALID_NAME,
                         VALID_AMOUNT, VALID_TAGS);
        String expectedMessage = LoanDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedLoan person =
                new JsonAdaptedLoan(VALID_NAME,
                        VALID_AMOUNT, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }
    */
}
