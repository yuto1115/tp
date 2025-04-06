package wanted.testutil;

import java.util.ArrayList;
import java.util.Arrays;

import wanted.commons.core.datatypes.MoneyInt;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.loan.transaction.AddLoanTransaction;
import wanted.model.loan.transaction.RepayLoanTransaction;

/**
 * A utility class containing a list of {@code LoanAmount} objects to be used in tests.
 */
public class TypicalLoanAmount {
    public static final LoanAmount EMPTY_LOAN_AMOUNT = new LoanAmount();
    public static final LoanAmount NON_EMPTY_LOAN_AMOUNT_NOT_FULLY_REPAID;
    public static final LoanAmount NON_EMPTY_LOAN_AMOUNT_FULLY_REPAID;

    static {
        try {
            NON_EMPTY_LOAN_AMOUNT_NOT_FULLY_REPAID = new LoanAmount(new ArrayList<>(Arrays.asList(
                    new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("2024-01-01")),
                    new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2024-01-02")),
                    new RepayLoanTransaction(MoneyInt.fromCent(1500), new LoanDate("2024-01-03")),
                    new AddLoanTransaction(MoneyInt.fromCent(10305), new LoanDate("2024-02-01")),
                    new RepayLoanTransaction(MoneyInt.fromCent(8234), new LoanDate("2025-12-31"))
            )));
            NON_EMPTY_LOAN_AMOUNT_FULLY_REPAID = new LoanAmount(new ArrayList<>(Arrays.asList(
                    new AddLoanTransaction(MoneyInt.fromCent(1000), new LoanDate("2024-01-01")),
                    new AddLoanTransaction(MoneyInt.fromCent(500), new LoanDate("2024-01-02")),
                    new RepayLoanTransaction(MoneyInt.fromCent(350), new LoanDate("2024-01-03")),
                    new AddLoanTransaction(MoneyInt.fromCent(5000), new LoanDate("2024-02-01")),
                    new RepayLoanTransaction(MoneyInt.fromCent(6150), new LoanDate("2025-12-31"))
            )));
        } catch (ExcessRepaymentException e) {
            throw new RuntimeException(e);
        }
    }
}
