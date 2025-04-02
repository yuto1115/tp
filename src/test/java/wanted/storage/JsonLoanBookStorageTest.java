package wanted.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalPersons.ALICE;
import static wanted.testutil.TypicalPersons.HOON;
import static wanted.testutil.TypicalPersons.IDA;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import wanted.commons.exceptions.DataLoadingException;
import wanted.model.LoanBook;
import wanted.model.ReadOnlyLoanBook;

public class JsonLoanBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonLoanBookStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readLoanBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readLoanBook(null));
    }

    private java.util.Optional<ReadOnlyLoanBook> readLoanBook(String filePath) throws Exception {
        return new JsonLoanBookStorage(Paths.get(filePath)).readLoanBook(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readLoanBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readLoanBook("notJsonFormatLoanBook.json"));
    }

    @Test
    public void readLoanBook_invalidPersonLoanBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readLoanBook("invalidPersonLoanBook.json"));
    }

    @Test
    public void readLoanBook_invalidAndValidPersonLoanBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readLoanBook("invalidAndValidPersonLoanBook.json"));
    }

    @Test
    public void readAndSaveLoanBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("Temploanbook.json");
        LoanBook original = getTypicalLoanBook();
        JsonLoanBookStorage jsonLoanBookStorage = new JsonLoanBookStorage(filePath);

        // Save in new file and read back
        jsonLoanBookStorage.saveLoanBook(original, filePath);
        ReadOnlyLoanBook readBack = jsonLoanBookStorage.readLoanBook(filePath).get();
        assertEquals(original, new LoanBook(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonLoanBookStorage.saveLoanBook(original, filePath);
        readBack = jsonLoanBookStorage.readLoanBook(filePath).get();
        assertEquals(original, new LoanBook(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonLoanBookStorage.saveLoanBook(original); // file path not specified
        readBack = jsonLoanBookStorage.readLoanBook().get(); // file path not specified
        assertEquals(original, new LoanBook(readBack));

    }

    @Test
    public void saveLoanBook_nullLoanBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveLoanBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code loanBook} at the specified {@code filePath}.
     */
    private void saveLoanBook(ReadOnlyLoanBook loanBook, String filePath) {
        try {
            new JsonLoanBookStorage(Paths.get(filePath))
                    .saveLoanBook(loanBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveLoanBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveLoanBook(new LoanBook(), null));
    }
}
