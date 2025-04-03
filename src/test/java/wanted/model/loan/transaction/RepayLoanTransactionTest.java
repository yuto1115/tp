package wanted.model.loan.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;

public class RepayLoanTransactionTest {
    private static final MoneyInt VALID_AMOUNT = MoneyInt.fromDollarAndCent(10, 50);
    private static final MoneyInt VALID_AMOUNT_2 = MoneyInt.fromDollarAndCent(0, 0);
    private static final LoanDate VALID_DATE = new LoanDate("1 Jan 2025");
    private static final LoanDate VALID_DATE_2 = new LoanDate("2 Jan 2025");

    @Test
    public void constructor_validInput_success() {
        final RepayLoanTransaction transaction = new RepayLoanTransaction(VALID_AMOUNT, VALID_DATE);
        assertEquals(VALID_AMOUNT, transaction.getAmount());
        assertEquals(VALID_DATE, transaction.getDate());
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RepayLoanTransaction(null, VALID_DATE));
        assertThrows(NullPointerException.class, () -> new RepayLoanTransaction(VALID_AMOUNT, null));
        assertThrows(NullPointerException.class, () -> new RepayLoanTransaction(null, null));
    }

    @Test
    public void getExplanation() {
        final RepayLoanTransaction transaction = new RepayLoanTransaction(VALID_AMOUNT, VALID_DATE);
        final String expected = "$10.50 repaid on 1 Jan 2025";
        assertEquals(expected, transaction.getExplanation());
    }

    @Test
    public void getTotalAmountAfterTransaction() {
        final MoneyInt previousAmount = MoneyInt.fromDollarAndCent(10, 50);
        final MoneyInt transactionAmount = MoneyInt.fromDollarAndCent(30, 72);
        final MoneyInt expectedAmount = previousAmount;

        final RepayLoanTransaction transaction = new RepayLoanTransaction(transactionAmount, VALID_DATE);
        assertEquals(expectedAmount, transaction.getTotalAmountAfterTransaction(previousAmount));
    }

    @Test
    public void getRemainingAmountAfterTransaction_success() throws Exception {
        {
            final MoneyInt previousAmount = MoneyInt.fromDollarAndCent(41, 22);
            final MoneyInt transactionAmount = MoneyInt.fromDollarAndCent(30, 72);
            final MoneyInt expectedAmount = MoneyInt.fromDollarAndCent(10, 50);

            final RepayLoanTransaction transaction = new RepayLoanTransaction(transactionAmount, VALID_DATE);
            assertEquals(expectedAmount, transaction.getRemainingAmountAfterTransaction(previousAmount));
        }
        {
            final MoneyInt previousAmount = MoneyInt.fromDollarAndCent(30, 72);
            final MoneyInt transactionAmount = MoneyInt.fromDollarAndCent(30, 72);
            final MoneyInt expectedAmount = MoneyInt.fromDollarAndCent(0, 0);

            final RepayLoanTransaction transaction = new RepayLoanTransaction(transactionAmount, VALID_DATE);
            assertEquals(expectedAmount, transaction.getRemainingAmountAfterTransaction(previousAmount));
        }
    }

    @Test
    public void getRemainingAmountAfterTransaction_throwsExcessRepaymentException() {
        final MoneyInt previousAmount = MoneyInt.fromDollarAndCent(30, 71);
        final MoneyInt transactionAmount = MoneyInt.fromDollarAndCent(30, 72);

        final RepayLoanTransaction transaction = new RepayLoanTransaction(transactionAmount, VALID_DATE);
        assertThrows(ExcessRepaymentException.class, () ->
                transaction.getRemainingAmountAfterTransaction(previousAmount));
    }

    @Test
    public void getNewTransactionOfSameTypeTest() {
        final RepayLoanTransaction transaction = new RepayLoanTransaction(VALID_AMOUNT, VALID_DATE);
        final RepayLoanTransaction expected = new RepayLoanTransaction(VALID_AMOUNT_2, VALID_DATE_2);
        final LoanTransaction actual = transaction.getNewTransactionOfSameType(VALID_AMOUNT_2, VALID_DATE_2);

        assertEquals(expected, actual);
        // original object must not change
        assertEquals(new RepayLoanTransaction(VALID_AMOUNT, VALID_DATE), transaction);
    }

    @Test
    public void equals() {
        final RepayLoanTransaction transaction = new RepayLoanTransaction(VALID_AMOUNT, VALID_DATE);

        // same values -> returns true
        assertTrue(transaction.equals(new RepayLoanTransaction(VALID_AMOUNT, VALID_DATE)));

        // same object -> returns true
        assertTrue(transaction.equals(transaction));

        // null -> returns false
        assertFalse(transaction.equals(null));

        // different types -> returns false
        assertFalse(transaction.equals(new AddLoanTransaction(VALID_AMOUNT, VALID_DATE)));

        // different RepayLoanTransaction -> returns false
        assertFalse(transaction.equals(new RepayLoanTransaction(VALID_AMOUNT_2, VALID_DATE)));
        assertFalse(transaction.equals(new RepayLoanTransaction(VALID_AMOUNT, VALID_DATE_2)));
    }
}
