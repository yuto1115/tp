package wanted.logic.commands;

import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;
import static wanted.testutil.TypicalPersons.getTypicalLoanBook;

import org.junit.jupiter.api.Test;

import wanted.model.LoanBook;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyLoanBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyLoanBook_success() {
        Model model = new ModelManager(getTypicalLoanBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalLoanBook(), new UserPrefs());
        expectedModel.setLoanBook(new LoanBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
