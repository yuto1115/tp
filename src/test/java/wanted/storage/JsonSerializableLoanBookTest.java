package wanted.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static wanted.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import wanted.commons.exceptions.IllegalValueException;
import wanted.commons.util.JsonUtil;
import wanted.model.LoanBook;
import wanted.testutil.TypicalPersons;

public class JsonSerializableLoanBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableLoanBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsLoanBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonLoanBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonLoanBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableLoanBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableLoanBook.class).get();
        LoanBook loanBookFromFile = dataFromFile.toModelType();
        LoanBook typicalPersonsAddressBook = TypicalPersons.getTypicalLoanBook();
        assertEquals(loanBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableLoanBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableLoanBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }
    //TODO: toModelType_duplicatePersons_throwsIllegalValueException() throws Exception
}
