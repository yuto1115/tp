package wanted.commons.core.datatypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class MoneyIntTest {
    @Test
    public void fromDollarAndCent_outOfRangeInput_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> MoneyInt.fromDollarAndCent(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> MoneyInt.fromDollarAndCent(10, -1));
        assertThrows(IllegalArgumentException.class, () -> MoneyInt.fromDollarAndCent(10, 100));
    }

    @Test
    public void fromDollarAndCent_validInput_success() {
        assertEquals(0, MoneyInt.fromDollarAndCent(0, 0).getValueTimesOneHundred());
        assertEquals(99, MoneyInt.fromDollarAndCent(0, 99).getValueTimesOneHundred());
        assertEquals(1000, MoneyInt.fromDollarAndCent(10, 0).getValueTimesOneHundred());
        assertEquals(1050, MoneyInt.fromDollarAndCent(10, 50).getValueTimesOneHundred());
        assertEquals(123456, MoneyInt.fromDollarAndCent(1234, 56).getValueTimesOneHundred());
    }

    @Test
    public void getStringRepresentationWithFixedDecimalPoint() {
        assertEquals("0.00",
                MoneyInt.fromDollarAndCent(0, 0).getStringRepresentationWithFixedDecimalPoint());
        assertEquals("0.99",
                MoneyInt.fromDollarAndCent(0, 99).getStringRepresentationWithFixedDecimalPoint());
        assertEquals("10.00",
                MoneyInt.fromDollarAndCent(10, 0).getStringRepresentationWithFixedDecimalPoint());
        assertEquals("10.50",
                MoneyInt.fromDollarAndCent(10, 50).getStringRepresentationWithFixedDecimalPoint());
        assertEquals("1234.56",
                MoneyInt.fromDollarAndCent(1234, 56).getStringRepresentationWithFixedDecimalPoint());
    }

    @Test
    public void equals() {
        final MoneyInt moneyInt = MoneyInt.fromDollarAndCent(12, 34);

        // same values -> returns true
        assertTrue(moneyInt.equals(MoneyInt.fromDollarAndCent(12, 34)));

        // same object -> returns true
        assertTrue(moneyInt.equals(moneyInt));

        // null -> returns false
        assertFalse(moneyInt.equals(null));

        // different types -> returns false
        assertFalse(moneyInt.equals(1234));
        assertFalse(moneyInt.equals(12.34f));

        // different MoneyInt -> returns false
        assertFalse(moneyInt.equals(MoneyInt.fromDollarAndCent(12, 43)));
    }

    @Test
    public void toStringMethod() {
        MoneyInt moneyInt = MoneyInt.fromDollarAndCent(12, 34);
        String expected = MoneyInt.class.getCanonicalName()
                + "{valueTimesOneHundred=" + moneyInt.getValueTimesOneHundred() + "}";
        assertEquals(expected, moneyInt.toString());
    }

    @Test
    public void compareToMethod() {
        MoneyInt moneyInt = MoneyInt.fromDollarAndCent(12, 34);
        MoneyInt less = MoneyInt.fromDollarAndCent(12, 0);
        MoneyInt greater = MoneyInt.fromDollarAndCent(12, 99);
        MoneyInt equal = MoneyInt.fromDollarAndCent(12, 34);
        assertTrue(moneyInt.compareTo(less) > 0);
        assertTrue(moneyInt.compareTo(greater) < 0);
        assertEquals(0, moneyInt.compareTo(equal));
    }
}
