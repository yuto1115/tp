package wanted.testutil;

import wanted.model.LoanBook;
import wanted.model.loan.Loan;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code LoanBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private LoanBook addressBook;

    public AddressBookBuilder() {
        addressBook = new LoanBook();
    }

    public AddressBookBuilder(LoanBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Loan} to the {@code LoanBook} that we are building.
     */
    public AddressBookBuilder withPerson(Loan person) {
        addressBook.addPerson(person);
        return this;
    }

    public LoanBook build() {
        return addressBook;
    }
}
