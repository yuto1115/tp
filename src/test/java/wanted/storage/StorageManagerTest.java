package wanted.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import wanted.commons.core.GuiSettings;
import wanted.model.LoanBook;
import wanted.model.ReadOnlyLoanBook;
import wanted.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonLoanBookStorage addressBookStorage = new JsonLoanBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void loanBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonLoanBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonLoanBookStorageTest} class.
         */
        LoanBook original = getTypicalLoanBook();
        storageManager.saveLoanBook(original);
        ReadOnlyLoanBook retrieved = storageManager.readLoanBook().get();
        assertEquals(original, new LoanBook(retrieved));
    }

    @Test
    public void getLoanBookFilePath() {
        assertNotNull(storageManager.getLoanBookFilePath());
    }

}
