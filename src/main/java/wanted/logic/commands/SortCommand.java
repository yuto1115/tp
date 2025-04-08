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
    public static final String MESSAGE_SUCCESS = "Sorted the persons in descending order of remaining loan amount";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        LoanBook loanBook = (LoanBook) model.getLoanBook();
        ObservableList<Loan> oldList = loanBook.getPersonList();
        Comparator<Loan> comparator =
                Comparator.nullsLast(Comparator.comparingInt(a -> (
                        -a.getLoanAmount().getRemainingAmount().getValueTimesOneHundred())));
        List<Loan> sortedList = oldList.sorted(comparator);
        loanBook.setPersons(sortedList);
        return new CommandResult(String.format(MESSAGE_SUCCESS));
    }
}
