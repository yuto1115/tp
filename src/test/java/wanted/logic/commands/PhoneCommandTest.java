package wanted.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static wanted.logic.commands.CommandTestUtil.assertCommandFailure;
import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;
import wanted.model.loan.Phone;
import wanted.model.loan.exceptions.PhoneUnchangedException;

public class PhoneCommandTest {
    private final Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());

    @Test
    void execute_deletePhoneNumber() throws PhoneUnchangedException {
        Loan loanToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PhoneCommand phoneCommand = new PhoneCommand(INDEX_FIRST_PERSON, Phone.EMPTY_PHONE);

        Loan updatedLoan = loanToUpdate.changePhone(Phone.EMPTY_PHONE);
        String expectedMessage = String.format(PhoneCommand.MESSAGE_DELETED_SUCCESS,
                Messages.format(updatedLoan));
        ModelManager expectedModel = new ModelManager(model.getLoanBook(), new UserPrefs());
        expectedModel.setPerson(loanToUpdate, updatedLoan);

        assertCommandSuccess(phoneCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validPhoneNumber() throws PhoneUnchangedException {
        Loan loanToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PhoneCommand phoneCommand = new PhoneCommand(INDEX_FIRST_PERSON, new Phone("88889999"));
        Loan updatedLoan = loanToUpdate.changePhone(new Phone("88889999"));

        String expectedMessage = String.format(PhoneCommand.MESSAGE_UPDATED_SUCCESS,
                Messages.format(updatedLoan));

        ModelManager expectedModel = new ModelManager(model.getLoanBook(), new UserPrefs());
        expectedModel.setPerson(loanToUpdate, updatedLoan);

        assertCommandSuccess(phoneCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        PhoneCommand phoneCommand = new PhoneCommand(outOfBoundIndex, new Phone("8899"));

        assertCommandFailure(phoneCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_phoneUnchanged_throwsCommandException() throws PhoneUnchangedException {
        Loan loanToUpdate = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        PhoneCommand phoneCommand = new PhoneCommand(INDEX_FIRST_PERSON, new Phone("88889999"));
        Loan updatedLoan = loanToUpdate.changePhone(new Phone("88889999"));

        model.setPerson(loanToUpdate, updatedLoan);

        assertCommandFailure(phoneCommand, model, PhoneCommand.MESSAGE_DUPLICATE_PHONE);
    }

    @Test
    public void equal() {
        PhoneCommand phoneCommand = new PhoneCommand(INDEX_FIRST_PERSON, Phone.EMPTY_PHONE);
        PhoneCommand phoneCommand1 = new PhoneCommand(INDEX_FIRST_PERSON, Phone.EMPTY_PHONE);
        PhoneCommand phoneCommand2 = new PhoneCommand(INDEX_FIRST_PERSON, new Phone(CommandTestUtil.VALID_PHONE));
        PhoneCommand phoneCommand3 = new PhoneCommand(INDEX_SECOND_PERSON, Phone.EMPTY_PHONE);

        //same object -> true
        assertEquals(phoneCommand, phoneCommand);

        //different object, same value -> true
        assertEquals(phoneCommand, phoneCommand1);

        //different type -> false
        assertNotEquals(phoneCommand, " ");

        //different object, different value -> false
        assertNotEquals(phoneCommand, phoneCommand2);

        //different object, different index -> false
        assertNotEquals(phoneCommand1, phoneCommand3);
    }
}
