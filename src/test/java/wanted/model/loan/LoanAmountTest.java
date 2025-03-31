package wanted.model.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.LoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;
import wanted.testutil.TypicalLoanAmount;

public class LoanAmountTest {
    @Test
    public void constructor_success() throws Exception {
        {
            LoanAmount loanAmount = new LoanAmount();

            assertEquals(MoneyInt.fromCent(0), loanAmount.getTotalAmount());
            assertEquals(MoneyInt.fromCent(0), loanAmount.getRemainingAmount());
            assertEquals(0, loanAmount.getTransactionHistoryCopy().size());
        }
        {
            LoanAmount loanAmount = new LoanAmount(new ArrayList<>(Arrays.asList(
                    new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                    new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2nd Jan 2024")),
                    new RepayLoanTransaction(MoneyInt.fromCent(1250), new LoanDate("3rd Jan 2024"))
            )));

            assertEquals(MoneyInt.fromCent(1500), loanAmount.getTotalAmount());
            assertEquals(MoneyInt.fromCent(250), loanAmount.getRemainingAmount());
            assertEquals(3, loanAmount.getTransactionHistoryCopy().size());
        }
        // After passing a list to the constructor, modifications on the list on the caller side should not
        // affect the created LoanAmount object.
        {
            ArrayList<LoanTransaction> transactions = new ArrayList<>(Arrays.asList(
                    new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                    new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2nd Jan 2024")),
                    new RepayLoanTransaction(MoneyInt.fromCent(1250), new LoanDate("3rd Jan 2024"))
            ));
            LoanAmount loanAmount = new LoanAmount(transactions);
            transactions.clear();

            assertEquals(3, loanAmount.getTransactionHistoryCopy().size());
        }
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LoanAmount(null));
        assertThrows(NullPointerException.class, () ->
                new LoanAmount(new ArrayList<>(Arrays.asList(
                        new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                        null
                ))));
    }

    @Test
    public void constructor_invalidTransactions_throwsExcessRepaymentException() {
        // Balance gets negative at the end
        assertThrows(ExcessRepaymentException.class, () -> new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2nd Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(2000), new LoanDate("3rd Jan 2024"))
        ))));

        // Balance gets negative in the middle, even though it gets positive at the end
        assertThrows(ExcessRepaymentException.class, () -> new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(1001), new LoanDate("2nd Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("3rd Jan 2024"))
        ))));
    }

    @Test
    public void getTransactionListCopy() {
        LoanAmount loanAmount = TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_NOT_FULLY_REPAID;

        ArrayList<LoanTransaction> transactions = loanAmount.getTransactionHistoryCopy();

        assertEquals(5, transactions.size());

        assertEquals(new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                transactions.get(0));
        assertEquals(new RepayLoanTransaction(MoneyInt.fromCent(1500), new LoanDate("3rd Jan 2024")),
                transactions.get(2));

        // modifying the returned list should not affect the original LoanAmount
        transactions.set(4, new AddLoanTransaction(MoneyInt.fromCent(0), new LoanDate("1st Jan 2024")));

        assertEquals(new RepayLoanTransaction(MoneyInt.fromCent(8234), new LoanDate("31th Dec 2025")),
                loanAmount.getTransactionHistoryCopy().get(4));
    }

    @Test
    public void appendTransaction_success() throws Exception {
        LoanTransaction transaction1 = new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024"));
        LoanTransaction transaction2 = new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2nd Jan 2024"));
        LoanTransaction transaction3 = new RepayLoanTransaction(MoneyInt.fromCent(1200), new LoanDate("3rd Jan 2024"));
        LoanAmount originalLoanAmount = new LoanAmount(new ArrayList<>(List.of(transaction1)));
        LoanAmount newLoanAmount = originalLoanAmount
                .appendTransaction(transaction2)
                .appendTransaction(transaction3);

        // new transactions should be appended at the back of the history
        assertEquals(new ArrayList<>(List.of(transaction1, transaction2, transaction3)),
                newLoanAmount.getTransactionHistoryCopy());

        // original LoanAmount should remain unchanged
        assertEquals(new ArrayList<>(List.of(transaction1)), originalLoanAmount.getTransactionHistoryCopy());
    }

    @Test
    public void appendTransactions_excessiveRepayment_throwsExcessRepaymentException() {
        LoanAmount loanAmount = TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_NOT_FULLY_REPAID;
        RepayLoanTransaction transaction = new RepayLoanTransaction(
                MoneyInt.fromCent(loanAmount.getRemainingAmount().getValueTimesOneHundred() + 1),
                new LoanDate("1st Jan 2024"));

        assertThrows(ExcessRepaymentException.class, () -> loanAmount.appendTransaction(transaction));
    }

    @Test
    public void isRepaid() {
        assertTrue(TypicalLoanAmount.EMPTY_LOAN_AMOUNT.isRepaid());
        assertFalse(TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_NOT_FULLY_REPAID.isRepaid());
        assertTrue(TypicalLoanAmount.NON_EMPTY_LOAN_AMOUNT_FULLY_REPAID.isRepaid());
    }

    @Test
    public void equalsMethod() throws Exception {
        LoanAmount loanAmount = new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1500), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("1st Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024"))
        )));

        // same values -> returns true
        assertTrue(loanAmount.equals(new LoanAmount()
                .appendTransaction(new AddLoanTransaction(MoneyInt.fromCent(1500), new LoanDate("1st Jan 2024")))
                .appendTransaction(new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("1st Jan 2024")))
                .appendTransaction(new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")))
        ));

        // same object -> returns true
        assertTrue(loanAmount.equals(loanAmount));

        // null -> returns false
        assertFalse(loanAmount.equals(null));

        // different types -> returns false
        assertFalse(loanAmount.equals(10.10));

        // different values -> returns false
        assertFalse(loanAmount.equals(new LoanAmount(new ArrayList<>(List.of(
                new AddLoanTransaction(MoneyInt.fromCent(1500), new LoanDate("1st Jan 2024")))))));
        assertFalse(loanAmount.equals(new LoanAmount(new ArrayList<>(Arrays.asList(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")),
                new RepayLoanTransaction(MoneyInt.fromCent(500), new LoanDate("1st Jan 2024")),
                new AddLoanTransaction(MoneyInt.fromCent(1500), new LoanDate("1st Jan 2024"))
        )))));
    }

    @Test
    public void compareToMethod() throws Exception {
        LoanAmount loanAmount = new LoanAmount(new ArrayList<>(List.of(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("1st Jan 2024")))));
        LoanAmount less = new LoanAmount(new ArrayList<>(List.of(
                new AddLoanTransaction(MoneyInt.fromCent(900), new LoanDate("1st Jan 2024")))));
        LoanAmount greater = new LoanAmount(new ArrayList<>(List.of(
                new AddLoanTransaction(MoneyInt.fromCent(1001), new LoanDate("1st Jan 2024")))));
        LoanAmount equal = new LoanAmount(new ArrayList<>(List.of(
                new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("2nd Feb 2222")))));
        assertTrue(loanAmount.compareTo(less) > 0);
        assertTrue(loanAmount.compareTo(greater) < 0);
        assertEquals(0, loanAmount.compareTo(equal));
    }

    /*
    @Test
    public void isValidAmount() {
        //null amount
        assertThrows(NullPointerException.class, () -> LoanAmount.isValidAmount(null));

        //blank amount
        assertFalse(LoanAmount.isValidAmount(""));

        //non-numeric amount
        assertFalse(LoanAmount.isValidAmount("hello"));

        //negative amount
        assertFalse(LoanAmount.isValidAmount("-10.10"));

        //too many decimal places
        assertFalse(LoanAmount.isValidAmount("10.1010101010"));

        //missing parts
        assertFalse(LoanAmount.isValidAmount("10"));
        assertFalse(LoanAmount.isValidAmount("20.1"));

        //valid amount
        assertTrue(LoanAmount.isValidAmount("10.10"));
        assertTrue(LoanAmount.isValidAmount("200.10"));
        assertTrue(LoanAmount.isValidAmount("47.00"));

    }
     */
}
