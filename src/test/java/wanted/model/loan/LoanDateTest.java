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
        assertFalse(LoanDate.isValidLoanDate("#27th of Feb!!")); // non-alphanumeric numbers
        assertFalse(LoanDate.isValidLoanDate("12 June")); //no year included

        //valid loan date
        assertTrue(LoanDate.isValidLoanDate("28th February 2025"));
        assertTrue(LoanDate.isValidLoanDate("30th January 2024"));
        assertTrue(LoanDate.isValidLoanDate("19th December 2023"));
    }

    @Test
    public void equals() {
        LoanDate date = new LoanDate("26th January 2025");

        // same values -> returns true
        assertTrue(date.equals(new LoanDate("26th January 2025")));

        // same object -> returns true
        assertTrue(date.equals(date));

        // null -> returns false
        assertFalse(date.equals(null));

        // different types -> returns false
        assertFalse(date.equals(45));

        // different values -> returns false
        assertFalse(date.equals(new LoanDate("20th January 2025")));
    }

}
