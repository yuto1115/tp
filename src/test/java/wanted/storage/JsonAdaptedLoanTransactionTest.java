package wanted.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static wanted.storage.JsonAdaptedLoanTransaction.MISSING_FIELD_MESSAGE_FORMAT;
import static wanted.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import wanted.commons.exceptions.IllegalValueException;
import wanted.logic.parser.ParserUtil;
import wanted.logic.parser.exceptions.ParseException;
import wanted.model.loan.LoanDate;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.LoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;

public class JsonAdaptedLoanTransactionTest {
    private static final String INVALID_TYPE = "increase";
    private static final String INVALID_AMOUNT = "12.0";
    private static final String INVALID_DATE = "02-14";

    private static final String[] VALID_TYPES = new String[]{"add", "repay"};
    private static final String VALID_AMOUNT = "12.05";
    private static final String VALID_DATE = "2025-02-14";

    private static final LoanTransaction VALID_ADD_TRANSACTION;
    private static final LoanTransaction VALID_REPAY_TRANSACTION;

    static {
        try {
            VALID_ADD_TRANSACTION = new AddLoanTransaction(
                    ParserUtil.parseMoneyAmount(VALID_AMOUNT),
                    new LoanDate(VALID_DATE)
            );
            VALID_REPAY_TRANSACTION = new RepayLoanTransaction(
                    ParserUtil.parseMoneyAmount(VALID_AMOUNT),
                    new LoanDate(VALID_DATE)
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void toModelType_validLoanTransactionDetails_returnsLoanTransaction() throws Exception {
        // from LoanTransaction object
        assertEquals(VALID_ADD_TRANSACTION, new JsonAdaptedLoanTransaction(VALID_ADD_TRANSACTION).toModelType());
        assertEquals(VALID_REPAY_TRANSACTION, new JsonAdaptedLoanTransaction(VALID_REPAY_TRANSACTION).toModelType());

        // from details
        assertEquals(VALID_ADD_TRANSACTION,
                new JsonAdaptedLoanTransaction(VALID_TYPES[0], VALID_AMOUNT, VALID_DATE).toModelType());
        assertEquals(VALID_REPAY_TRANSACTION,
                new JsonAdaptedLoanTransaction(VALID_TYPES[1], VALID_AMOUNT, VALID_DATE).toModelType());
    }

    @Test
    public void toModelType_invalidType_throwsIllegalValueException() {
        JsonAdaptedLoanTransaction transaction =
                new JsonAdaptedLoanTransaction(INVALID_TYPE, VALID_AMOUNT, VALID_DATE);
        String expectedMessage = "Unknown LoanTransaction type: " + INVALID_TYPE;
        assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullType_throwsIllegalValueException() {
        JsonAdaptedLoanTransaction transaction =
                new JsonAdaptedLoanTransaction(null, VALID_AMOUNT, VALID_DATE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "type");
        assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidAmount_throwsIllegalValueException() {
        JsonAdaptedLoanTransaction transaction =
                new JsonAdaptedLoanTransaction(VALID_TYPES[0], INVALID_AMOUNT, VALID_DATE);
        String expectedMessage = ParserUtil.MESSAGE_INVALID_MONEY_AMOUNT;
        assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullAmount_throwsIllegalValueException() {
        JsonAdaptedLoanTransaction transaction =
                new JsonAdaptedLoanTransaction(VALID_TYPES[0], null, VALID_DATE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "amount");
        assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedLoanTransaction transaction =
                new JsonAdaptedLoanTransaction(VALID_TYPES[0], VALID_AMOUNT, INVALID_DATE);
        String expectedMessage = LoanDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }

    @Test
    public void toModelType_nullDate_throwsIllegalValueException() {
        JsonAdaptedLoanTransaction transaction =
                new JsonAdaptedLoanTransaction(VALID_TYPES[0], VALID_AMOUNT, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "date");
        assertThrows(IllegalValueException.class, expectedMessage, transaction::toModelType);
    }
}
