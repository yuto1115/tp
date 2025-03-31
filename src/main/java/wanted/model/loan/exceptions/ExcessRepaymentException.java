package wanted.model.loan.exceptions;

/**
 * Exception thrown when an operation causes the loan balance to get negative.
 */
public class ExcessRepaymentException extends Exception {
    public ExcessRepaymentException() {
        super("Operation would result in a negative loan balance");
    }
}
