package wanted.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static wanted.storage.JsonAdaptedLoan.LOAN_EXCESS_REPAYMENT_MESSAGE;
import static wanted.storage.JsonAdaptedLoan.MISSING_FIELD_MESSAGE_FORMAT;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import wanted.commons.exceptions.IllegalValueException;
import wanted.logic.parser.ParserUtil;
import wanted.model.loan.Name;

public class JsonAdaptedLoanTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_TAG = "#friend";
    private static final JsonAdaptedLoanTransaction INVALID_AMOUNT_TRANSACTION =
            new JsonAdaptedLoanTransaction("add", "10.0", "1st April 2020");
    private static final List<JsonAdaptedLoanTransaction> INVALID_TRANSACTIONS_SEQUENCE =
            new ArrayList<>(Arrays.asList(
                    new JsonAdaptedLoanTransaction("add", "10.00", "1st April 2020"),
                    new JsonAdaptedLoanTransaction("repay", "12.00", "2nd April 2020"),
                    new JsonAdaptedLoanTransaction("add", "10.00", "3rd April 2020")
            ));

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final List<JsonAdaptedLoanTransaction> VALID_TRANSACTIONS =
            BENSON.getLoanAmount().getTransactionHistoryCopy().stream()
                    .map(JsonAdaptedLoanTransaction::new)
                    .toList();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validLoanDetails_returnsLoan() throws Exception {
        assertEquals(BENSON, new JsonAdaptedLoan(BENSON).toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedLoan loan = new JsonAdaptedLoan(INVALID_NAME, VALID_TRANSACTIONS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, loan::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedLoan loan = new JsonAdaptedLoan(null, VALID_TRANSACTIONS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, loan::toModelType);
    }

    @Test
    public void toModelType_invalidTransactions_throwsIllegalValueException() {
        // include invalid transaction
        {
            List<JsonAdaptedLoanTransaction> transactions = new ArrayList<>(VALID_TRANSACTIONS);
            transactions.add(INVALID_AMOUNT_TRANSACTION);
            JsonAdaptedLoan loan = new JsonAdaptedLoan(VALID_NAME, transactions, VALID_TAGS);
            String expectedMessage = ParserUtil.MESSAGE_INVALID_MONEY_AMOUNT;
            assertThrows(IllegalValueException.class, expectedMessage, loan::toModelType);
        }
        // invalid transactions as a sequence
        {
            JsonAdaptedLoan loan = new JsonAdaptedLoan(VALID_NAME, INVALID_TRANSACTIONS_SEQUENCE, VALID_TAGS);
            String expectedMessage = LOAN_EXCESS_REPAYMENT_MESSAGE;
            assertThrows(IllegalValueException.class, expectedMessage, loan::toModelType);
        }
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedLoan loan = new JsonAdaptedLoan(VALID_NAME, VALID_TRANSACTIONS, invalidTags);
        assertThrows(IllegalValueException.class, loan::toModelType);
    }
}
