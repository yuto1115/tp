package wanted.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import wanted.commons.exceptions.DataLoadingException;
import wanted.model.LoanBook;
import wanted.model.ReadOnlyLoanBook;

/**
 * Represents a storage for {@link LoanBook}.
 */
public interface LoanBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getLoanBookFilePath();

    /**
     * Returns LoanBook data as a {@link ReadOnlyLoanBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyLoanBook> readLoanBook() throws DataLoadingException;

    /**
     * @see #getLoanBookFilePath()
     */
    Optional<ReadOnlyLoanBook> readLoanBook(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyLoanBook} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveLoanBook(ReadOnlyLoanBook addressBook) throws IOException;

    /**
     * @see #saveLoanBook(ReadOnlyLoanBook)
     */
    void saveLoanBook(ReadOnlyLoanBook addressBook, Path filePath) throws IOException;

}
