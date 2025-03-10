package wanted.model;

import javafx.collections.ObservableList;
import wanted.model.loan.Loan;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyLoanBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Loan> getPersonList();

}
