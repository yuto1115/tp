package wanted.logic.commands;

import static wanted.testutil.TypicalPersons.getTypicalAddressBook;
import static wanted.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import wanted.model.LoanBook;
import wanted.model.Model;
import wanted.model.ModelManager;
import wanted.model.UserPrefs;
import wanted.model.loan.Loan;

public class SortCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_Successfully() {
        LoanBook loanBook = (LoanBook) expectedModel.getAddressBook();
        ObservableList<Loan> oldList = loanBook.getPersonList();
        Comparator<Loan> comparator =
                Comparator.nullsLast(Comparator.comparingInt(a -> (
                        -a.getAmount().remainingValue.getValueTimesOneHundred())));
        List<Loan> sortedList = oldList.sorted(comparator);
        loanBook.setPersons(sortedList);
        assertCommandSuccess(new SortCommand(), model, SortCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
