package wanted.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalPersons.ALICE;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wanted.model.loan.Loan;
import wanted.testutil.PersonBuilder;

public class LoanBookTest {

    private final LoanBook loanBook = new LoanBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), loanBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> loanBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyLoanBook_replacesData() {
        LoanBook newData = getTypicalLoanBook();
        loanBook.resetData(newData);
        assertEquals(newData, loanBook);
    }
    /* TODO: Update
    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Loan editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Loan> newPersons = Arrays.asList(ALICE, editedAlice);
        LoanBookStub newData = new LoanBookStub(newPersons);

        assertThrows(DuplicateLoanException.class, () -> loanBook.resetData(newData));
    }
     */

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> loanBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInLoanBook_returnsFalse() {
        assertFalse(loanBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInLoanBook_returnsTrue() {
        loanBook.addPerson(ALICE);
        assertTrue(loanBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInLoanBook_returnsTrue() {
        loanBook.addPerson(ALICE);
        Loan editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(loanBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> loanBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = LoanBook.class.getCanonicalName() + "{persons=" + loanBook.getPersonList() + "}";
        assertEquals(expected, loanBook.toString());
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
