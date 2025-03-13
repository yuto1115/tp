package wanted.commons.core.datatypes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DateTest {

    @Test
    public void constructor() {
        assertEquals("2025-01-01", new Date("2025-01-01").getDate());
    }

    @Test
    public void equals() {
        final Date date = new Date("1 Jan 2025");

        // same values -> returns true
        assertTrue(date.equals(new Date("1 Jan 2025")));

        // same object -> returns true
        assertTrue(date.equals(date));

        // null -> returns false
        assertFalse(date.equals(null));

        // different types -> returns false
        assertFalse(date.equals("1 Jan 2025"));

        // different dates -> returns false
        assertFalse(date.equals(new Date("1 Feb 2025")));

        // same date but different formats -> returns false
        assertFalse(date.equals(new Date("01-01-2025")));
    }

    @Test
    public void toStringMethod() {
        final Date date = new Date("1 Jan 2025");
        String expected = Date.class.getCanonicalName() + "{date=" + date.getDate() + "}";
        assertEquals(expected, date.toString());
    }
}
