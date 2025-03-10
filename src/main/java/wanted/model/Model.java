package wanted.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import wanted.commons.core.GuiSettings;
import wanted.model.loan.Loan;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Loan> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyLoanBook addressBook);

    /** Returns the LoanBook */
    ReadOnlyLoanBook getAddressBook();

    /**
     * Returns true if a loan with the same identity as {@code loan} exists in the address book.
     */
    boolean hasPerson(Loan person);

    /**
     * Deletes the given loan.
     * The loan must exist in the address book.
     */
    void deletePerson(Loan target);

    /**
     * Adds the given loan.
     * {@code loan} must not already exist in the address book.
     */
    void addPerson(Loan person);

    /**
     * Replaces the given loan {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The loan identity of {@code editedPerson} must not be the same as another existing loan in the address book.
     */
    void setPerson(Loan target, Loan editedPerson);

    /** Returns an unmodifiable view of the filtered loan list */
    ObservableList<Loan> getFilteredPersonList();

    /**
     * Updates the filter of the filtered loan list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Loan> predicate);
}
