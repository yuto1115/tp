package wanted.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import wanted.commons.core.LogsCenter;
import wanted.commons.exceptions.DataLoadingException;
import wanted.commons.exceptions.IllegalValueException;
import wanted.commons.util.FileUtil;
import wanted.commons.util.JsonUtil;
import wanted.model.ReadOnlyLoanBook;

/**
 * A class to access LoanBook data stored as a json file on the hard disk.
 */
public class JsonLoanBookStorage implements LoanBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonLoanBookStorage.class);

    private Path filePath;

    public JsonLoanBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getLoanBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyLoanBook> readLoanBook() throws DataLoadingException {
        return readLoanBook(filePath);
    }

    /**
     * Similar to {@link #readLoanBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyLoanBook> readLoanBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableLoanBook> jsonAddressBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableLoanBook.class);
        if (!jsonAddressBook.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonAddressBook.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveLoanBook(ReadOnlyLoanBook loanBook) throws IOException {
        saveLoanBook(loanBook, filePath);
    }

    /**
     * Similar to {@link #saveLoanBook(ReadOnlyLoanBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveLoanBook(ReadOnlyLoanBook loanBook, Path filePath) throws IOException {
        requireNonNull(loanBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableLoanBook(loanBook), filePath);
    }

}
