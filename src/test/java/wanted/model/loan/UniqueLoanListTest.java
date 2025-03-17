package wanted.model.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static wanted.testutil.Assert.assertThrows;
import static wanted.testutil.TypicalPersons.ALICE;
import static wanted.testutil.TypicalPersons.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import wanted.model.loan.exceptions.DuplicateLoanException;
import wanted.model.loan.exceptions.LoanNotFoundException;
import wanted.testutil.PersonBuilder;

public class UniqueLoanListTest {

    private final UniqueLoanList uniqueLoanList = new UniqueLoanList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLoanList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniqueLoanList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniqueLoanList.add(ALICE);
        assertTrue(uniqueLoanList.contains(ALICE));
    }

    //contains_personWithSameIdentityFieldsInList_returnsTrue
    //TODO: add new test here

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLoanList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniqueLoanList.add(ALICE);
        assertThrows(DuplicateLoanException.class, () -> uniqueLoanList.add(ALICE));
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLoanList.setPerson(null, ALICE));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLoanList.setPerson(ALICE, null));
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(LoanNotFoundException.class, () -> uniqueLoanList.setPerson(ALICE, ALICE));
    }

    @Test
    public void setPerson_editedPersonIsSameLoan_success() {
        uniqueLoanList.add(ALICE);
        uniqueLoanList.setPerson(ALICE, ALICE);
        UniqueLoanList expectedUniqueLoanList = new UniqueLoanList();
        expectedUniqueLoanList.add(ALICE);
        assertEquals(expectedUniqueLoanList, uniqueLoanList);
    }

    @Test
    public void setPerson_editedPersonHasSameIdentity_success() {
        uniqueLoanList.add(ALICE);
        Loan editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueLoanList.setPerson(ALICE, editedAlice);
        UniqueLoanList expectedUniqueLoanList = new UniqueLoanList();
        expectedUniqueLoanList.add(editedAlice);
        assertEquals(expectedUniqueLoanList, uniqueLoanList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        uniqueLoanList.add(ALICE);
        uniqueLoanList.setPerson(ALICE, BOB);
        UniqueLoanList expectedUniqueLoanList = new UniqueLoanList();
        expectedUniqueLoanList.add(BOB);
        assertEquals(expectedUniqueLoanList, uniqueLoanList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniqueLoanList.add(ALICE);
        uniqueLoanList.add(BOB);
        assertThrows(DuplicateLoanException.class, () -> uniqueLoanList.setPerson(ALICE, BOB));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLoanList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(LoanNotFoundException.class, () -> uniqueLoanList.remove(ALICE));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniqueLoanList.add(ALICE);
        uniqueLoanList.remove(ALICE);
        UniqueLoanList expectedUniqueLoanList = new UniqueLoanList();
        assertEquals(expectedUniqueLoanList, uniqueLoanList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLoanList.setPersons((UniqueLoanList) null));
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniqueLoanList.add(ALICE);
        UniqueLoanList expectedUniqueLoanList = new UniqueLoanList();
        expectedUniqueLoanList.add(BOB);
        uniqueLoanList.setPersons(expectedUniqueLoanList);
        assertEquals(expectedUniqueLoanList, uniqueLoanList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueLoanList.setPersons((List<Loan>) null));
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniqueLoanList.add(ALICE);
        List<Loan> personList = Collections.singletonList(BOB);
        uniqueLoanList.setPersons(personList);
        UniqueLoanList expectedUniqueLoanList = new UniqueLoanList();
        expectedUniqueLoanList.add(BOB);
        assertEquals(expectedUniqueLoanList, uniqueLoanList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Loan> listWithDuplicatePersons = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicateLoanException.class, () -> uniqueLoanList.setPersons(listWithDuplicatePersons));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueLoanList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueLoanList.asUnmodifiableObservableList().toString(), uniqueLoanList.toString());
    }
}
