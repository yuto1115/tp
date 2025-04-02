package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.commands.CommandTestUtil.*;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.model.LoanBook;
import wanted.logic.Messages;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.testutil.EditLoanDescriptorBuilder;
import wanted.testutil.PersonBuilder;

class RenameCommandTest {

    private Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws ExcessRepaymentException {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Loan lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson).withName(VALID_NAME_BOB);
        Loan editedPerson = personInList.withName(VALID_NAME_BOB).build();
        BaseEdit.EditLoanDescriptor descriptor =
                new EditLoanDescriptorBuilder().withName(VALID_NAME_BOB).build();

        RenameCommand renameCommand = new RenameCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(RenameCommand.MESSAGE_RENAME_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new LoanBook(model.getLoanBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(renameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Loan personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Loan editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        RenameCommand renameCommand = new RenameCommand(INDEX_FIRST_PERSON,
                new EditLoanDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(RenameCommand.MESSAGE_RENAME_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new LoanBook(model.getLoanBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(renameCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws ExcessRepaymentException {
        Loan firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        BaseEdit.EditLoanDescriptor descriptor = new EditLoanDescriptorBuilder(firstPerson).build();
        RenameCommand renameCommand = new RenameCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(renameCommand, model, RenameCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() throws ExcessRepaymentException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit loan in filtered list into a duplicate in address book
        Loan personInList = model.getLoanBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        RenameCommand renameCommand = new RenameCommand(INDEX_FIRST_PERSON,
                new EditLoanDescriptorBuilder(personInList).build());

        assertCommandFailure(renameCommand, model, RenameCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        BaseEdit.EditLoanDescriptor descriptor = new EditLoanDescriptorBuilder().withName(VALID_NAME_BOB).build();
        RenameCommand renameCommand = new RenameCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(renameCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getLoanBook().getPersonList().size());

        RenameCommand renameCommand = new RenameCommand(outOfBoundIndex,
                new EditLoanDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(renameCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    //TODO: equals tests
}