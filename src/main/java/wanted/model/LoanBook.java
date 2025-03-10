package wanted.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import wanted.commons.util.ToStringBuilder;
import wanted.model.loan.Loan;
import wanted.model.loan.UniqueLoanList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class LoanBook implements ReadOnlyLoanBook {

    private final UniqueLoanList persons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniqueLoanList();
    }

    public LoanBook() {}

    /**
     * Creates an LoanBook using the Persons in the {@code toBeCopied}
     */
    public LoanBook(ReadOnlyLoanBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the loan list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Loan> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code LoanBook} with {@code newData}.
     */
    public void resetData(ReadOnlyLoanBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// loan-level operations

    /**
     * Returns true if a loan with the same identity as {@code loan} exists in the address book.
     */
    public boolean hasPerson(Loan person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a loan to the address book.
     * The loan must not already exist in the address book.
     */
    public void addPerson(Loan p) {
        persons.add(p);
    }

    /**
     * Replaces the given loan {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The loan identity of {@code editedPerson} must not be the same as another existing loan in the address book.
     */
    public void setPerson(Loan target, Loan editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code LoanBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Loan key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Loan> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LoanBook)) {
            return false;
        }

        LoanBook otherAddressBook = (LoanBook) other;
        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
