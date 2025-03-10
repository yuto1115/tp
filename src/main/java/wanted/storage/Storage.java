package wanted.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import wanted.commons.exceptions.DataLoadingException;
import wanted.model.ReadOnlyLoanBook;
import wanted.model.ReadOnlyUserPrefs;
import wanted.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends LoanBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyLoanBook> readAddressBook() throws DataLoadingException;

    @Override
    void saveAddressBook(ReadOnlyLoanBook addressBook) throws IOException;

}
