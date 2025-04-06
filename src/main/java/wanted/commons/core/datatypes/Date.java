package wanted.commons.core.datatypes;

/**
 * Represents a date.
 * <p>
 * In the current implementation, a date is simply stored as a String,
 * but it should be modified in the future so that it can:
 * - reject invalid dates
 * - convert to String in a specific format
 * - calculate the difference of two dates
 * etc.
 */
public class Date {
    private final String date;

    /**
     * Constructs a {@code Date} with the given date.
     *
     * @param date The string representation of the date.
     */
    public Date(String date) {
        this.date = date;
    }

    /**
     * Returns the stored date.
     */
    public String getDate() {
        return this.date;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Date)) {
            return false;
        }

        Date otherDate = (Date) other;
        return this.date.equals(otherDate.date);
    }

    /*
    @Override
    public String toString() {
        return new ToStringBuilder(this).add("date", date).toString();
    }

     */
    @Override
    public String toString() {
        return getDate();
    }
}
