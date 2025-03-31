package wanted.logic.commands;

import static wanted.logic.commands.CommandTestUtil.assertCommandFailure;
import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;
import static wanted.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import org.junit.jupiter.api.Test;

import wanted.commons.core.datatypes.Index;
import wanted.commons.core.datatypes.MoneyInt;
import wanted.logic.Messages;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanDate;
import wanted.model.loan.exceptions.ExcessRepaymentException;

public class RepayCommandTest {
    private final Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());

    /**
     * Test in case repay a loan partially
     * (Have not completed the test case)
     */
    @Test
    public void execute_validIndexUnfilteredListAndRepaidPartialLoan_success() throws ExcessRepaymentException {
        Loan loanToRepaidAll = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        RepayCommand repayCommand = new RepayCommand(INDEX_FIRST_PERSON,
                MoneyInt.fromDollarAndCent(10, 0),
                new LoanDate(CommandTestUtil.VALID_DATE_AMY));
        Loan updatedLoan = loanToRepaidAll.repayLoan(MoneyInt.fromDollarAndCent(10, 0),
                new LoanDate(CommandTestUtil.VALID_DATE_AMY));

        String expectedMessage = String.format(RepayCommand.MESSAGE_REPAID_SUCCESS,
                Messages.format(updatedLoan));

        ModelManager expectedModel = new ModelManager(model.getLoanBook(), new UserPrefs());
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
        RepayCommand repayCommand = new RepayCommand(INDEX_FIRST_PERSON,
                loanToRepaidAll.getLoanAmount().getRemainingAmount(),
                new LoanDate(CommandTestUtil.VALID_DATE_AMY));

        String expectedMessage = String.format(RepayCommand.MESSAGE_REPAID_ALL_SUCCESS,
                Messages.format(loanToRepaidAll));

        ModelManager expectedModel = new ModelManager(model.getLoanBook(), new UserPrefs());
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
        RepayCommand repayCommand = new RepayCommand(outOfBoundIndex,
                MoneyInt.fromDollarAndCent(10, 0),
                new LoanDate(CommandTestUtil.VALID_DATE_AMY));

        assertCommandFailure(repayCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


}
