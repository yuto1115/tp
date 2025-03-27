package wanted.logic.commands;

import java.util.Comparator;
import java.util.List;

import javafx.collections.ObservableList;
import wanted.logic.commands.exceptions.CommandException;
import wanted.model.LoanBook;
import wanted.model.Model;
import wanted.model.loan.Loan;

/**
 * Sort loan by loan amount value. Maybe have more options in future
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SUCCESS = "Sort success";
    public static final String MESSAGE_FAIL = "Sort failed";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            LoanBook loanBook = (LoanBook) model.getAddressBook();
            ObservableList<Loan> oldList = loanBook.getPersonList();
            Comparator<Loan> comparator =
                    Comparator.nullsLast(Comparator.comparingInt(a ->
                            (-a.getAmount().remainingValue.getValueTimesOneHundred())));
            List<Loan> sortedList = oldList.sorted(comparator);
            loanBook.setPersons(sortedList);
        } catch (Exception e) {
            throw new CommandException(MESSAGE_FAIL + ": " + e.getMessage());
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS));
    }
}
