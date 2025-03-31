package wanted.model.loan.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.loan.LoanDate;

public class AddLoanTransactionTest {
    private static final MoneyInt VALID_AMOUNT = MoneyInt.fromDollarAndCent(10, 50);
    private static final MoneyInt VALID_AMOUNT_2 = MoneyInt.fromDollarAndCent(0, 0);
    private static final LoanDate VALID_DATE = new LoanDate("1 Jan 2025");
    private static final LoanDate VALID_DATE_2 = new LoanDate("2 Jan 2025");

    @Test
    public void constructor_validInput_success() {
        final AddLoanTransaction transaction = new AddLoanTransaction(VALID_AMOUNT, VALID_DATE);
        assertEquals(VALID_AMOUNT, transaction.getAmount());
        assertEquals(VALID_DATE, transaction.getDate());
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddLoanTransaction(null, VALID_DATE));
        assertThrows(NullPointerException.class, () -> new AddLoanTransaction(VALID_AMOUNT, null));
        assertThrows(NullPointerException.class, () -> new AddLoanTransaction(null, null));
    }

    @Test
    public void getExplanation() {
        final AddLoanTransaction transaction = new AddLoanTransaction(VALID_AMOUNT, VALID_DATE);
        final String expected = "$10.50 loaned on 1 Jan 2025";
        assertEquals(expected, transaction.getExplanation());
    }

    @Test
    public void getTotalAmountAfterTransaction() {
        final MoneyInt previousAmount = MoneyInt.fromDollarAndCent(10, 50);
        final MoneyInt transactionAmount = MoneyInt.fromDollarAndCent(30, 72);
        final MoneyInt expectedAmount = MoneyInt.fromDollarAndCent(41, 22);

        final AddLoanTransaction transaction = new AddLoanTransaction(transactionAmount, VALID_DATE);
        assertEquals(expectedAmount, transaction.getTotalAmountAfterTransaction(previousAmount));
    }

    @Test
    public void getRemainingAmountAfterTransaction() {
        final MoneyInt previousAmount = MoneyInt.fromDollarAndCent(10, 50);
        final MoneyInt transactionAmount = MoneyInt.fromDollarAndCent(30, 72);
        final MoneyInt expectedAmount = MoneyInt.fromDollarAndCent(41, 22);

        final AddLoanTransaction transaction = new AddLoanTransaction(transactionAmount, VALID_DATE);
        assertEquals(expectedAmount, transaction.getRemainingAmountAfterTransaction(previousAmount));
    }

    @Test
    public void equals() {
        final AddLoanTransaction transaction = new AddLoanTransaction(VALID_AMOUNT, VALID_DATE);

        // same values -> returns true
        assertTrue(transaction.equals(new AddLoanTransaction(VALID_AMOUNT, VALID_DATE)));

        // same object -> returns true
        assertTrue(transaction.equals(transaction));

        // null -> returns false
        assertFalse(transaction.equals(null));

        // different types -> returns false
        assertFalse(transaction.equals(new RepayLoanTransaction(VALID_AMOUNT, VALID_DATE)));

        // different AddLoanTransaction -> returns false
        assertFalse(transaction.equals(new AddLoanTransaction(VALID_AMOUNT_2, VALID_DATE)));
        assertFalse(transaction.equals(new AddLoanTransaction(VALID_AMOUNT, VALID_DATE_2)));
    }
}
