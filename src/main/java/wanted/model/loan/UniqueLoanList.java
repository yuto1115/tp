package wanted.model.loan;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wanted.model.loan.exceptions.DuplicateLoanException;
import wanted.model.loan.exceptions.LoanNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A loan is considered unique by comparing using {@code Loan#isSamePerson(Loan)}. As such, adding and updating of
 * persons uses Loan#isSamePerson(Loan) for equality so as to ensure that the loan being added or updated is
 * unique in terms of identity in the UniqueLoanList. However, the removal of a loan uses Loan#equals(Object) so
 * as to ensure that the loan with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Loan#isSamePerson(Loan)
 */
public class UniqueLoanList implements Iterable<Loan> {

    private final ObservableList<Loan> internalList = FXCollections.observableArrayList();
    private final ObservableList<Loan> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent loan as the given argument.
     */
    public boolean contains(Loan toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Adds a loan to the list.
     * The loan must not already exist in the list.
     */
    public void add(Loan toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateLoanException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the loan {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.
     * The loan identity of {@code editedPerson} must not be the same as another existing loan in the list.
     */
    public void setPerson(Loan target, Loan editedPerson) {
        requireAllNonNull(target, editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new LoanNotFoundException();
        }

        if (!target.isSamePerson(editedPerson) && contains(editedPerson)) {
            throw new DuplicateLoanException();
        }

        internalList.set(index, editedPerson);
    }

    /**
     * Removes the equivalent loan from the list.
     * The loan must exist in the list.
     */
    public void remove(Loan toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new LoanNotFoundException();
        }
    }

    public void setPersons(UniqueLoanList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Loan> persons) {
        requireAllNonNull(persons);
        if (!personsAreUnique(persons)) {
            throw new DuplicateLoanException();
        }

        internalList.setAll(persons);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Loan> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Loan> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueLoanList)) {
            return false;
        }

        UniqueLoanList otherUniqueLoanList = (UniqueLoanList) other;
        return internalList.equals(otherUniqueLoanList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code persons} contains only unique persons.
     */
    private boolean personsAreUnique(List<Loan> persons) {
        for (int i = 0; i < persons.size() - 1; i++) {
            for (int j = i + 1; j < persons.size(); j++) {
                if (persons.get(i).isSamePerson(persons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
