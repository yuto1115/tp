package wanted.model;

import static java.util.Objects.requireNonNull;
import static wanted.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import wanted.commons.core.GuiSettings;
import wanted.commons.core.LogsCenter;
import wanted.model.loan.Loan;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final LoanBook loanBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Loan> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyLoanBook loanBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(loanBook, userPrefs);

        logger.fine("Initializing with address book: " + loanBook + " and user prefs " + userPrefs);

        this.loanBook = new LoanBook(loanBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.loanBook.getPersonList());
    }

    public ModelManager() {
        this(new LoanBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getLoanBookFilePath() {
        return userPrefs.getLoanBookFilePath();
    }

    @Override
    public void setLoanBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setLoanBookFilePath(addressBookFilePath);
    }

    //=========== LoanBook ================================================================================

    @Override
    public void setLoanBook(ReadOnlyLoanBook loanBook) {
        this.loanBook.resetData(loanBook);
    }

    @Override
    public ReadOnlyLoanBook getLoanBook() {
        return loanBook;
    }

    @Override
    public boolean hasPerson(Loan person) {
        requireNonNull(person);
        return loanBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Loan target) {
        loanBook.removePerson(target);
    }

    @Override
    public void addPerson(Loan person) {
        loanBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Loan target, Loan editedPerson) {
        requireAllNonNull(target, editedPerson);

        loanBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Loan List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Loan} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Loan> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Loan> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return loanBook.equals(otherModelManager.loanBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
