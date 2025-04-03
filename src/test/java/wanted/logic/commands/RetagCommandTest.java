package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static wanted.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static wanted.logic.commands.CommandTestUtil.assertCommandFailure;
import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;
import static wanted.logic.commands.CommandTestUtil.showPersonAtIndex;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.model.LoanBook;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.testutil.EditLoanDescriptorBuilder;
import wanted.testutil.PersonBuilder;

/**
 * Tests based on rename command test
 */
public class RetagCommandTest {

    private Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws ExcessRepaymentException {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Loan lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson).withTags(VALID_TAG_HUSBAND);
        Loan editedPerson = personInList.withTags(VALID_TAG_HUSBAND).build();
        BaseEdit.EditLoanDescriptor descriptor =
                new EditLoanDescriptorBuilder().withTags(VALID_TAG_HUSBAND).build();

        RetagCommand retagCommand = new RetagCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(RetagCommand.MESSAGE_RENAME_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new LoanBook(model.getLoanBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(retagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Loan personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Loan editedPerson = new PersonBuilder(personInFilteredList).withTags(VALID_TAG_FRIEND).build();
        RetagCommand retagCommand = new RetagCommand(INDEX_FIRST_PERSON,
                new EditLoanDescriptorBuilder().withTags(VALID_TAG_FRIEND).build());

        String expectedMessage = String.format(RetagCommand.MESSAGE_RENAME_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new LoanBook(model.getLoanBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(retagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        BaseEdit.EditLoanDescriptor descriptor = new EditLoanDescriptorBuilder().withTags(VALID_TAG_HUSBAND).build();
        RetagCommand renameCommand = new RetagCommand(outOfBoundIndex, descriptor);

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

        RetagCommand renameCommand = new RetagCommand(outOfBoundIndex,
                new EditLoanDescriptorBuilder().withTags(VALID_TAG_HUSBAND).build());

        assertCommandFailure(renameCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
