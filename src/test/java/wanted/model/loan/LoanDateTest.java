package wanted.model.loan;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LoanDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LoanDate(null));
    }

    @Test
    public void constructor_invalidLoanDate_throwsIllegalArgumentException() {
        String invalidLoanDate = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidLoanDate));
    }

    @Test
    public void isValidLoanDate() {
        //null loan date
        assertThrows(NullPointerException.class, () -> LoanDate.isValidLoanDate(null)); //null value
        //invalid loan date
        assertFalse(LoanDate.isValidLoanDate("")); //blank value
        assertFalse(LoanDate.isValidLoanDate("#2024-02-27!!")); // non-alphanumeric numbers
        assertFalse(LoanDate.isValidLoanDate("06-12")); //no year included

        //valid loan date
        assertTrue(LoanDate.isValidLoanDate("2025-02-28"));
        assertTrue(LoanDate.isValidLoanDate("2024-01-30"));
        assertTrue(LoanDate.isValidLoanDate("2023-12-19"));
    }

    @Test
    public void equals() {
        LoanDate date = new LoanDate("2025-01-26");

        // same values -> returns true
        assertTrue(date.equals(new LoanDate("2025-01-26")));

        // same object -> returns true
        assertTrue(date.equals(date));

        // null -> returns false
        assertFalse(date.equals(null));

        // different types -> returns false
        assertFalse(date.equals(45));

        // different values -> returns false
        assertFalse(date.equals(new LoanDate("2025-01-27")));
    }

}
