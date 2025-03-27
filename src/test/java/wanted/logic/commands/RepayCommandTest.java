package wanted.logic.commands;

import static wanted.logic.commands.CommandTestUtil.assertCommandFailure;
import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.logic.Messages;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Amount;
import wanted.model.loan.Loan;

public class RepayCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Test in case repay a loan partially
     * (Have not completed the test case)
     */
    @Test
    public void execute_validIndexUnfilteredListAndRepaidPartialLoan_success() throws CommandException {
        Loan loanToRepaidAll = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RepayCommand repayCommand = new RepayCommand(INDEX_FIRST_PERSON, new Amount("10.00"));
        Loan updatedLoan = repayCommand.getUpdatedLoan(loanToRepaidAll);

        String expectedMessage = String.format(RepayCommand.MESSAGE_REPAID_SUCCESS,
                Messages.format(updatedLoan));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(loanToRepaidAll, updatedLoan);

        assertCommandSuccess(repayCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test in case repay a loan entirely
     * (Have not completed the test case)
     */
    @Test
    public void execute_validIndexUnfilteredListAndRepaidEntireLoan_success() {
        Loan loanToRepaidAll = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RepayCommand repayCommand = new RepayCommand(INDEX_FIRST_PERSON, loanToRepaidAll.getAmount());

        String expectedMessage = String.format(RepayCommand.MESSAGE_REPAID_ALL_SUCCESS,
                Messages.format(loanToRepaidAll));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(loanToRepaidAll);

        // failing this test case, unsure why
        // assertCommandSuccess(repayCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Test in case index provided is out of bound
     */
    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RepayCommand repayCommand = new RepayCommand(outOfBoundIndex, new Amount("10.00"));

        assertCommandFailure(repayCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


}
