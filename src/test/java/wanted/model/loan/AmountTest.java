package wanted.model.loan;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AmountTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Amount(null));
    }

    @Test
    public void constructor_invalidAmount_throwsIllegalArgumentException() {
        String invalidAmount = "";
        assertThrows(IllegalArgumentException.class, () -> new Amount(invalidAmount));
    }

    @Test
    public void isValidAmount() {
        //null amount
        assertThrows(NullPointerException.class, () -> Amount.isValidAmount(null));

        //blank amount
        assertFalse(Amount.isValidAmount(""));

        //non-numeric amount
        assertFalse(Amount.isValidAmount("hello"));

        //negative amount
        assertFalse(Amount.isValidAmount("-10.10"));

        //too many decimal places
        assertFalse(Amount.isValidAmount("10.1010101010"));

        //missing parts
        assertFalse(Amount.isValidAmount("10"));
        assertFalse(Amount.isValidAmount("20.1"));

        //valid amount
        assertTrue(Amount.isValidAmount("10.10"));
        assertTrue(Amount.isValidAmount("200.10"));
        assertTrue(Amount.isValidAmount("47.00"));

    }

    @Test
    public void equals() {
        Amount amount = new Amount("10.10");

        // same values -> returns true
        assertTrue(amount.equals(new Amount("10.10")));

        // same object -> returns true
        assertTrue(amount.equals(amount));

        // null -> returns false
        assertFalse(amount.equals(null));

        // different types -> returns false
        assertFalse(amount.equals(10.10));

        // different values -> returns false
        assertFalse(amount.equals(new Amount("20.20")));
    }

}
