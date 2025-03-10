package wanted.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import wanted.commons.core.LogsCenter;
import wanted.commons.exceptions.DataLoadingException;
import wanted.model.ReadOnlyLoanBook;
import wanted.model.ReadOnlyUserPrefs;
import wanted.model.UserPrefs;

/**
 * Manages storage of LoanBook data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private LoanBookStorage loanBookStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code LoanBookStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(LoanBookStorage loanBookStorage, UserPrefsStorage userPrefsStorage) {
        this.loanBookStorage = loanBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ LoanBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        return loanBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyLoanBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(loanBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyLoanBook> readAddressBook(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return loanBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyLoanBook addressBook) throws IOException {
        saveAddressBook(addressBook, loanBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyLoanBook addressBook, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        loanBookStorage.saveAddressBook(addressBook, filePath);
    }

}
