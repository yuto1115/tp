package wanted.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalPersons.ALICE;
import static wanted.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wanted.model.loan.Loan;
import wanted.testutil.PersonBuilder;

public class AddressBookTest {

    private final LoanBook addressBook = new LoanBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        LoanBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }
    /* TODO: Update
    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Loan editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Loan> newPersons = Arrays.asList(ALICE, editedAlice);
        LoanBookStub newData = new LoanBookStub(newPersons);

        assertThrows(DuplicateLoanException.class, () -> addressBook.resetData(newData));
    }
     */

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Loan editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = LoanBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyLoanBook whose persons list can violate interface constraints.
     */
    private static class LoanBookStub implements ReadOnlyLoanBook {
        private final ObservableList<Loan> persons = FXCollections.observableArrayList();

        LoanBookStub(Collection<Loan> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Loan> getPersonList() {
            return persons;
        }
    }

}
