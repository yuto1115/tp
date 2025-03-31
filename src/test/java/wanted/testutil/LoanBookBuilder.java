package wanted.testutil;

import wanted.model.LoanBook;
import wanted.model.loan.Loan;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code LoanBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class LoanBookBuilder {

    private LoanBook loanBook;

    public LoanBookBuilder() {
        loanBook = new LoanBook();
    }

    public LoanBookBuilder(LoanBook loanBook) {
        this.loanBook = loanBook;
    } //?

    /**
     * Adds a new {@code Loan} to the {@code LoanBook} that we are building.
     */
    public LoanBookBuilder withPerson(Loan person) {
        loanBook.addPerson(person);
        return this;
    }

    public LoanBook build() {
        return loanBook;
    }
}
